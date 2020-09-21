package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import my.utils.*;
import inner.CentralServer;
import inner.Subserver;
import my.rmi.CSRemote;
import my.rmi.Users;

public class Main {

	public static boolean nogui;
	public static Logger logger;
	public static CentralServer cs;
	public static CentralServerRMI myRMI;
	

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
			ServerSocket ss = new ServerSocket(58278);
			int port = ss.getLocalPort();
			ss.close();
			myRMI = new CentralServerRMI();
			CSRemote rmi = (CSRemote) UnicastRemoteObject.exportObject(myRMI, 0);
			Registry registry = LocateRegistry.createRegistry(port);
			registry.rebind("/csrmi", rmi);
			cs = new CentralServer(port);
			logger.log("Created regsitry on port: " + port);
		} catch (RemoteException e) {
			e.printStackTrace();
			//TODO
		}
		catch(IOException e) {
			
		}
		
	}

}
