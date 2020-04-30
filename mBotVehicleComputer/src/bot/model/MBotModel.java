package bot.model;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import bot.messages.MessageToBot;
import mV2IL.io.Logger;
import mV2IL.model.PositionState;

public class MBotModel extends Observable {

	public enum LineState {
		LINESTATE_OUTSIDE(0), LINESTATE_INSIDE(1), LINESTATE_HALFINSIDE_10(2), LINESTATE_HALFINSIDE_01(3),
		LINESTATE_HALFOUTSIDE_10(4), LINESTATE_HALFOUTSIDE_01(5), LINESTATE_OUT_OF_LINE(6);

		int state;

		LineState(int state) {
			this.state = state;
		}

		public int getState() {
			return state;
		}
	}

	private boolean isDriving = false;
	private int motorEffect = 20;
	private int position = 0;
	private int leftPower = 0;
	private int rightPower = 0;
	private int roundTripTime = 0;
	private int currentPosition = 0;
	private long timeLastCountedLine = -1;
	public static final int START_TIME_BETWEEN_LINES = 1500; //[ms]
	private int timeBetweenLines = START_TIME_BETWEEN_LINES; // [ms]

	private String bluetoothAdress = null;
	private String botName = null;

	private LineState currentLineState = LineState.LINESTATE_OUTSIDE;
	private PositionState currentPositionState = PositionState.POSITION_STATE_UNKNOWN;

	private boolean clockwise = true;
	private boolean allowDrivingRSU = true;
	private boolean allowDrivingUS = true;

	private Logger logger = new Logger("");

	public void printMessage(String msg) {
		String logMsg;
		try {
			logMsg = logger.logData(msg);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public MBotModel(String botName, String bluetoothAdress) throws IOException {
		setBluetoothAdress(bluetoothAdress);
		setBotName(botName);
	}

	public boolean isAllowedToDrive() {
		return allowDrivingRSU && allowDrivingUS;
	}

	public void setAllowDrivingRSU(boolean allowDrivingRSU) {
		if (allowDrivingRSU != this.allowDrivingRSU) {
			this.allowDrivingRSU = allowDrivingRSU;
			printMessage("allowDrivingRSU=" + allowDrivingRSU);
		}

	}

	public void setAllowDrivingUS(boolean allowDrivingUS) {
		if (allowDrivingUS != this.allowDrivingUS) {
			this.allowDrivingUS = allowDrivingUS;
			printMessage("allowDrivingUS=" + allowDrivingUS);
		}
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int countedContinuousLines) {
		if (countedContinuousLines != this.currentPosition) {
			this.currentPosition = countedContinuousLines;
		}
	}

	public long getTimeLastCountedLine() {
		return timeLastCountedLine;
	}

	public void setTimeLastCountedLine(long timeLastCountedLine) {
		this.timeLastCountedLine = timeLastCountedLine;
	}

	public int getTimeBetweenLines() {
		return timeBetweenLines;
	}

	public void setTimeBetweenLines(int timeBetweenLines) {
		this.timeBetweenLines = timeBetweenLines;
	}

	public String getBluetoothAdress() {
		return bluetoothAdress;
	}

	public void setBluetoothAdress(String bluetoothAdress) {
		this.bluetoothAdress = bluetoothAdress;
	}

	public String getBotName() {
		return botName;
	}

	public void setBotName(String botName) {
		logger.setOwner(botName);
		this.botName = botName;
	}

	public int getMotorEffect() {
		return motorEffect;
	}

	public void setMotorEffect(int motorEffect) {
		this.motorEffect = motorEffect;
	}

	public PositionState getCurrentPositionState() {
		return currentPositionState;
	}

	public void setCurrentPositionState(PositionState currentPositionState) {
		this.currentPositionState = currentPositionState;
		printMessage("currentPositionState=" + currentPositionState + ", " + "currentPosition=" + currentPosition);
	}

	public double getSpeed() {
		return 0;
	}

	public boolean isDriving() {
		return isDriving;
	}

	public void setDriving(boolean isDriving) {
		if (this.isDriving != isDriving) {
			this.isDriving = isDriving;
			printMessage("isDriving=" + isDriving);
		}
	}

	public LineState getLineState() {
		return currentLineState;
	}

	public void setLineState(LineState lineState) {
		this.currentLineState = lineState;
	}

	public void setLineState(int lineState) {
		for (LineState ls : LineState.values()) {
			if (ls.getState() == lineState)
				currentLineState = ls;
		}
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setLeftPower(int pow) {
		leftPower = pow;
	}

	public void setRightPower(int pow) {
		rightPower = pow;
	}

	public MessageToBot getAsMessage() {
		MessageToBot m = new MessageToBot();

		m.drive = isDriving;
		m.bspeed = motorEffect;
		m.corrvalue = 0;

		return m;
	}

	public int getRoundTripTime() {
		return roundTripTime;
	}

	public void setRoundTripTime(int roundTripTime) {
		this.roundTripTime = roundTripTime;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MBotModel) {
			MBotModel other = (MBotModel) obj;
			return other.bluetoothAdress.equals(bluetoothAdress);
		}
		return false;
	}

	public void reset() {
		isDriving = false;
		motorEffect = 20;
		position = 0;
		leftPower = 0;
		rightPower = 0;
		roundTripTime = 0;

		currentPosition = 0;

		timeLastCountedLine = -1;
		timeBetweenLines = START_TIME_BETWEEN_LINES; // [ms]

		currentLineState = LineState.LINESTATE_OUTSIDE;
		currentPositionState = PositionState.POSITION_STATE_UNKNOWN;
	}

	public boolean isClockwise() {
		return clockwise;
	}

	public void setClockwise(boolean clockwise) {
		this.clockwise = clockwise;
	}

	@Override
	public String toString() {
		return " [isDriving=" + isDriving + ", motorEffect=" + motorEffect + ", position=" + position
				+ ", currentPosition=" + currentPosition + ", currentLineState=" + currentLineState
				+ ", currentPositionState=" + currentPositionState + ", clockwise=" + clockwise + ", allowDrivingRSU="
				+ allowDrivingRSU + ", allowDrivingUS=" + allowDrivingUS + "]";
	}
}
