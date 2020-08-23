package server;
import javax.swing.*;

public class Main {

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

	}

}
