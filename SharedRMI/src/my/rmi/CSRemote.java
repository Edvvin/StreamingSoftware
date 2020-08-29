package my.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CSRemote extends Remote{
	public String login(String username, String password) throws RemoteException;
	boolean connectToCS(String host, int port) throws RemoteException;
	Users getUsers() throws RemoteException;
	boolean register(String username, String password) throws RemoteException;
	void newMovie() throws RemoteException;
}
