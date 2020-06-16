package rsu.algorithms;

import java.beans.PropertyChangeListener;

import rsu.server.VehicleInIntersection;

public interface AlgorithmICWS {
	public boolean isVehicleAllowedToDrive(VehicleInIntersection cii);
	public void addPropertyChangeListener(PropertyChangeListener listener);
	public void removePropertyChangeListener(PropertyChangeListener listener);
}
