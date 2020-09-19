package inner;

import my.rmi.*;
import my.rmi.RoomState.State;

import java.rmi.RemoteException;
import java.util.*;
import my.utils.*;
import server.CentralServerRMI;

public class CentralServer {

	ArrayList<Subserver> subs;
	Users users;
	HashMap<String, ArrayList<Subserver>> chunks;
	HashMap<String, Long> fileSizes;
	HashMap<Room, RoomState> rooms;
	RoomElf elf;
	SSElf sself;
	int port;

	public CentralServer(int port) {
		subs = new ArrayList<>();
		users = new Users();
		chunks = new HashMap<>();
		rooms = new HashMap<>();
		fileSizes = new HashMap<>();
		this.port = port;
		elf = new RoomElf();
		elf.start();
		sself = new SSElf();
		sself.start();
	}
	
	public synchronized String login(String username, String password) {
		String session = "FAILED";
		User u = new User(username, password);
		if(!getUsers().exists(u))
			return "INVALID";
		Subserver ss = routeUser(u);//check if in users
		if(ss == null) {
			return "FAILED";
		}
		SSRemote ssrmi = ss.getRMI();
		if(ssrmi == null) {
			return "FAILED"; // TODO
		}
		try {
			session = ssrmi.login(username, password);
		}
		catch(RemoteException e) {
			removeSubserver(ss);
		}
		return session;
	}
	
	public synchronized void registerSubserver(Subserver ss, ArrayList<Movie> wih) {
		for(Movie m : wih) {
			for(int chunk : m.getChunks()) {
				String chid = m.getName() + ":" + chunk;
				if(chunks.containsKey(chid)) {
					ArrayList<Subserver> tempsubs = chunks.get(chid);
					tempsubs.add(ss);
				}
				else {
					ArrayList<Subserver> tempsubs = new ArrayList<>(); 
					tempsubs.add(ss);
					chunks.put(chid,tempsubs);
					fileSizes.putIfAbsent(m.getName(), m.getSize());
				}
			}
		}
		if(subs.size() == 0)
			ss.setRegistered();
		subs.add(ss);
	}

	public synchronized Subserver registerUser(User user) throws UserExistsException, NoSubserverException {
		int min = -1;
		if(users.exists(user))
			throw new UserExistsException();
		Subserver best = null;
		for(Subserver ss : subs) {
			if((min == -1 || min > ss.getNumOfUsers()) && ss.isRegistered()) {
				min = ss.getNumOfUsers();
				best = ss;
			}
		}
		if(best != null) {
			best.addUser(user);
		}
		else {
			throw new NoSubserverException();
		}
		ArrayList<Subserver> toRem = new ArrayList<>();
		for(Subserver ss: subs) {
			try {
				ss.getRMI().newUser(user.getUsername(), user.getPassword());
			} catch (RemoteException e) {
				toRem.add(ss);
			}
		}
		for(Subserver ss: toRem) {
			removeSubserver(ss);
		}
		users.addUser(user.getUsername(), user.getPassword());
		return best;
	}
	
	public synchronized Subserver routeUser(User u) {
		for(Subserver ss : subs) {
			if(ss.hasUser(u)) {
				return ss;
			}
		}
		return null;
	}
	
	public synchronized void removeSubserver(Subserver ss) {
		ArrayList<User> users = ss.strip();
		subs.remove(ss);
		chunks.forEach((key, value)->{
			for(Subserver s : value) {
				if(ss.toString().equals(s.toString())) {
					value.remove(s);
					break;
				}
			}
		});
		//TODO fileSizes possibly remove from here
		if(subs.size() == 1) {
			subs.get(0).setRegistered();
		}
		for(User u: users) {
			int min = -1;
			Subserver best = null;
			for(Subserver s : subs) {
				if((min == -1 || min > s.getNumOfUsers()) && s.isRegistered()) {
					min = s.getNumOfUsers();
					best = s;
				}
			}
			if(best != null) {
				best.addUser(u);
			}
		}
		return;
	}
	
	public synchronized Users getUsers() {
		return users;
	}
	
	public synchronized ArrayList<Order> getOrders(String sshost, int ssport){
		ArrayList<Order> orders = new ArrayList<>();
		Random r = new Random();
		for(Subserver ns: subs) {
			if(ns.getHost().equals(sshost) && ns.getPort() == ssport) {
				Subserver ss = ns;
				chunks.forEach((key,value) -> {
					if(!value.contains(ss)) {
						Subserver orderSS = value.get(r.nextInt(value.size()));
						String[] parts = key.split(":");
						orders.add(
								new Order(orderSS.getHost(), orderSS.getPort(),
										parts[0], Integer.parseInt(parts[1]), fileSizes.get(parts[0]))
								);
					}
				});

				if(!ss.isRegistered()) {
					boolean shouldReg = true;
					int regCnt = 0;
					for(Subserver s : subs) {
						if(s.isRegistered()) {
							regCnt++;
						}
					}
					for(Order o: orders) {
						String chid = o.getMovie() + ":" + o.getIndex();
						int regCnt2 = 0;
						for(Subserver s : chunks.get(chid)) {
							if(s.isRegistered()) {
								regCnt2++;
							}
						}
						if(regCnt == regCnt2) {
							shouldReg = false;
							break;
						}
					}
					if(shouldReg)
						ss.setRegistered();
				}
			}
		}
		if(orders.size() == 0)	
			return orders;
		ArrayList<Order> randomOrders = new ArrayList<>();
		for(int i = 0; i<Consts.ORDER_COUNT; i++) {
			if(orders.size() == 0)
				break;
			Order temp = orders.get(r.nextInt(orders.size()));
			randomOrders.add(temp);
			orders.remove(temp);
		}
		return randomOrders;
	}

