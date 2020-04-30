package rsu.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import mV2IL.LAN.ControllerLAN;
import mV2IL.io.Logger;
import mV2IL.messages.MessageServer;
import mV2IL.model.PositionState;

public class CarInIntersection implements Comparable<Object> {
	private String name;
	private PositionState positionState;
	private int currentPosition;
	private ControllerLAN controllerLAN;
	private long infoAge;
	private static List<CarInIntersection> carsInIntersection = new LinkedList<CarInIntersection>();
	private Logger logger = null;
	
	private CarInIntersection(MessageServer msg, ControllerLAN controllerLAN) {
		try {
			logger = new Logger("RSU-" + msg.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setControllerLAN(controllerLAN);
		setName(msg.getName());
		setCurrentPosition(msg.getPos());
		setInfoAge(msg.getTimeStamp());
		setPositionState(msg.getState());
	}
	
	public static CarInIntersection getCarInIntersection(ControllerLAN controllerLAN) {
		for (CarInIntersection c : carsInIntersection) {
			if (c.getControllerLAN().equals(controllerLAN)) {
				return c;
			}
		}
		return null;
	}
	
	public static CarInIntersection updateInfo(MessageServer msg, ControllerLAN controllerLAN) {
		for (CarInIntersection c : carsInIntersection) {
			if (c.getControllerLAN().equals(controllerLAN)) {
				
				c.setName(msg.getName());
				c.setCurrentPosition(msg.getPos());
				c.setInfoAge(msg.getTimeStamp());
				c.setPositionState(msg.getState());
				return c;
			}
		}
		CarInIntersection c = new CarInIntersection(msg, controllerLAN);
		carsInIntersection.add(c);
		return c;
	}
	
	
	
	@Override
	public int compareTo(Object arg0) {
		if (arg0 instanceof CarInIntersection) {
			CarInIntersection other = (CarInIntersection) arg0;
			switch (other.getPositionState()) {
			case POSITION_STATE_ENTER_EAST:
			case POSITION_STATE_ENTER_WEST:
				if (getPositionState() == PositionState.POSITION_STATE_ENTER_EAST || getPositionState() == PositionState.POSITION_STATE_ENTER_WEST)
					return 0;
				else
					return -1;
			case POSITION_STATE_ENTER_NORTH:
			case POSITION_STATE_ENTER_SOUTH:
				if (getPositionState() == PositionState.POSITION_STATE_ENTER_NORTH || getPositionState() == PositionState.POSITION_STATE_ENTER_SOUTH)
					return 0;
				else
					return 1;
			default:
				System.out.println("[server:" + name + "]: WARNING! Car is not in intersection");
				break;
			}
		}
		return -1;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CarInIntersection) {
			CarInIntersection other = (CarInIntersection) obj;
			return (other.getName().equals(getName()) && other.getControllerLAN().equals(getControllerLAN()));
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (this.name != name)
			this.name = name;
	}

	public PositionState getPositionState() {
		return positionState;
	}

	public void setPositionState(PositionState positionState) {
		if (this.positionState != positionState) {
			this.positionState = positionState;
			try {
				logger.logData(toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ControllerLAN getControllerLAN() {
		return controllerLAN;
	}

	public void setControllerLAN(ControllerLAN controllerLAN) {
		this.controllerLAN = controllerLAN;
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		if (this.currentPosition != currentPosition)
			this.currentPosition = currentPosition;
	}

	public long getInfoAge() {
		return infoAge;
	}

	public void setInfoAge(long infoAge) {
		this.infoAge = infoAge;
	}

	@Override
	public String toString() {
		return "CarInIntersection [name=" + name + ", positionState=" + positionState + ", currentPosition="
				+ currentPosition + "]";
	}
}
