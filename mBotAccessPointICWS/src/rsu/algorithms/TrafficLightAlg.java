package rsu.algorithms;

import java.io.IOException;

import mV2IL.model.TraficLightStates;
import rsu.server.CarInIntersection;

public class TrafficLightAlg extends AlgorithmICWSabstarct implements Runnable {
	
	public static final int LIGHT_TIME_ALL_RED = (int) (3.5 * 1000); // [ms]
	public static final int LIGHT_TIME_GREEN = 7 * 1000; // [ms]
	private boolean isRunning = true;
	private TraficLightStates currentTrafficLightState = TraficLightStates.ALL_RED;
	private final static int STOP_AT_LINE = 6;
	public final static String EVENT_NAME = "TrafficLight";
	

	public TrafficLightAlg() {
		Thread t = new Thread(this);
		t.start();
		try {
			setLogger("TrafficLight");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addPropertyChangeListener(e -> {
			try {
				getLogger().logData(e.toString());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}
	

	@Override
	public boolean isVehicleAllowedToDrive(CarInIntersection cii) {
		switch (currentTrafficLightState) {
		case ALL_RED:
			switch (cii.getPositionState()) {
			case POSITION_STATE_ENTER_EAST:
			case POSITION_STATE_ENTER_NORTH:
			case POSITION_STATE_ENTER_SOUTH:
			case POSITION_STATE_ENTER_WEST:
				if (cii.getCurrentPosition() < STOP_AT_LINE)
					return false;
				break;
			}
			break;
		case EAST_WEST_GREEN:
			switch (cii.getPositionState()) {
			case POSITION_STATE_ENTER_NORTH:
			case POSITION_STATE_ENTER_SOUTH:
				if (cii.getCurrentPosition() < STOP_AT_LINE)
					return false;
				break;
			}
			break;
		case SOUTH_NORTH_GREEN:
			switch (cii.getPositionState()) {
			case POSITION_STATE_ENTER_EAST:
			case POSITION_STATE_ENTER_WEST:
				if (cii.getCurrentPosition() < STOP_AT_LINE)
					return false;
				break;
			}
			break;
		}
		return true;
	}

	@Override
	public void run() {
		TraficLightStates oldValue = currentTrafficLightState;
		try {
			while (isRunning) {
				oldValue = currentTrafficLightState;
				setCurrentTrafficLightState(TraficLightStates.SOUTH_NORTH_GREEN);
				pcs.firePropertyChange(EVENT_NAME, oldValue, getCurrentTrafficLightState());
				
				Thread.sleep(LIGHT_TIME_GREEN);
				
				oldValue = currentTrafficLightState;
				setCurrentTrafficLightState(TraficLightStates.ALL_RED);
				pcs.firePropertyChange(EVENT_NAME, oldValue, getCurrentTrafficLightState());

				Thread.sleep(LIGHT_TIME_ALL_RED);
				
				oldValue = currentTrafficLightState;
				setCurrentTrafficLightState(TraficLightStates.EAST_WEST_GREEN);
				pcs.firePropertyChange(EVENT_NAME, oldValue, getCurrentTrafficLightState());

				Thread.sleep(LIGHT_TIME_GREEN);
				
				oldValue = currentTrafficLightState;
				setCurrentTrafficLightState(TraficLightStates.ALL_RED);
				pcs.firePropertyChange(EVENT_NAME, oldValue, getCurrentTrafficLightState());

				Thread.sleep(LIGHT_TIME_ALL_RED);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public TraficLightStates getCurrentTrafficLightState() {
		return currentTrafficLightState;
	}

	private void setCurrentTrafficLightState(TraficLightStates currentTrafficLightState) {
		this.currentTrafficLightState = currentTrafficLightState;
	}
	
	public void close() {
		isRunning = false;
	}
}
