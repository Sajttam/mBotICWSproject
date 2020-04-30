package mV2IL.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Semaphore;

import com.google.gson.JsonParser;

import mV2IL.messages.MessageWithOrigin;

public class InputControllerBT extends InputController {
	private Semaphore connectionConfirmed = null;
	private boolean connectionTested = false;
	private static final int MAX_MESSAGE_LENGTH = 300;

	public InputControllerBT(InputStream inputStream, Semaphore connectionConfirmed) {
		super(inputStream);
		this.connectionConfirmed = connectionConfirmed;
	}	

	@Override
	public void run() {
		try {
			MessageWithOrigin oldMsg = null;
			MessageWithOrigin msg = null;
			while (isRunning) {
				if (inputStream.available() > 0) {
					int apa = inputStream.read();
					if (apa == '}') {
						stringBuilder.append((char) apa);
										
						oldMsg = msg;
						msg = getMsgFromJSON(stringBuilder.toString());
						
						if (!connectionTested) {
							connectionTested = true;
							connectionConfirmed.release();
						} else {
							pcs.firePropertyChange("NewMsg", oldMsg, msg);
						}
						
						stringBuilder.setLength(0);						
					} else {
						stringBuilder.append((char) apa);
						if (stringBuilder.length() > MAX_MESSAGE_LENGTH) {
							System.out.println("error: Message to long! clearingbuffer... MAX_MESSAGE_LENGTH=" + MAX_MESSAGE_LENGTH);
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
