package my.rmi;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {
	ArrayList<Integer> chunks;
	String name;
	
	public Movie(String name) {
		this.name = name;
		this.chunks = new ArrayList<>();
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
}
