package my.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface CSRemote extends Remote{
	public String login(String username, String password) throws RemoteException;
	public SubServerState connectToCS(String host, int port, ArrayList<Movie> wih) throws RemoteException;
	public Users getUsers() throws RemoteException;
	public boolean register(String username, String password) throws RemoteException;
	public boolean newMovie(int port, String name, long numOfChunks) throws RemoteException, NotSycnhedException;
	public ArrayList<Order> getOrders( int port) throws RemoteException, NotSycnhedException;
	public void registerOrders(int port, ArrayList<Order> orders) throws RemoteException, NotSycnhedException;
	public ArrayList<String> getRegisteredMoives() throws RemoteException;
	public boolean createRoom(int port, Room room) throws RemoteException, NotSycnhedException;
	boolean setRoomState(int port, Room room, double time, RoomState.State state) throws RemoteException, NotSycnhedException;
	HashMap<Room, RoomState> getRooms() throws RemoteException;
	public String complain(String user, ArrayList<String> tried) throws RemoteException;
	public void hello(int port) throws RemoteException;
}
