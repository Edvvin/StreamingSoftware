package inner;

import server.Main;

public class MovieElf extends Thread {
	public MovieElf() {
		super();
	}
	
	@Override
	public void run() {
		try {
			while(!interrupted()) {
				synchronized(this) {
					//checkMovies TODO
					wait();
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
