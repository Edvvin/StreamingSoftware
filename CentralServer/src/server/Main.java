package server;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.swing.*;

public class Main {

	public static final int CS_PORT = 4000;
	public static boolean nogui;
	public static Logger logger;
	
	public static HashMap<String,SSRemote> ssMap = new HashMap<String,SSRemote>();
	public static HashMap<String,String> userMap = new HashMap<String,String>();

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

			Registry registry = LocateRegistry.createRegistry(CS_PORT);
			registry.rebind("/csrmi", rmi);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

}
