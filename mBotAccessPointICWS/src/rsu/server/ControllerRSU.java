package rsu.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import mV2IL.LAN.ControllerLAN;
import mV2IL.io.InputController;
import mV2IL.io.Logger;
import mV2IL.messages.MessageRSUdriving;
import mV2IL.messages.MessageServer;
import mV2IL.messages.MessageWithOrigin;
import mV2IL.model.TraficLightStates;
import rsu.algorithms.AlgorithmICWS;
import rsu.algorithms.QueueAlg;
import rsu.algorithms.TrafficLightAlg;

public class ControllerRSU implements PropertyChangeListener, Runnable {
	private ServerSocket serverSocket = null;
	private static final int SERVER_PORT = 6060;
	private Map<InputController, ControllerLAN> connections = null;
	private boolean isRunning = false;
	private Logger logger = null;
	private AlgorithmICWS algorithmICWS = null;

	public ControllerRSU() {
		try {
			logger = new Logger("RSU");
			serverSocket = new ServerSocket(SERVER_PORT);
			connections = new HashMap<InputController, ControllerLAN>();
			isRunning = true;
			algorithmICWS = new QueueAlg();

			CommunicationTimer comTime = new CommunicationTimer();
			comTime.addPropertyChangeListener(e -> {
				for (ControllerLAN cl : connections.values()) {
					CarInIntersection cii = CarInIntersection.getCarInIntersection(cl);

					if (cii != null) {
						boolean drive = algorithmICWS.isVehicleAllowedToDrive(cii);
						cl.sendMessage(new MessageRSUdriving(drive));
					}
				}
			});

			Thread comTimeThread = new Thread(comTime);
			comTimeThread.start();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void stop() {
		try {
			for (ControllerLAN c : connections.values()) {
				c.close();
			}
			isRunning = false;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public AlgorithmICWS getAlgorithmICWS() {
		return algorithmICWS;
	}

	public void setAlgorithmICWS(AlgorithmICWS algorithmICWS) {
		this.algorithmICWS = algorithmICWS;
	}

	@Override
	public void run() {
		try {
			while (isRunning) {
				Socket clienSocket = serverSocket.accept();
				ControllerLAN newConnection = new ControllerLAN();
				newConnection.openConnection(clienSocket, this);
				connections.put(newConnection.getInputController(), newConnection);
				logger.logData("newConnection: " + clienSocket.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleJsonFromLAN(MessageWithOrigin msgOrigin) {
		JsonObject jObj = msgOrigin.message;
		GsonBuilder builder = new GsonBuilder();
		builder.setVersion(1.0);
		Gson gson = builder.create();
		MessageServer msgServer = gson.fromJson(jObj.toString(), MessageServer.class);
		ControllerLAN cl = connections.get(msgOrigin.origin);
		CarInIntersection.updateInfo(msgServer, cl);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		MessageWithOrigin msg = (MessageWithOrigin) evt.getNewValue();
		for (ControllerLAN c : connections.values()) {
			//TODO: is this really correct???
			if (c.getInputController().equals(msg.origin)) {
				handleJsonFromLAN(msg);
				break;
			}
		}
	}
}
