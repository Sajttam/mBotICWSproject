package mV2IL.messages;
import mV2IL.model.PositionState;

public class MessageServer implements MessageJson {
	private int pos;
	private int state;
	private long timeStamp;
	private String name;
	
	public MessageServer(int pos, PositionState state, String name) {
		setPos(pos);
		setState(state);
		setName(name);
		timeStamp = System.currentTimeMillis();
	}
	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public PositionState getState() {
		for (PositionState pos : PositionState.values()) {
			if (pos.getState() == state) return pos;
		}
		return null;
	}

	public void setState(PositionState state) {
		this.state = state.getState();
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("pos:" + pos);
		s.append(", state:" +  getState());
		s.append(", timeStamp:" + timeStamp + " [ms]");
		return s.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
