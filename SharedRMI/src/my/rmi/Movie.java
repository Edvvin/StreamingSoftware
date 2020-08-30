package my.rmi;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {
	ArrayList<Integer> chunks;
	String name;
	long size;
	
	public Movie(String name, long size) {
		this.name = name;
		this.chunks = new ArrayList<>();
		this.size = size;
	}
	
	public void addChunk(int chunkNum) {
		chunks.add(chunkNum);
	}
	
	public ArrayList<Integer> getChunks() {
		return chunks;
	}
	
	public String getName() {
		return name;
	}
	
	public long getSize() {
		return size;
	}
}
