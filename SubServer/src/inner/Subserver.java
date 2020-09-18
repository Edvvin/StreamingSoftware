package inner;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.*;
import java.util.*;

import com.sun.tools.javac.Main;

import my.utils.*;

import my.rmi.*;
import my.rmi.RoomState.State;

public class Subserver {
	HashMap<String, Movie> movies;
	int port;
	String dir;
	String cshost;
	int csport;
	CSRemote csrmi;
	Users users;
	MovieElf elf;
	public HelloElf helloelf;
	HashMap<Room, RoomState> rooms;
	HashMap<Room, ArrayList<Boolean>> recieved;
	boolean reconnecting;
	
	public Subserver(int port, String dir, String cshost, int csport) {
		this.port = port;
		this.cshost = cshost;
		this.csport = csport;
		this.users =  new Users();
		movies = new HashMap<>();
		rooms = new HashMap<>();
		recieved = new HashMap<>();
		this.dir = dir;
		elf = new MovieElf();
		helloelf = new HelloElf();
		Path indexPath = Path.of(dir, "index.txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(indexPath.toFile()));
			Iterator<String> lines = br.lines().iterator();
			lines.forEachRemaining(line -> {
					String parts[] = line.split(":");
					String movieName = parts[0];
					Path filePath = Path.of(dir, movieName);
					if(filePath.toFile().exists()) {
						int chunkNum = Integer.parseInt(parts[1]);
						Movie m;
						if(movies.containsKey(movieName)) {
							m = movies.get(movieName);
						}
						else {
							m = new Movie(movieName,
									filePath.toFile().length());
							movies.put(movieName, m);
						}
						m.addChunk(chunkNum);
					}
				}
			);
			
		} catch (IOException e) {
			try {
				indexPath.toFile().createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
	
	private synchronized void clearRecieved (Room room) {
		ArrayList<Boolean> rec = recieved.get(room);
		if(rec != null) {
			rec.clear();
			for(int i = 0;i < room.getBuddies().size(); i++) {
				rec.add(false);
			}
		}
	}
	
	private synchronized boolean getRecieved(Room room, String user) {
		ArrayList<Boolean> rec = recieved.get(room);
		if(rec == null) {
			rec = new ArrayList<Boolean>();
			for(int i = 0;i < room.getBuddies().size(); i++) {
				rec.add(false);
			}
			recieved.put(room, rec);
		}
		int i = 0;
		for (String u : room.getBuddies()) {
			if(u.equals(user)) {
				boolean rez = rec.get(i);
				rec.set(i, true);
				return rez;
			}
			i++;
		}
		assert(false);
		return false;
	}
	
	public synchronized void upload(String name, Chunk chunk, boolean newFile) throws IOException {
		Path filePath = Path.of(dir, name);
		FileOutputStream fs = new FileOutputStream(filePath.toFile(), newFile);
		fs.write(chunk.getBytes());
		fs.close();
	}
	
	public synchronized void uploadFinished(String name) throws NotSycnhedException, CSNotAvailException {
		Path filePath = Path.of(dir, name);
		long numOfChunks = 0;
		long size = filePath.toFile().length();
		if(size % Chunk.CHUNK_SIZE == 0) {
			numOfChunks = size / Chunk.CHUNK_SIZE;
		}
		else {
			numOfChunks = size / Chunk.CHUNK_SIZE + 1;
		}
		Movie m = new Movie(name, filePath.toFile().length());
		for(int i = 0; i < numOfChunks; i++)
			m.addChunk(i);
		movies.put(name, m);
		Path indexPath = Path.of(dir, "index.txt");
		try( 
			FileWriter fw = new FileWriter(indexPath.toFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			)
		{
			for(int i = 0; i < numOfChunks; i++) {
				pw.println(name + ":" + i);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		try {
			csrmi.newMovie(port, name, filePath.toFile().length());
		} catch (RemoteException e) {
			reconnect();
			throw new CSNotAvailException();
		}
		catch(NotSycnhedException e) {
			reconnect();
			throw e;
		}
	}
	
	public int getPort() {
		return port;
	}
	
	private ArrayList<Movie> toArrayList(){
		ArrayList<Movie> res = new ArrayList<>();
		movies.forEach((key,value)->{
			res.add(value);
		});
		return res;
	}
	
	public synchronized void connect() {
		Registry regCS;
		try {
			regCS = LocateRegistry.getRegistry(cshost, csport);
			csrmi = (CSRemote) regCS.lookup("/csrmi");
			String temp = InetAddress.getLocalHost().getHostAddress();
			SubServerState sss = csrmi.connectToCS(temp, port, toArrayList());
			if(sss == null) {
				//TODO
			}
			users = sss.getUsers();
			rooms = sss.getRooms();
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();//TODO RETRY
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}
	
	public synchronized void reconnect() {
		Thread t = new Thread() {
			@Override
			public void run() {
				synchronized(server.Main.ss) {
					//TODO ako treba da se doda nesto za notificationz
					//TODO da se pobrines za onaj thread HelloElf
					while(true) {
						Registry regCS;
						try {
							regCS = LocateRegistry.getRegistry(cshost, csport);
							csrmi = (CSRemote) regCS.lookup("/csrmi");
							String temp = InetAddress.getLocalHost().getHostAddress();
							SubServerState sss = csrmi.connectToCS(temp, port, toArrayList());
							if(sss == null) {
								//TODO
								assert(false);
							}
							users = sss.getUsers();
							rooms = sss.getRooms();
							server.Main.ss.elf.newMovie();
							server.Main.ss.helloelf.notify();
						} catch (RemoteException | NotBoundException e) {
							try {
								Thread.sleep(Consts.RECONNECT_SLEEP_TIME);
							} catch (InterruptedException e1) {
							}
						} catch (UnknownHostException e) {
							e.printStackTrace();
							System.out.println(e.getMessage());
						}
					}
				}
			}
		};
		if(!reconnecting) {
			reconnecting = true;
			t.start();
		}
	}
	
	public void newUser(String username, String password) {
		users.addUser(username, password);
	}
	
	public boolean checkUser(String username, String password) {
		return users.checkLogin(username, password);
	}
	
	public void wakeElf() {
		elf.start();
	}

	public boolean carryOut(Order o) {
		Path filePath = Path.of(dir, o.getMovie());
		File file = filePath.toFile();
		boolean newFile = false;
		if(!file.exists()) {
			try {
				newFile = true;
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try(RandomAccessFile f = new RandomAccessFile(file, "rws")){
			if(newFile)
				f.setLength(o.getFileSize());
			Registry regSS = LocateRegistry.getRegistry(o.getHost(), o.getPort());
			SSRemote ssrmi = (SSRemote) regSS.lookup("/ssrmi");
			Chunk c = ssrmi.download(o.getMovie(), o.getIndex());
			f.seek(c.getIndex()*Chunk.CHUNK_SIZE);
			f.write(c.getBytes());
			Path indexPath = Path.of(dir, "index.txt");
			try( 
				FileWriter fw = new FileWriter(indexPath.toFile(),true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter pw = new PrintWriter(bw);
				)
			{
				pw.println(o.getMovie() + ":" + o.getIndex());
				return true;
			}
			catch(IOException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				return false;
			}
		}
		catch(IOException e) {
			e.printStackTrace();
			return false;
		} catch (NotBoundException e) {
			// TODO do this
			e.printStackTrace();
			return false;
		}
	}

	public Chunk download(String name, int chunkIndex) {
		if(!movies.containsKey(name))
			return null;
		Path filePath = Path.of(dir, name);
		Chunk c = null;
		try(RandomAccessFile f = new RandomAccessFile(filePath.toFile(), "r")){
			c = new Chunk(chunkIndex);
			f.seek(c.getIndex()*Chunk.CHUNK_SIZE);
			int mylen = f.read(c.getBytes());
			if(mylen < 0)
				return null;
			c.pack(mylen);
		}catch(RemoteException e1){
			 //TODO
			e1.printStackTrace();
		}
		catch(IOException e2) {
			e2.printStackTrace();
		}
		return c;
	}
	
	public void newMovie() {
		elf.newMovie();
	}

	public boolean createRoom(Room room) throws NotSycnhedException, CSNotAvailException {
		try {
			return csrmi.createRoom(port, room);
		} catch (RemoteException e) {
			reconnect();
			throw new CSNotAvailException();
		}
		catch(NotSycnhedException e) {
			reconnect();
			throw e;
		}
	}

	public boolean setRoomState(Room room, double time, State state) throws CSNotAvailException, NotSycnhedException {
		try {
			return csrmi.setRoomState(port, room, time, state);
		}
		catch(RemoteException e) {
			reconnect();
			throw new CSNotAvailException();
		}
		catch(NotSycnhedException e) {
			reconnect();
			throw e;
		}
	}

	public synchronized RoomState getRoomState(Room room, String user, boolean force) {
		boolean recieved = false;
		if(!user.equals(room.getAdmin())) {
			recieved = getRecieved(room, user);
		}
		else {
			RoomState rs = rooms.get(room);
			return rs;
		}
		if(force) {
			RoomState rs = rooms.get(room);
			return rs;
		}
		while(recieved) {
			try {
				wait();
			} catch (InterruptedException e) {
				//TODO sta ako se zaglavi a klijent ugasi
				e.printStackTrace();
			}
			recieved = getRecieved(room, user);
		};
		RoomState rs = rooms.get(room);
		return rs;
	}

	public synchronized void updateState(Room room, double time, State state) {
		if(!rooms.containsKey(room))
			rooms.putIfAbsent(room, new RoomState(time, state));
		rooms.get(room).setRoomState(time, state);
		clearRecieved(room);
		notifyAll();
	}

	public synchronized ArrayList<Room> getRooms() {
		ArrayList<Room> roomList = new ArrayList<>();
		rooms.forEach((key,value)->{
			roomList.add(key);
		});
		return roomList;
	}

	public synchronized void newRoom(Room room, RoomState rs) {
		// TODO
		rooms.putIfAbsent(room, rs);
	}
}
