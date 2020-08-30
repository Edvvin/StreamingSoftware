package my.rmi;

public class Chunk {
	byte bytes[];
	int index;
	public static final int CHUNK_SIZE = 20*1024*1024;
	
	public Chunk(int ind) {
		bytes = new byte[CHUNK_SIZE];
		index = ind;
	}

	
	public byte[] getBytes() {
		return bytes;
	}
}
