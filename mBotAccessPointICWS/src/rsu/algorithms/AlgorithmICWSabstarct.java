package rsu.algorithms;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import mV2IL.io.Logger;

public abstract class AlgorithmICWSabstarct implements AlgorithmICWS {
	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private Logger logger = null;

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	@Override
	public String toString() {
		return "AlgorithmICWSabstarct [pcs=" + pcs + "]";
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(String owner) throws IOException {
		if (logger == null)
			logger = new Logger(owner);
		else
			logger.setOwner(owner);
	}
}
