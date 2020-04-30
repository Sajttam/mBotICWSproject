package mV2IL.io;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Observer;
import java.util.concurrent.Semaphore;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import mV2IL.io.InputController;
import mV2IL.io.InputControllerBT;
import mV2IL.io.OutputController;
import mV2IL.messages.MessageDissconnect;
import mV2IL.messages.MessageJson;
import mV2IL.messages.MsgConfirmBtCon;


public class BluetoothController {
	private InputController inputController = null;
	private OutputController outputController = null;
	private StreamConnection streamConnection = null;
	private String adressBT = null;
	private Semaphore connectionConfirmed = new Semaphore(0); 

	public String getAdressBT() {
		return adressBT;
	}

	public boolean setAdressBT(String adressBT) {
		if (adressBT == null) return false;
		
		//TODO: TEST IF PROPER BT-adress
		
		this.adressBT = adressBT;
		return true;
	}

	public void sendMessage(MessageJson msg) {
		if (streamConnection != null) {
			outputController.queueMessage(msg);
		}
	}

	public void openConnection(PropertyChangeListener pcl) throws IOException {
		if (!isConnected() && setAdressBT(adressBT)) {
			streamConnection = (StreamConnection) Connector.open(adressBT);

			outputController = new OutputController(streamConnection.openOutputStream());
			inputController = new InputControllerBT(streamConnection.openInputStream(), connectionConfirmed);
			inputController.addPropertyChangeListener(pcl);

			Thread output = new Thread(outputController);
			Thread input = new Thread(inputController);
			output.start();
			input.start();
		}
	}
	
	public void openConnection(String adress, PropertyChangeListener pcl) throws IOException {
		setAdressBT(adress);
		openConnection(pcl);
	}

	public InputController getInputController() {
		return inputController;
	}

	public boolean isConnected() {
		return streamConnection != null;

	}
	
	public void confirmConnection() throws InterruptedException {
		sendMessage(new MsgConfirmBtCon());
		connectionConfirmed.acquire();
	}
	
	public void logOutputData(boolean isLogging) {
		outputController.setLogging(isLogging);
	}

	public void close() throws IOException {
		if (isConnected()) {
			MessageDissconnect msg = new MessageDissconnect();
			msg.exit = true;
			sendMessage(msg);

			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			outputController.stop();
			inputController.stop();
			streamConnection.close();

			streamConnection = null;
			outputController = null;
			inputController = null;
		}
	}
}
