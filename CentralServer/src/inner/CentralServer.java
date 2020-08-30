package inner;

import my.rmi.*;

import java.rmi.RemoteException;
import java.util.*;
import my.utils.*;
import server.CentralServerRMI;

public class CentralServer {

	ArrayList<Subserver> subs;
	Users users;
	HashMap<String, ArrayList<Subserver>> chunks;
	int port;

	public CentralServer(int port) {
		subs = new ArrayList<>();
		users = new Users();
		chunks = new HashMap<>();
		this.port = port;
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
				}
			}
		}
		if(subs.size() == 0)
			ss.setRegistered();
		subs.add(ss);
	}

	public synchronized Subserver registerUser(User user) {
		int max = -1;
		if(users.exists(user))
			return null;
		Subserver best = null;
		for(Subserver ss : subs) {
			if((max == -1 || max < ss.getNumOfUsers()) && ss.isRegistered()) {
				max = ss.getNumOfUsers();
				best = ss;
			}
		}
		if(best != null) {
			best.addUser(user);
		}
		for(Subserver ss: subs) {
			try {
				ss.getRMI().newUser(user.getUsername(), user.getPassword());
			} catch (RemoteException e) {
				// TODO no connection
				e.printStackTrace();
			}
		}
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
		//TODO
		// remove from list, reroute users, remove chunks, reroute notifications
		ArrayList<User> users = ss.strip();
		subs.remove(ss);
		for(User u: users) {
			registerUser(u);
		}
		//TODO if only one left then it becomes registered
		return;
	}
	
	public synchronized Users getUsers() {
		return (Users) users.clone();
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
										parts[0], Integer.parseInt(parts[1]))
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
			
		ArrayList<Order> randomOrders = new ArrayList<>();
		for(int i = 0; i<Consts.ORDER_COUNT; i++) {
			Order temp = orders.get(r.nextInt(orders.size()));
			randomOrders.add(temp);
			orders.remove(temp);
		}
		return randomOrders;
	}

	public synchronized boolean newMovie(String sshost, int ssport, String name, long numOfChunks) {
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
				}
				for(Subserver s: subs) {
					try {
						s.getRMI().newMovie();
					} catch (RemoteException e) {
						// TODO what nau
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}
	

}
