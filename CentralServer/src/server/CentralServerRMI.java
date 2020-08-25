package server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class CentralServerRMI extends UnicastRemoteObject implements CSRemote{

	public CentralServerRMI() throws RemoteException {
		super();
	}

	@Override
	public String login(String username, String password) throws RemoteException {
		String session = "INVALID";
		String ssid = Main.userMap.get(username);
		if(ssid == null) {
			return "INVALID";
		}
		SSRemote ss = Main.ssMap.get(ssid);
		if(ss == null) {
			//TODO isto ko da ne moze da se konektuje
		}
		try {
			session = ss.login(username, password);
		}
		catch(RemoteException e) {
			//TODO
		}
		return session;
	}
	
	@Override 
	boolean connectToCS(String host, int port) {
		//TODO
		String ssid = host + ":" + ((Integer)port).toString();
		try {
			Registry regSS = LocateRegistry.getRegistry(host,port);
			SSRemote ss = (SSRemote) regSS.lookup("/ssrmi");
			Main.ssMap.put(ssid, ss);
			//TODO sync
		} catch (RemoteException err) {
			System.out.print(err.getMessage());//TODO
		} catch (NotBoundException err) {
			System.out.print("asfgsdgas");//TODO
		}

		return true;
	}
	
}
