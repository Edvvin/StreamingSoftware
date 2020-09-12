package my.rmi;

import java.io.Serializable;
import java.util.*;

public class Room implements Serializable {
	private String movie, admin, roomName;
	private boolean isPrivate;
	private ArrayList<String> buddies;
	public Room(String movie, String admin,
			String roomName, ArrayList<String> buddies) {
		isPrivate = false;
		this.movie = movie;
		this.admin = admin;
		this.roomName = roomName;
		this.buddies = buddies;
	}
	
	public Room(String movie, String admin) {
		isPrivate = true;
		this.movie = movie;
		this.admin = admin;
		this.roomName = "";
	}
	
	public String getMovie() {
		return movie;
	}
	
	public String getAdmin() {
		return admin;
	}
	
	public String getRoomName() {
		return roomName;
	}
	
	@Override
	public boolean equals(Object o) {
		Room r = (Room)o;
		return movie.equals(r.movie) &&
				admin.equals(r.admin) &&
				(roomName.equals(r.roomName) || isPrivate);
	}
	
	@Override
    public int hashCode() {        
        int result =  movie.hashCode();        
        result = 31 * result + admin.hashCode();
        result = 31 * result + (roomName != null ? roomName.hashCode() : 0);
        return result;    
    }    
}
