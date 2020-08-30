package my.rmi;

import java.util.ArrayList;

public class Movie {
	ArrayList<Integer> chunks;
	String name;
	
	public Movie(String name) {
		this.name = name;
		this.chunks = new ArrayList<>();
	}
	
	public void addChunk(int chunkNum) {
		chunks.add(chunkNum);
	}
}
