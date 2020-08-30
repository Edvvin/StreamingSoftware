package my.rmi;

import java.io.Serializable;
import java.util.Arrays;

public class Chunk implements Serializable{
	byte bytes[];
	int index;
	public static final int CHUNK_SIZE = 4*1024*1024;
	
	public Chunk(int ind) {
		bytes = new byte[CHUNK_SIZE];
		index = ind;
	}
	
	public byte[] getBytes() {
		return bytes;
	}
	
	public void pack(int len) {
		bytes = Arrays.copyOf(bytes, len);
	}
	
	public int getIndex() {
		return index;
	}
}
