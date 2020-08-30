package server;

import java.rmi.server.RemoteObject;
import javax.swing.JFileChooser;

import inner.Subserver;
import my.rmi.SSRemote;

import javax.swing.*;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {

	public static boolean nogui = false;
	public static Logger logger = null;
	public static Subserver ss;
	public static void main(String[] args) {
		String dir = "";
		String host = "", port = "";
		for(int i=0; i < args.length; i++) {
			if(args[i].equals("nogui")) {
				nogui = true;
			}
			else if(args[i].equals("host")) {
				host = args[++i];
			}
			else if(args[i].equals("port")) {
				port = args[++i];
			}
			else if(args[i].equals("dir")) {
				dir = args[++i];
			}
		}
		
		if(nogui) {
			if(host.isEmpty() || port.isEmpty()) {
				System.out.print("Both host and port must be set");
				return;
			}
			logger = new Logger();
		}
		else {
			if(host.isEmpty() || port.isEmpty()) {
				if(host.isEmpty())
					host = JOptionPane.showInputDialog("Host: ");
				if(port.isEmpty())
					port = JOptionPane.showInputDialog("Port: ");
			}
			SubServerGUI ssg = new SubServerGUI();
			while(dir.isEmpty()) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int option = fileChooser.showOpenDialog(ssg);
				if(option == JFileChooser.APPROVE_OPTION){
				   File file = fileChooser.getSelectedFile();
				   dir = file.getPath();
				}
			}
			logger = new Logger(ssg.getTextArea());
		}
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		
		try {
			ServerSocket socket = new ServerSocket(0);
			int ssport = socket.getLocalPort();
			socket.close();
			//single object
			SSRemote rmi = new SubServerRMI();
			Registry registry = LocateRegistry.createRegistry(ssport);
			registry.rebind("/ssrmi", rmi);
			ss = new Subserver(ssport, dir);
			Main.logger.log("Created RMI on port: " + ssport);

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
