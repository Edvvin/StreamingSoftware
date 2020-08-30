package inner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Stream;

import my.rmi.*;

public class Subserver {
	HashMap<String, Movie> movies;
	int port;
	String dir;
	
	public Subserver(int port, String dir) {
		this.port = port;
		movies = new HashMap<>();
		this.dir = dir;
		Path indexPath = Path.of(dir, "index.txt");
		try {
			BufferedReader br = new BufferedReader(new FileReader(indexPath.toFile()));
			Iterator<String> lines = br.lines().iterator();
			lines.forEachRemaining(line -> {
					String parts[] = line.split(":");
					String movieName = parts[0];
					int chunkNum = Integer.parseInt(parts[1]);
					Movie m;
					if(movies.containsKey(movieName)) {
						m = movies.get(movieName);
					}
					else {
						m = new Movie(movieName);
						movies.put(movieName, m);
					}
					m.addChunk(chunkNum);
				}
			);
			
		} catch (IOException e) {
			try {
				indexPath.toFile().createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}
	
	public boolean upload(String name, Chunk chunk, boolean newFile, boolean done) {
		Path filePath = Path.of(dir, name);
		try(FileOutputStream fs = new FileOutputStream(filePath.toFile(), newFile)) {
			fs.write(chunk.getBytes());
			if(done) {
				//TODO
			}
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public int getPort() {
		return port;
	}
}
