package server;

import java.rmi.*;

public interface SSRemote extends Remote{
	public String login(String username, String password) throws RemoteException ;
		
}
