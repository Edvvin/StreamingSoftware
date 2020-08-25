package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;

public class SubServerRMI extends UnicastRemoteObject implements SSRemote {
	
	public SubServerRMI() throws RemoteException {
		super();
	}

	@Override
	public String login(String username, String password) throws RemoteException {
		String ssid = "INVALID";
		try {
			ssid = RemoteServer.getClientHost() + ":" + Main.ssport;
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return ssid;
	}

}
