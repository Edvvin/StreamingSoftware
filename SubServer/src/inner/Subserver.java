package inner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
	
	public Subserver(int port, String dir) {
		movies = new HashMap<>();
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
				new FileWriter(indexPath.toFile()).close();
			} catch (IOException e1) {
				System.out.print(e1.getLocalizedMessage());
			}
		}

	}
}
