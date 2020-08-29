package my.rmi;

import java.rmi.*;

public interface SSRemote extends Remote{
	public String login(String username, String password) throws RemoteException;
	public Chunk getChunk() throws RemoteException;
	public void movieUpdate() throws RemoteException;
	public void upload() throws RemoteException;
}
