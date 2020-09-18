package inner;

import java.rmi.RemoteException;

import my.utils.Consts;
import server.Main;

public class HelloElf extends Thread {
	
	public HelloElf() {
		super();
	}
	
	@Override
	public void run() {
		try {
			while(!interrupted()) {
				Thread.sleep(Consts.Y);
				synchronized(Main.ss) {
					try {
						Main.ss.csrmi.hello(Main.ss.port);
					} catch (RemoteException e) {
						Main.ss.reconnect();
						wait();
					}
				}
			}
		} catch (InterruptedException e) {
		}
	}
}
