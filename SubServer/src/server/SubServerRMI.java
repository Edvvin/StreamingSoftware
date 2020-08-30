package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import my.rmi.*;

public class SubServerRMI extends UnicastRemoteObject implements SSRemote {
	
	public SubServerRMI() throws RemoteException {
		super();
	}

	@Override
	public String login(String username, String password) throws RemoteException {
		String ssid = "INVALID";
		try {
			ssid = RemoteServer.getClientHost() + ":" + Main.ss.getPort();
		} catch (ServerNotActiveException e) {
			e.printStackTrace();
		}
		return ssid;
	}

	@Override
	public Chunk getChunk() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void newMovie() throws RemoteException {
		movieElf.newMovie();
	}

	@Override
	public boolean upload(String name, Chunk chunk, boolean newFile, boolean done) throws RemoteException {
		Main.ss.upload(name, chunk, newFile, done);
		return true;
	}
	

}
