package my.rmi;

import java.io.Serializable;

public class Order implements Serializable{
	String host, movie;
	int chunkIndex, port;
	
	public Order(String host, int port, String movie, int chunkIndex) {
		this.host = host;
		this.port = port;
		this.movie = movie;
		this.chunkIndex = chunkIndex;
	}
}
