package my.rmi;

import java.io.Serializable;

public class RoomState implements Serializable{
	
	public enum State{
		PAUSED, PLAYING
	}
	
	private double time;
	private State state;
	private long lastUpdate;
	
	public RoomState(double time, State state) {
		this.time = time;
		this.state = state;
		this.lastUpdate = System.currentTimeMillis();
	}
	
	public double getTime() {
		return time;
	}
	
	public State getState() {
		return state;
	}
	
	public long getLastUpdate() {
		return lastUpdate;
	}
	
	public void setRoomState(double time, State state) {
		this.time = time;
		this.state = state;
		this.lastUpdate = System.currentTimeMillis();
	}

}
