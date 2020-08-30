package server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import my.rmi.*;
import inner.Subserver;

public class CentralServerRMI implements CSRemote{

	public CentralServerRMI() throws RemoteException {
		super();
	}

	@Override
	public String login(String username, String password) throws RemoteException {
		String session = "FAILED";
		User u = new User(username, password);
		Subserver ss = Main.cs.routeUser(u);
		if(ss == null) {
			return "FAILED";
		}
		SSRemote ssrmi = ss.getRMI();
		if(ssrmi == null) {
			return "FAILED"; // TODO
		}
		try {
			session = ssrmi.login(username, password);
		}
		catch(RemoteException e) {
			Main.cs.removeSubserver(ss);
		}
		return session;
	}
	
	@Override 
	public Users connectToCS(String host, int port, ArrayList<Movie> wih) throws RemoteException {
		try {
			//TODO SS posalje sta ima
			Registry regSS = LocateRegistry.getRegistry(host,port);
			SSRemote ssrmi = (SSRemote) regSS.lookup("/ssrmi");
			Subserver ss = new Subserver(host, port, ssrmi);
			Main.cs.registerSubserver(ss, wih);
		} catch (RemoteException err) {
			System.out.print(err.getMessage());//TODO
		} catch (NotBoundException err) {
			System.out.print("asfgsdgas");//TODO
		}

		return Main.cs.getUsers();
	}
	
	@Override
	public Users getUsers() throws RemoteException{
		return Main.cs.getUsers();
	}
	
	public ArrayList<Order> getOrders(int ssport) throws RemoteException{
		try {
			return Main.cs.getOrders(RemoteServer.getClientHost(), ssport);
		} catch (ServerNotActiveException e) {
			// TODO Not sure how to deal with this
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean register(String username, String password) throws RemoteException {
		User user = new User(username, password);
		Subserver ss = Main.cs.registerUser(user);
		return ss!=null;
	}

	@Override
	public boolean newMovie(int port, String name, long numOfChunks) throws RemoteException {
		try {
			return Main.cs.newMovie(RemoteServer.getClientHost(), port, name, numOfChunks);
		} catch (ServerNotActiveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
