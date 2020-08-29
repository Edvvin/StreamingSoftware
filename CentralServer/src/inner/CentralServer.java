package inner;

import my.rmi.*;
import java.util.*;

public class CentralServer {

	public static final int ORDER_COUNT = 3;
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
		// remove from list, reroute users, remove chunks, reroute notifications
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
	
	public synchronized ArrayList<Order> getOrders(Subserver ss){
		ArrayList<Order> orders = new ArrayList<>();
		Random r = new Random();
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
		ArrayList<Order> randomOrders = new ArrayList<>();
		for(int i = 0; i<ORDER_COUNT; i++) {
			Order temp = orders.get(r.nextInt(orders.size()));
			randomOrders.add(temp);
			orders.remove(temp);
		}
		return randomOrders;
	}

}
