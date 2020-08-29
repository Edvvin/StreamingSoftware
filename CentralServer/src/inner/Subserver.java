package inner;

import java.util.ArrayList;

import my.rmi.*;

public class Subserver {
	String host;
	int port;
	SSRemote ss;
	boolean isRegistered;
	ArrayList<User> users;
	
	public Subserver(String host, int port, SSRemote ss) {
		this.host = host;
		this.port = port;
		this.ss = ss;
		this.isRegistered = false;
		this.users = new ArrayList<>();
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
		users = null;
		return tempUsers;
	}
	
	public synchronized boolean isRegistered() {
		return isRegistered;
	}
	
	
}
