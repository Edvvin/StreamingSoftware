package my.rmi;

import java.io.Serializable;
import java.util.ArrayList;

public class Users implements Serializable, Cloneable{
	ArrayList<User> users;
	
	public Users() {
		users = new ArrayList<>();
	}

	synchronized boolean checkLogin(String user, String pass) {
		for(User u : users) {
			if(u.getUsername().equals(user))
				if(u.check(pass)) 
					return true;
		}
		return false;
	}
	
	synchronized void addUser(String user, String pass) {
		users.add(new User(user, pass));
	}
	
	@Override
	public Object clone() {
		Users u = new Users();
		u.users = (ArrayList<User>) this.users.clone();
		return u;
	}
}
