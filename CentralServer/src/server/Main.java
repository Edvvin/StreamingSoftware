package server;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.*;

public class Main {

	public static boolean nogui;
	public static Logger logger;

	public static void main(String[] args) {
		boolean nogui = false;
		switch(args.length) {
		case 0:
			break;
		case 1:
			if(args[0].equals("nogui"))
				nogui = true;
			break;
		default:
			System.out.println("Too many arguments");
			break;
		}

		if(nogui) {
			logger = new Logger();
		}
		else {
			CentralServerGUI csg = new CentralServerGUI();
			logger = new Logger(csg.getTextArea());
		}
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try {

			//single object
			CSRemote rmi = new CentralServerRMI();
			CSRemote stub = (CSRemote) UnicastRemoteObject.exportObject(rmi, 0);

			Registry registry = LocateRegistry.createRegistry(4001);
			registry.rebind("/csrmi", stub);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

}
