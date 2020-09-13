package inner;

import server.*;
import my.rmi.RoomState;
import my.utils.*;

public class RoomElf extends Thread {
	public RoomElf() {
		super();
	}

	@Override
	public void run() {
		try {
			while(!interrupted()) {
				Thread.sleep(Consts.ADMIN_TIMEOUT);
				synchronized(Main.cs) {
					Main.cs.rooms.forEach((key,value) -> {
						if(value.getState() == RoomState.State.PLAYING) {
							long lu = value.getLastUpdate();
							if(System.currentTimeMillis() - lu > Consts.ADMIN_TIMEOUT) {
								value.setRoomState(value.getTime(), RoomState.State.PAUSED);
								Main.cs.setRoomState(key, value.getTime(), RoomState.State.PAUSED);
							}
						}
					});
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
