package bot.controller;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import com.google.gson.JsonObject;
import bot.messages.MessagePing;
import bot.messages.MessageSetPosition;
import bot.model.MBotModel;
import mV2IL.LAN.ControllerLAN;
import mV2IL.io.BluetoothController;
import mV2IL.messages.MessageServer;
import mV2IL.messages.MessageWithOrigin;
import mV2IL.model.PositionState;

public class ControllerMBot implements Runnable {
	private MBotModel mBotModel = null;
	private String rsuIpAdress = "127.0.0.1";

	private BluetoothController bluetoothController = null;
	private ControllerLAN controllerLAN = null;

	public ControllerMBot(String name, String bluetoothAdress, String rsuIpAdress) throws IOException {
		bluetoothController = new BluetoothController();
		this.rsuIpAdress = rsuIpAdress;
		controllerLAN = new ControllerLAN();
		mBotModel = new MBotModel(name, bluetoothAdress);
	}

	public ControllerMBot(MBotModel mBotModel) throws IOException {
		bluetoothController = new BluetoothController();

		controllerLAN = new ControllerLAN();
		this.mBotModel = mBotModel;
	}

	public boolean isConnectToBluetooth() {
		return bluetoothController.isConnected();
	}

	public MBotModel getmBotModel() {
		return mBotModel;
	}

	public void connect() throws IOException, InterruptedException {
		mBotModel.printMessage("Trying to connect to " + mBotModel.getBluetoothAdress() + "...");
		bluetoothController.openConnection(mBotModel.getBluetoothAdress(), e -> {
			handleJSONfromBot(((MessageWithOrigin) e.getNewValue()).message);
		});

		Thread.sleep(2000);
		bluetoothController.confirmConnection();
		bluetoothController.logOutputData(true);

		mBotModel.printMessage("Trying to connect to RSU " + rsuIpAdress + "...");
		boolean connectToLAN = true;
		while (connectToLAN) {
			try {
				controllerLAN.openConnection(rsuIpAdress, e -> {
					handleJSONfromLAN(((MessageWithOrigin) e.getNewValue()).message);
				});
				connectToLAN = false;
			} catch (IOException e) {
				mBotModel.printMessage("Found no RSU... trying again..");
				Thread.sleep(1000);
			}
		}

		TimerTask task = new TimerTask() {
			public void run() {
				controllerLAN.sendMessage(new MessageServer(mBotModel.getCurrentPosition(),
						mBotModel.getCurrentPositionState(), mBotModel.getBotName()));
			}
		};
		Timer t = new Timer();
		t.schedule(task, 75, 75);

		mBotModel.printMessage("Connection established");
	}
	
	public void disconnect() throws IOException {
		mBotModel.printMessage("Disconnecting...");
		mBotModel.reset();
		bluetoothController.close();
		mBotModel.printMessage("... disconnected");
	}

	public void pingBot() {
		bluetoothController.sendMessage(new MessagePing((int) (Math.random() * 50)));
	}

	public void resetPosition() {
		mBotModel.setPosition(0);
		bluetoothController.sendMessage(new MessageSetPosition());
	}

	/**
	 * Check if time between lines is more than the accepted time between lines in
	 * order to calculate a continues position state.
	 * 
	 * @param currentTime
	 * @return
	 */
	public boolean positionStable(long currentTime) {
		return (currentTime - mBotModel.getTimeLastCountedLine()) > mBotModel.getTimeBetweenLines();
	}
	
	int intersectionPassingNum = 0;
	public void switchPositionsClockwise(int count) {
		switch (mBotModel.getCurrentPositionState()) {
		case POSITION_STATE_ENTER_WEST:
			mBotModel.setCurrentPositionState(PositionState.POSITION_STATE_EXIT_EAST);
			break;
		case POSITION_STATE_ENTER_SOUTH:
			mBotModel.setCurrentPositionState(PositionState.POSITION_STATE_EXIT_NORTH);
			break;
		case POSITION_STATE_UNKNOWN:
		case POSITION_STATE_EXIT_EAST:
			if (count >= 6) { // AT FOUR LINE
				mBotModel.setCurrentPositionState(PositionState.POSITION_STATE_ENTER_SOUTH);
				mBotModel.printMessage("intersectionPassingNum: " + ++intersectionPassingNum);
			}
		case POSITION_STATE_EXIT_NORTH:
			if (count > 1 && count < 6) { // AT TWO LINE
				mBotModel.setCurrentPositionState(PositionState.POSITION_STATE_ENTER_WEST);
				mBotModel.printMessage("intersectionPassingNum: " + ++intersectionPassingNum);
			}
			break;
		default:
			break;
		}
	}

