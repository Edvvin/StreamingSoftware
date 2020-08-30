package my.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface CSRemote extends Remote{
	public String login(String username, String password) throws RemoteException;
	public Users connectToCS(String host, int port, ArrayList<Movie> wih) throws RemoteException;
	public Users getUsers() throws RemoteException;
	public boolean register(String username, String password) throws RemoteException;
	public boolean newMovie(int port, String name, long numOfChunks) throws RemoteException;
	public ArrayList<Order> getOrders(int port) throws RemoteException;
}
