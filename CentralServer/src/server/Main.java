package server;
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
			Remote x = new Remote();
			Remote stub = (Remote) UnicastRemoteObject.exportObject(x, 0);

			Registry registry = LocateRegistry.createRegistry(4001);
			registry.rebind("/x", stub);

		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}

}
