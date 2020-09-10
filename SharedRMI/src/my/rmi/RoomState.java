package my.rmi;

public class RoomState {
	
	public enum State{
		PAUSED, PLAYING
	}
	
	private int time;
	private State state;
	private long lastUpdate;
	
	public RoomState(int time, State state) {
		this.time = time;
		this.state = state;
		this.lastUpdate = System.currentTimeMillis();
	}
	
	public int getTime() {
		return time;
	}
	
	public State getState() {
		return state;
	}
	
	public long getLastUpdate() {
		return lastUpdate;
	}
	
	public void setRoomState(int time, State state) {
		this.time = time;
		this.state = state;
		this.lastUpdate = System.currentTimeMillis();
	}

}
