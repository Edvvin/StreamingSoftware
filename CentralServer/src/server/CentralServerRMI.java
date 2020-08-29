package server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import my.rmi.*;
import inner.Subserver;

public class CentralServerRMI extends UnicastRemoteObject implements CSRemote{

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
	public boolean connectToCS(String host, int port) throws RemoteException {
		try {
			Registry regSS = LocateRegistry.getRegistry(host,port);
			SSRemote ssrmi = (SSRemote) regSS.lookup("/ssrmi");
			Subserver ss = new Subserver(host, port, ssrmi);
			Main.cs.registerSubserver(ss);
		} catch (RemoteException err) {
			System.out.print(err.getMessage());//TODO
		} catch (NotBoundException err) {
			System.out.print("asfgsdgas");//TODO
		}

		return true;
	}
	
	@Override
	public Users getUsers() throws RemoteException{
		return Main.cs.getUsers();
	}
	
	//TODO
	//public ArrayList<String> getShoppingList() throws RemoteException{
		//return null;
	//}

	@Override
	public boolean register(String username, String password) throws RemoteException {
		User user = new User(username, password);
		Subserver ss = Main.cs.registerUser(user);
		return ss==null;
	}

	@Override
	public void newMovie() throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
}
