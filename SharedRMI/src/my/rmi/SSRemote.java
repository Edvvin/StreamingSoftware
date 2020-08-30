package my.rmi;

import java.rmi.*;

public interface SSRemote extends Remote{
	public String login(String username, String password) throws RemoteException;
	public Chunk getChunk() throws RemoteException;
	public void newMovie() throws RemoteException;
	public boolean upload(String name, Chunk chunk, boolean newFile) throws RemoteException;
}