	public void switchPositionsAnticlockwise(int count) {
		switch (mBotModel.getCurrentPositionState()) {

		case POSITION_STATE_ENTER_NORTH:
			mBotModel.setCurrentPositionState(PositionState.POSITION_STATE_EXIT_SOUTH);
			break;
		case POSITION_STATE_ENTER_EAST:
			mBotModel.setCurrentPositionState(PositionState.POSITION_STATE_EXIT_WEST);
			break;
		case POSITION_STATE_UNKNOWN:
		case POSITION_STATE_EXIT_WEST:
			if (count >= 6) { // AT FOUR LINE
				mBotModel.setCurrentPositionState(PositionState.POSITION_STATE_ENTER_NORTH);
				mBotModel.printMessage("intersectionPassingNum: " + ++intersectionPassingNum);
			}
		case POSITION_STATE_EXIT_SOUTH:
			if (count > 1 && count < 6) { // AT TWO LINE
				mBotModel.setCurrentPositionState(PositionState.POSITION_STATE_ENTER_EAST);
				mBotModel.printMessage("intersectionPassingNum: " + ++intersectionPassingNum);
			}
			break;
		default:
			break;
		}
	}

	private void updatePosition(int position) throws InterruptedException {
		if (mBotModel.getPosition() != position) {

			long currentTime = System.currentTimeMillis();
			if (positionStable(currentTime)) { // if position has not changed
				int count = mBotModel.getCurrentPosition();
				if (mBotModel.isClockwise())
					switchPositionsClockwise(count);
				else
					switchPositionsAnticlockwise(count);
				mBotModel.setCurrentPosition(1);
			} else {
				mBotModel.setCurrentPosition(mBotModel.getCurrentPosition() + (position - mBotModel.getPosition()));
			}

			mBotModel.setTimeLastCountedLine(currentTime);
			mBotModel.setPosition(position);

			MessageServer msgServer = new MessageServer(mBotModel.getCurrentPosition(),
					mBotModel.getCurrentPositionState(), mBotModel.getBotName());
			controllerLAN.sendMessage(msgServer);
		}
	}

	private void updateState(int lineState) {
		mBotModel.setLineState(lineState);
	}

	private void handleJSONfromBot(JsonObject jObj) {
		for (String key : jObj.keySet()) {
			switch (key) {
			case "timeStamp":
				long currentTime = System.currentTimeMillis();
				long pastTime = jObj.get(key).getAsLong();
				long elapsedTime = currentTime - pastTime;
				mBotModel.setRoundTripTime((int) elapsedTime);
				break;
			case "state":
				updateState(jObj.get(key).getAsInt());
				break;
			case "pos":
				try {
					updatePosition(jObj.get(key).getAsInt());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case "lp":
				mBotModel.setLeftPower(jObj.get(key).getAsInt());
				break;
			case "rp":
				mBotModel.setRightPower(jObj.get(key).getAsInt());
				break;
			case "bspeed":
				if (jObj.get(key).getAsInt() != mBotModel.getMotorEffect())
					mBotModel.printMessage("WARNING! Base speed doesn't match!");
				break;

			case "ping":
				// System.out.println(arg1);
				break;
			case "driving":
				boolean botDriving = jObj.get(key).getAsBoolean();
				if (botDriving != mBotModel.isDriving()) {
					mBotModel.printMessage("WARNING! arduino and RPi drivng values unsynced. " + "botDriving="
							+ botDriving + ", mBotModel.isDriving()=" + mBotModel.isDriving());
					setDriving(!botDriving);
				}
				break;
			case "dist":

				int distance = jObj.get(key).getAsInt();
				if (mBotModel.isDriving()) {
					if (distance <= 10) {
						mBotModel.setAllowDrivingUS(false);
						setDriving(false);
					}
				} else {
					if (distance > 15) {
						mBotModel.setAllowDrivingUS(true);
						setDriving(true);
					}
				}
				break;
			default:
				mBotModel.printMessage("Recived unknown message on BT");
				break;
			}
		}
	}

	private void handleJSONfromLAN(JsonObject jObj) {
		for (String key : jObj.keySet()) {
			switch (key) {
			case "drive":
				boolean drive = jObj.get(key).getAsBoolean(); setDriving(drive);
				setDrivingRSU(drive);
				break;
			case "traficLightState":
				break;
			}
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ControllerMBot) {
			ControllerMBot other = (ControllerMBot) obj;
			return other.mBotModel.equals(mBotModel);
		}
		return false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public void setBaseMotorSpeed(int value) {
		if (mBotModel != null) {
			mBotModel.setMotorEffect(value);
		}
	}

	public void setDrivingRSU(boolean driving) {
		mBotModel.setAllowDrivingRSU(driving);
		setDriving(driving);
	}

	long saveTime = 0;

	public void setDriving(boolean driving) {
		boolean oldDriving = mBotModel.isDriving();
		if (oldDriving != driving && oldDriving != mBotModel.isAllowedToDrive()) {
			if (mBotModel.isAllowedToDrive() && driving) {
				mBotModel.setDriving(true);
				mBotModel.setTimeLastCountedLine(System.currentTimeMillis() - saveTime);
			} else {
				saveTime = System.currentTimeMillis() - mBotModel.getTimeLastCountedLine();
				mBotModel.setDriving(false);
			}

			bluetoothController.sendMessage(mBotModel.getAsMessage());
		}
	}

	public void setClockwise(boolean b) {
		mBotModel.setClockwise(b);
	}
}
