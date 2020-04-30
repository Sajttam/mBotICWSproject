package mV2IL.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;

import com.google.gson.JsonParser;

import mV2IL.messages.MessageWithOrigin;

public class InputControllerBT extends InputController {
	private Semaphore connectionConfirmed = null;
	private boolean connectionTested = false;

	public InputControllerBT(InputStream inputStream, Semaphore connectionConfirmed) {
		super(inputStream);
		this.connectionConfirmed = connectionConfirmed;
	}

	@Override
	public void run() {
		try {
			while (isRunning) {
				if (inputStream.available() > 0) {
					int apa = inputStream.read();
					if (apa == '}') {
						stringBuilder.append((char) apa);
						JsonParser p = new JsonParser();
						MessageWithOrigin msg = new MessageWithOrigin();
						msg.origin = this;
						msg.message = p.parse(stringBuilder.toString()).getAsJsonObject();
						
						if (!connectionTested) {
							connectionTested = true;
							connectionConfirmed.release();
						} else {
							setChanged();
							notifyObservers(msg);
						}
						stringBuilder = new StringBuilder();
					} else {
						stringBuilder.append((char) apa);
						if (stringBuilder.length() > 300) {
							System.out.println("error: Message to long! clearingbuffer...");
							stringBuilder = new StringBuilder();
						}
					}
				}
			}
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
