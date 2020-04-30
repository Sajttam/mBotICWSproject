package mV2IL.messages;
import mV2IL.model.TraficLightStates;

public class MessageRSUdriving implements MessageJson {
	public boolean drive = false;
	public int traficLightState = TraficLightStates.ALL_RED.getState();
	
	public MessageRSUdriving(boolean drive) {
		this.drive = drive;
	}
	
	public MessageRSUdriving(boolean drive, TraficLightStates tls) {
		this.drive = drive;
		this.traficLightState = tls.getState();
	}
	
	public MessageRSUdriving(TraficLightStates tls) {
		drive = true;
		this.traficLightState = tls.getState();
	}
}
