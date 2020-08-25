package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CSRemote extends Remote{
	public String login(String username, String password) throws RemoteException;
}
