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
import my.utils.*;

import my.rmi.*;

public class Subserver {
	HashMap<String, Movie> movies;
	int port;
	String dir;
	String cshost;
	int csport;
	CSRemote csrmi;
	Users users;
	MovieElf elf;
	
	public Subserver(int port, String dir, String cshost, int csport) {
		this.port = port;
		this.cshost = cshost;
		this.csport = csport;
		this.users =  new Users();
		movies = new HashMap<>();
		this.dir = dir;
		elf = new MovieElf();
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
									filePath.toFile().getTotalSpace());
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
	
	public synchronized void upload(String name, Chunk chunk, boolean newFile) throws IOException {
		Path filePath = Path.of(dir, name);
		FileOutputStream fs = new FileOutputStream(filePath.toFile(), newFile);
		fs.write(chunk.getBytes());
		fs.close();
	}
	
	public synchronized void uploadFinished(String name) {
		Path filePath = Path.of(dir, name);
		long numOfChunks = 0;
		long size = filePath.toFile().length();
		if(size % Chunk.CHUNK_SIZE == 0) {
			numOfChunks = size / Chunk.CHUNK_SIZE;
		}
		else {
			numOfChunks = size / Chunk.CHUNK_SIZE + 1;
		}
		Movie m = new Movie(name, filePath.toFile().getTotalSpace());
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
			e.printStackTrace(); //TODO
		}
		try {
			csrmi.newMovie(port, name, filePath.toFile().getTotalSpace());
		} catch (RemoteException e) {
			// TODO 
			e.printStackTrace();
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
			users = csrmi.connectToCS(temp, port, toArrayList());
			if(users == null) {
				//TODO
			}
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();//TODO RETRY
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public void carryOut(Order o) {
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
		}
		catch(IOException e) {
			
		} catch (NotBoundException e) {
			// TODO do this
			e.printStackTrace();
		}
	}

	public Chunk download(String name, int chunkIndex) {
		if(!movies.containsKey(name))
			return null;
		Path filePath = Path.of(dir, name);
		Chunk c = null;
		try(RandomAccessFile f = new RandomAccessFile(filePath.toFile(), "r")){
			c = new Chunk(chunkIndex);
			int mylen = f.read(c.getBytes(), chunkIndex*Chunk.CHUNK_SIZE, Chunk.CHUNK_SIZE);
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
}
