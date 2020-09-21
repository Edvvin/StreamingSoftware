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
						try{
							orders =
									Main.ss.csrmi.getOrders(Main.ss.port);
							if(orders.size() > 0)
								break;
							else
								wait();
						}
						catch(NotSycnhedException | RemoteException e) {
							Main.ss.reconnect();
							wait();
						}
					}
				}
				ArrayList<Order> success = new ArrayList<>();
				for(Order o: orders) {
					boolean reply = Main.ss.carryOut(o);
					if(reply) {
						success.add(o);
						if(!Main.ss.movies.containsKey(o.getMovie()))
							Main.ss.movies.put(o.getMovie(), new Movie(o.getMovie(), o.getFileSize()));
						Main.ss.movies.get(o.getMovie()).addChunk(o.getIndex());
					}
				}
				try {
					Main.ss.csrmi.registerOrders(Main.ss.port, success);
				}
				catch(NotSycnhedException | RemoteException e) {
					Main.ss.reconnect();
				}
			}
		}
		catch(InterruptedException e) {
		}
	}
	
	public synchronized void newMovie() {
		notify();
	}
}
