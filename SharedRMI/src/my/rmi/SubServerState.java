package my.rmi;

import java.io.Serializable;
import java.util.HashMap;

public class SubServerState implements Serializable{
	Users users;
	HashMap<Room, RoomState> rooms;

	
	public SubServerState(Users users, HashMap<Room, RoomState> rooms) {
		this.users = users;
		this.rooms = rooms;
	}


	public Users getUsers() {
		return users;
	}
	
	public HashMap<Room, RoomState> getRooms() {
		return rooms;
	}
}
