package my.rmi;

import java.io.Serializable;

public class Order implements Serializable{
	String host, movie;
	int chunkIndex, port;
	long fileSize;
	
	public Order(String host, int port, String movie, int chunkIndex, long fileSize) {
		this.host = host;
		this.port = port;
		this.movie = movie;
		this.chunkIndex = chunkIndex;
		this.fileSize = fileSize;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getMovie() {
		return movie;
	}
	
	public int getPort() {
		return port;
	}

	public int getIndex() {
		return chunkIndex;
	}
	
	public long getFileSize() {
		return fileSize;
	}
}
