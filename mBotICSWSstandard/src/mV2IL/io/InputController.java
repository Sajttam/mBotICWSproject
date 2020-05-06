package mV2IL.io;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.InputStream;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import mV2IL.messages.MessageWithOrigin;

public abstract class InputController implements Runnable {
	protected InputStream inputStream;
	protected StringBuilder stringBuilder;
	protected boolean isRunning = true;
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	protected InputController(InputStream inputStream) {
		this.inputStream = inputStream;
		stringBuilder = new StringBuilder();
	}
	
	public void stop() {
		isRunning  = false;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}
	
	public boolean equals(Object other) {
		return super.equals(other);
	}
	
	protected MessageWithOrigin getMsgFromJSON(String json) {
		MessageWithOrigin msg = new MessageWithOrigin();
		msg = new MessageWithOrigin();
		msg.origin = this;
		try {
		msg.message = JsonParser.parseString(json).getAsJsonObject();		
		}
		catch (Exception e) {
			System.out.println(e);
		}
		return msg;		
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
						stringBuilder.append((char)apa);
										
						oldMsg = msg;	
						msg = getMsgFromJSON(stringBuilder.toString());
						pcs.firePropertyChange("NewMsg", oldMsg, msg);
						
						stringBuilder.setLength(0);
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
