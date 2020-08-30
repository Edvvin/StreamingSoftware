package my.rmi;

public class Chunk {
	byte bytes[];
	public static final int CHUNK_SIZE = 20*1024*1024;
	
	public Chunk() {
		bytes = new byte[CHUNK_SIZE];
	}

	
	public byte[] getBytes() {
		return bytes;
	}
}
