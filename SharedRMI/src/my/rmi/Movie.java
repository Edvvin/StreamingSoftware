package my.rmi;

import java.util.ArrayList;

import sun.tools.tree.ArrayAccessExpression;

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
