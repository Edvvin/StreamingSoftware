package my.rmi;

import java.io.Serializable;

public class User implements Serializable{
	String user, pass;
	
	public User(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}
	
	public String getUsername() {
		return user;
	}
	public boolean check(String pass) {
		return pass.equals(this.pass);
	}
}
