package inner;

import java.util.ArrayList;

import my.rmi.*;

public class Subserver {
	private String host;
	private int port;
	private SSRemote ss;
	private boolean isRegistered;
	private ArrayList<User> users;
	int helloCnt = 0;
	long lastUpdate;
	
	public Subserver(String host, int port, SSRemote ss) {
		this.host = host;
		this.port = port;
		this.ss = ss;
		this.isRegistered = true;
		this.users = new ArrayList<>();
		this.lastUpdate = System.currentTimeMillis();
	}
	
	public synchronized void addUser(User user) {
		users.add(user);
	}
	
	public synchronized int getNumOfUsers() {
		return users.size();
	}
	
	public synchronized boolean hasUser(User u) {
		for(User user : users) {
			if(user.getUsername().equals(u.getUsername())) {
				return true;
			}
		}
		return false;
	}
	
	public SSRemote getRMI() {
		return ss;
	}
	
	public synchronized ArrayList<User> strip(){
		ArrayList<User> tempUsers = users;
		users = new ArrayList<User>();
		return tempUsers;
	}
	
	public synchronized boolean isRegistered() {
		return isRegistered;
	}
	
	public synchronized void setRegistered() {
		isRegistered = true;
	}

	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	@Override
	public String toString() {
		return host + ":" + port;
	}
	
	public User removeUser(String user) {
		for(User u: users) {
			if(u.getUsername().equals(user)) {
				users.remove(u);
				return u;
			}
		}
		return null;
	}
	
	
}
