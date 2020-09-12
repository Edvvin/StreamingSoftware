package my.rmi;

import java.io.IOException;
import java.rmi.*;

public interface SSRemote extends Remote{
	public String login(String username, String password) throws RemoteException;
	public void newMovie() throws RemoteException;
	public void upload(String name, Chunk chunk, boolean newFile) throws IOException, RemoteException;
	public void uploadFinished(String name) throws RemoteException;
	public void newUser(String username, String password) throws RemoteException;
	public Chunk download(String name, int chunkIndex) throws RemoteException;
	public boolean createRoom(Room room) throws RemoteException;
	public boolean setRoomState(Room room, double time, RoomState.State state) throws RemoteException;
	public RoomState getRoomState(Room room, String user, boolean force) throws RemoteException;
}
