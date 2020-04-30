package mV2IL.io;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Observable;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import mV2IL.messages.MessageWithOrigin;

public abstract class InputController extends Observable implements Runnable {
	protected InputStream inputStream;
	protected StringBuilder stringBuilder;
	protected boolean isRunning = true;

	protected InputController(InputStream inputStream) {
		this.inputStream = inputStream;
		stringBuilder = new StringBuilder();
	}
	
	public void stop() {
		isRunning  = false;
	}
	
	public boolean equals(Object other) {
		return super.equals(other);
	}

	@Override
	public void run() {
		try {
			while (isRunning) {
				if (inputStream.available() > 0) {
					int apa = inputStream.read();
					if (apa == '}') {
						
						stringBuilder.append((char)apa);
						//System.out.println("mBot says: " + stringBuilder.toString());
						
						JsonParser p = new JsonParser();
						
						MessageWithOrigin msg = new MessageWithOrigin();
						msg.origin = this;
						msg.message = p.parse(stringBuilder.toString()).getAsJsonObject();
						
						setChanged();
						notifyObservers(msg);
						
						stringBuilder = new StringBuilder();
					} else {
						stringBuilder.append((char) apa);
					}
				}
			}
			inputStream.close();;
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
