package rsu.algorithms;

import java.beans.PropertyChangeListener;

import rsu.server.CarInIntersection;

public interface AlgorithmICWS {
	public boolean isVehicleAllowedToDrive(CarInIntersection cii);
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
}
