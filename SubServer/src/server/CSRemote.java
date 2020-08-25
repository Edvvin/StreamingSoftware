package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CSRemote extends Remote{
	public String login() throws RemoteException;
}
