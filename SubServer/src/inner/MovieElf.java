package inner;

import java.rmi.RemoteException;
import java.util.ArrayList;

import my.rmi.*;
import server.Main;

public class MovieElf extends Thread {
	public MovieElf() {
		super();
	}
	
	@Override
	public void run() {
		try {
			while(!interrupted()) {
				ArrayList<Order> orders;
				synchronized(this) {
					while(true) {
						orders =
								Main.ss.csrmi.getOrders(Main.ss.port);
						if(orders.size() > 0)
							break;
						wait();
					}
				}
				ArrayList<Order> success = new ArrayList<>();
				for(Order o: orders) {
					boolean reply = Main.ss.carryOut(o);
					if(reply)
						success.add(o);
				}
				Main.ss.csrmi.registerOrders(Main.ss.port, success);
			}
		}
		catch(InterruptedException e) {
		} catch (RemoteException e) {
			// TODO neka konekcija nesto
			e.printStackTrace();
		}
	}
	
	public synchronized void newMovie() {
		notify();
	}
}
