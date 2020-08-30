package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import my.rmi.*;

public class SubServerRMI implements SSRemote {
	
	public SubServerRMI() throws RemoteException {
		super();
	}

	@Override
	public String login(String username, String password) throws RemoteException {
		String ssid = "INVALID";
		try {
			ssid = InetAddress.getLocalHost().getHostAddress() + ":" + Main.ss.getPort();
			Main.ss.checkUser(username, password);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
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
		//movieElf.newMovie();
	}

	@Override
	public void upload(String name, Chunk chunk, boolean newFile) throws IOException, RemoteException {
		Main.ss.upload(name, chunk, newFile);
	}
	
	@Override
	public void uploadFinished(String name) {
		Main.ss.uploadFinished(name);
	}
	
	@Override
	public void newUser(String username, String password) {
		Main.ss.newUser(username, password);
	}
	

}
