package mV2IL.messages;

import com.google.gson.JsonObject;

import mV2IL.io.InputController;

public class MessageWithOrigin {

	public InputController origin;
	public JsonObject message;
	
	public MessageWithOrigin() {

	}
}