	public synchronized boolean newMovie(String sshost, int ssport, String name, long fileSize) {
		long numOfChunks;
		if(fileSize % Chunk.CHUNK_SIZE == 0) {
			numOfChunks = fileSize / Chunk.CHUNK_SIZE;
		}
		else {
			numOfChunks = fileSize / Chunk.CHUNK_SIZE + 1;
		}
		for(Subserver ss: subs) {
			if(ss.getHost().equals(sshost) && ss.getPort() == ssport) {
				for(int i=0;i<numOfChunks; i++) {
					String chid = name + ":" + i;
					if(chunks.containsKey(chid))
						return false;
				}
				for(int i=0;i<numOfChunks; i++) {
					String chid = name + ":" + i;
					ArrayList<Subserver> templist = new ArrayList<>();
					templist.add(ss);
					chunks.put(chid,templist);
					fileSizes.putIfAbsent(name, fileSize);
				}
				ArrayList<Subserver> toRem = new ArrayList<>();
				for(Subserver s: subs) {
					try {
						s.getRMI().newMovie();
					} catch (RemoteException e) {
						toRem.add(s);
					}
				}
				
				for(Subserver s : toRem) {
					removeSubserver(s);
				}
			}
		}
		return false;
	}

	public synchronized void registerOrders(String sshost, int ssport, ArrayList<Order> orders) {
		for(Subserver ss: subs) {
			if(ss.getHost().equals(sshost) && ss.getPort() == ssport) {
				for(Order o : orders) {
					chunks.get(o.getMovie() + ":" + o.getIndex()).add(ss);
				}
			}
		}
	}

	public synchronized ArrayList<String> getRegisteredMovies() {
		int numReg = 0;
		ArrayList<String> result = new ArrayList<>();
		HashMap<String, Long> partCnt = new HashMap<>();
		for(Subserver ss : subs) {
			if(ss.isRegistered())
				numReg++;
		}
		final int finalNumReg = numReg; // Java wants this idk y
		chunks.forEach((key, value)->{
			int numReg2 = 0;
			for(Subserver ss : value) {
				if(ss.isRegistered())
					numReg2++;
			}
			if(numReg2 == finalNumReg) {
				String[] parts = key.split(":");
				partCnt.putIfAbsent(parts[0],  0L);
				partCnt.put(parts[0], partCnt.get(parts[0]) + 1);
			}
		});
		
		partCnt.forEach((key, value)->{
			long numOfChunks = 0;
			long fileSize = fileSizes.get(key);
			if(fileSize % Chunk.CHUNK_SIZE == 0) {
				numOfChunks = fileSize / Chunk.CHUNK_SIZE;
			}
			else {
				numOfChunks = fileSize / Chunk.CHUNK_SIZE + 1;
			}
			if(value == numOfChunks) {
				result.add(key);
			}
		});
		
		return result;
	}
	
	public synchronized boolean createRoom(Room room) {
		RoomState state = new RoomState(0, RoomState.State.PAUSED);
		if(rooms.containsKey(room))
			return false;
		rooms.putIfAbsent(room, state);
		ArrayList<Subserver> toRem = new ArrayList<>();
		for(Subserver s : subs) {
			try {
				s.getRMI().newRoom(room, state);
			} catch (RemoteException e) {
				toRem.add(s);
			}
		}
		
		for(Subserver s : toRem) {
			removeSubserver(s);
		}
		return true;
	}

	public synchronized void setRoomState(Room room, double time, State state) {
		rooms.get(room).setRoomState(time, state);
		ArrayList<Subserver> toRem = new ArrayList<>();
		for(Subserver s : subs) {
			try {
				s.getRMI().updateState(room, time, state);
			} catch (RemoteException e) {
				toRem.add(s);
			}
		}
		
		for(Subserver s : toRem) {
			removeSubserver(s);
		}
	}

	public synchronized HashMap<Room, RoomState> getRooms() {
		return rooms;
	}

	public synchronized String complain(String user, ArrayList<String> tried) {
		Subserver ssold = routeUser(new User(user, "123"));
		if(ssold == null)
			return "FAILED";
		if(!tried.contains(ssold.toString()))
			return ssold.toString();
		ArrayList<Subserver> baggie = new ArrayList<>();
		for(Subserver s : subs) {
			if(s.isRegistered() && !tried.contains(s.toString())) {
				baggie.add(s);
			}
		}
		if(baggie.size() == 0) {
			return "FAILED";
		}
		int min = -1;
		Subserver best = null;
		for(Subserver s : baggie) {
			if(min == -1 || min > s.getNumOfUsers()) {
				min = s.getNumOfUsers();
				best = s;
			}
		}
		Subserver ssnew = best;
		ssnew.addUser(ssold.removeUser(user));
		return ssnew.toString();
	}

	public synchronized void checkSS(String ssid) throws NotSycnhedException {
		for(Subserver ss : subs) {
			if(ss.toString().equals(ssid)) {
				return;
			}
		}
		throw new NotSycnhedException();
	}

	public synchronized void hello(String ssid) {
		for(Subserver s : subs) {
			if(s.toString().equals(ssid)) {
				s.helloCnt = 0;
				s.lastUpdate = System.currentTimeMillis();
				return;
			}
		}
	}
	

}
