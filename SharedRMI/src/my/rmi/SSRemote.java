package my.rmi;

import java.io.IOException;
import java.rmi.*;
import java.util.ArrayList;

public interface SSRemote extends Remote{
	public String login(String username, String password) throws RemoteException;
	public void newMovie() throws RemoteException;
	public void upload(String name, Chunk chunk, boolean newFile) throws IOException, RemoteException;
	public void uploadFinished(String name) throws RemoteException, NotSycnhedException, CSNotAvailException;
	public void newUser(String username, String password) throws RemoteException;
	public Chunk download(String name, int chunkIndex) throws RemoteException;
	public boolean createRoom(Room room) throws RemoteException, NotSycnhedException, CSNotAvailException;
	public boolean setRoomState(Room room, double time, RoomState.State state) throws RemoteException, CSNotAvailException, NotSycnhedException;
	public RoomState getRoomState(Room room, String user, boolean force) throws RemoteException;
	public void updateState(Room room, double time, RoomState.State state) throws RemoteException;
	ArrayList<Room> getRooms() throws RemoteException;
	void newRoom(Room room, RoomState rs) throws RemoteException;
	public void ping() throws RemoteException;
}
