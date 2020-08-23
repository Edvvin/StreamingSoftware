package server;

import javax.swing.JOptionPane;

public class Main {

	public static boolean nogui = false;
	public static Logger logger = null;
	public static void main(String[] args) {
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
			logger = new Logger(ssg.getTextArea());
		}
	}

}
