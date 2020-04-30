package rsu.server;

import java.util.Observable;

class CommunicationTimer extends Observable implements Runnable {
	private static final int WAIT_TIME = 75; // [ms];
	private boolean isRunning = true;

	@Override
	public void run() {
		try {
			while (isRunning) {
				Thread.sleep(WAIT_TIME);
				setChanged();
				notifyObservers();
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