package mV2IL.LAN;

import java.io.IOException;
import java.net.Socket;
import java.util.Observer;

import mV2IL.io.InputController;
import mV2IL.io.InputControllerLAN;
import mV2IL.io.OutputController;
import mV2IL.messages.MessageDissconnect;
import mV2IL.messages.MessageJson;

public class ControllerLAN {
	private InputController inputController = null;
	private OutputController outputController = null;
	private String adressLAN = "127.0.0.1";
	private Socket socket = null;

	public void openConnection(Observer obs) throws IOException {
		if (!isConnected() && setAdressLAN(adressLAN)) {
			openConnection(new Socket(adressLAN, 6060), obs);
		}
	}
	
	public void openConnection(Socket socket, Observer obs) {
		try {
			this.socket = socket;
			outputController = new OutputController(socket.getOutputStream());
			inputController = new InputControllerLAN(socket.getInputStream());
			inputController.addObserver(obs);

			Thread output = new Thread(outputController);
			Thread input = new Thread(inputController);
			output.start();
			input.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openConnection(String adress, Observer obs) throws IOException {
		setAdressLAN(adress);
		openConnection(obs);
	}

	public String getAdressLAN() {
		return adressLAN;
	}

	public boolean setAdressLAN(String adressLAN) {
		if (adressLAN == null)
			return false;

		// TODO: TEST IF PROPER BT-adress

		this.adressLAN = adressLAN;
		return true;
	}

	public void sendMessage(MessageJson msg) {
		if (socket != null) {
			outputController.queueMessage(msg);
		}
	}

	public boolean isConnected() {
		return socket != null;

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
			socket.close();

			outputController = null;
			socket = null;
			inputController = null;
		}
	}

	public InputController getInputController() {
		return inputController;
	}

	public Socket getSocket() {
		return socket;
	}
}
