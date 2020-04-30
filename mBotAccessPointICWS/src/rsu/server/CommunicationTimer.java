package rsu.server;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Observable;

class CommunicationTimer implements Runnable {
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public CommunicationTimer() {
		
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	private static final int WAIT_TIME = 75; // [ms];
	private boolean isRunning = true;

	@Override
	public void run() {
		try {
			while (isRunning) {
				Thread.sleep(WAIT_TIME);
				pcs.firePropertyChange("Timer", false, true);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		isRunning = false;
	}
}