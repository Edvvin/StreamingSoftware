package my.rmi;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {
	ArrayList<Integer> chunks;
	String name;
	long size;
	long numOfChunks = 0;
	
	public Movie(String name, long size) {
		this.name = name;
		this.chunks = new ArrayList<>();
		this.size = size;
		this.numOfChunks = 0;
		if(size % Chunk.CHUNK_SIZE == 0) {
			numOfChunks = size / Chunk.CHUNK_SIZE;
		}
		else {
			numOfChunks = size / Chunk.CHUNK_SIZE + 1;
		}
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
	
	public boolean isWhole() {
		return numOfChunks == chunks.size();
	}
}
