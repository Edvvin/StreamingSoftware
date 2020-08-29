package inner;

import my.rmi.*;
import java.util.*;

public class CentralServer {

	ArrayList<Subserver> subs;
	Users users;
	HashMap<String, ArrayList<Subserver>> chunks;

	public CentralServer() {
		subs = new ArrayList<>();
		users = new Users();
		chunks = new HashMap<>();
	}
	
	public synchronized void registerSubserver(Subserver ss) {
		subs.add(ss);
	}

	public synchronized Subserver registerUser(User user) {
		int max = -1;
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
		ArrayList<User> users = ss.strip();
		subs.remove(ss);
		for(User u: users) {
			registerUser(u);
		}
		return;
	}
	
	public synchronized Users getUsers() {
		return (Users) users.clone();
	}

}
