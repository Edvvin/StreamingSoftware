package client;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javafx.application.Platform;
import my.rmi.Room;
import my.utils.Consts;

public class ClientElf extends Thread {
	
	public ClientElf() {
		
	}
	
	@Override
	public void run() {
		try {
			while(!interrupted()) {
				Thread.sleep(Consts.CLIENT_ELF_CYCLE);
				try {
					ArrayList<Room> newRooms = Main.ssrmi.getRooms();
					for(Room r : newRooms) {
						if(!Main.rooms.contains(r) 
							&& (r.getBuddies().contains(Main.currentUser)
									|| r.getAdmin().equals(Main.currentUser))) {
							Main.rooms.add(r);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									Main.roomItems.add(new RoomGUI(r));
								}
							});
							if(!r.getAdmin().equals(Main.currentUser)) {
								Main.notiText.appendText("New Room: " + r.getRoomName() + "\n");
							}
						}
					}
				}
				catch(RemoteException e) {
					if(!Main.complain(false))
						interrupt();
				}
			}
		}
		catch(InterruptedException e) {
			
		}
	}
}
