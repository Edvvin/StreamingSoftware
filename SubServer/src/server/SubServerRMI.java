package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import my.rmi.*;
import my.rmi.RoomState.State;

public class SubServerRMI implements SSRemote {
	
	public SubServerRMI() throws RemoteException {
		super();
	}

	@Override
	public String login(String username, String password) throws RemoteException {
		String ssid = "INVALID";
		try {
			ssid = InetAddress.getLocalHost().getHostAddress() + ":" + Main.ss.getPort();
			if(!Main.ss.checkUser(username, password))
				return "INVALID";
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ssid;
	}

	@Override
	public void newMovie() throws RemoteException {
		Main.ss.newMovie();
	}

	@Override
	public void upload(String name, Chunk chunk, boolean newFile) throws IOException, RemoteException {
		Main.ss.upload(name, chunk, newFile);
	}
	
	@Override
	public void uploadFinished(String name) {
		Main.ss.uploadFinished(name);
	}
	
	@Override
	public void newUser(String username, String password) {
		Main.ss.newUser(username, password);
	}

	@Override
	public Chunk download(String name, int chunkIndex) throws RemoteException {
		return Main.ss.download(name, chunkIndex);
	}

	@Override
	public boolean createRoom(Room room) {
		return Main.ss.createRoom(room);
	}

	@Override
	public boolean setRoomState(Room room, double time, State state) throws RemoteException {
		return Main.ss.setRoomState(room,time, state);
	}

	@Override
	public RoomState getRoomState(Room room, String user, boolean force) throws RemoteException {
		return Main.ss.getRoomState(room, user, force);
	}

	@Override
	public void updateState(Room room, double time, State state) throws RemoteException {
		Main.ss.updateState(room, time, state);
	}
	
	@Override
	public ArrayList<Room> getRooms(){
		return Main.ss.getRooms();
	}

}
