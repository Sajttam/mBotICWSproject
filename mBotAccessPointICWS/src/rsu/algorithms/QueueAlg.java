package rsu.algorithms;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import mV2IL.model.TraficLightStates;
import rsu.server.VehicleInIntersection;

public class QueueAlg extends AlgorithmICWSabstarct {
	private final Queue<VehicleInIntersection> QUEUE_NORTH_SOUTH = new LinkedList<VehicleInIntersection>();
	private final Queue<VehicleInIntersection> QUEUE_EAST_WEST = new LinkedList<VehicleInIntersection>();
	private TraficLightStates currentTrafficLightState = TraficLightStates.EAST_WEST_GREEN;
	private final static int STOP_AT_LINE = 6;
	public final static String EVENT_NAME = "QueueChanged";
	private final static long TRAFFIC_SWITCH_DELAY = 750;

	public QueueAlg() {
		try {
			setLogger("QueueAlg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addPropertyChangeListener(e -> {
			try {
				if (e.getPropertyName().contentEquals(EVENT_NAME)) {
					getLogger().logData("QUEUE_NORTH_SOUTH: " + QUEUE_NORTH_SOUTH.toString());
					getLogger().logData("QUEUE_EAST_WEST: " + QUEUE_EAST_WEST.toString());
				} else {
					getLogger().logData("currentTrafficLightState: " + currentTrafficLightState);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}

	public void testQueues(VehicleInIntersection cii) {
		if (QUEUE_NORTH_SOUTH.contains(cii) && QUEUE_EAST_WEST.contains(cii))
			try {
				getLogger().logData("ERROR: " + cii.getName() + " in multiple queues!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public boolean queueVehicle(Queue<VehicleInIntersection> queue, VehicleInIntersection cii) {
		if (!queue.contains(cii) && cii.getCurrentPosition() < 22) {
			queue.add(cii);
			testQueues(cii);
			pcs.firePropertyChange(EVENT_NAME, null, cii);
		} else if (queue.contains(cii) && cii.getCurrentPosition() >= 22) {
			queue.remove(cii);
			pcs.firePropertyChange(EVENT_NAME, cii, null);
		}
		return queue.contains(cii);
	}
	
	public boolean trafficLight(VehicleInIntersection cii) {
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
	public boolean isVehicleAllowedToDrive(VehicleInIntersection cii) {
		
		switch (cii.getPositionState()) {
		case POSITION_STATE_ENTER_NORTH:
		case POSITION_STATE_ENTER_SOUTH:
			queueVehicle(QUEUE_NORTH_SOUTH, cii);
			//OM ANDRA KÖN ÄR TOM, GE KRÖNT LJUS
			if (QUEUE_EAST_WEST.isEmpty())
				currentTrafficLightState = TraficLightStates.SOUTH_NORTH_GREEN;
			break;
		case POSITION_STATE_ENTER_EAST:
		case POSITION_STATE_ENTER_WEST:
			queueVehicle(QUEUE_EAST_WEST, cii);
			//OM ANDRA KÖN ÄR TOM, GE KRÖNT LJUS
			if (QUEUE_NORTH_SOUTH.isEmpty())
				currentTrafficLightState = TraficLightStates.EAST_WEST_GREEN;
			break;	
		default:
			QUEUE_NORTH_SOUTH.remove(cii);
			QUEUE_EAST_WEST.remove(cii);
			break;
		}		
		
		return trafficLight(cii);
	}

	public TraficLightStates getCurrentTrafficLightState() {
		return currentTrafficLightState;
	}

	public void setCurrentTrafficLightState(TraficLightStates currentTrafficLightState) {
		this.currentTrafficLightState = currentTrafficLightState;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
}
