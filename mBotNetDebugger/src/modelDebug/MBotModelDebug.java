package modelDebug;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import bot.model.MBotModel;
import mV2IL.model.PositionState;

public class MBotModelDebug extends MBotModel {

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public MBotModelDebug(String botName, String bluetoothAdress) throws IOException {
		super(botName, bluetoothAdress);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	@Override
	public void setCurrentPosition(int countedContinuousLines) {
		if (pcs != null)
			this.pcs.firePropertyChange("countedContinuousLines", getCurrentPosition(), countedContinuousLines);
		super.setCurrentPosition(countedContinuousLines);
	}

	@Override
	public void setBotName(String botName) {
		if (pcs != null)
			this.pcs.firePropertyChange("botName", getBotName(), botName);
		super.setBotName(botName);
	}

	@Override
	public void setCurrentPositionState(PositionState currentPositionState) {
		if (pcs != null)
			this.pcs.firePropertyChange("currentPositionState", getCurrentPositionState(), currentPositionState);
		super.setCurrentPositionState(currentPositionState);
	}

	@Override
	public void setPosition(int position) {
		if (pcs != null)
			this.pcs.firePropertyChange("position", getPosition(), position);
		super.setPosition(position);
	}

	@Override
	public void setRoundTripTime(int roundTripTime) {
		if (pcs != null)
			this.pcs.firePropertyChange("roundTripTime", getRoundTripTime(), roundTripTime);
		super.setRoundTripTime(roundTripTime);
	}
}
