package bot.messages;

import com.google.gson.annotations.Since;

import mV2IL.messages.MessageJson;

public class MessageToBot implements MessageJson {
	@Since(1.0)
	public boolean drive = true;
	@Since(1.0)
	public int bspeed = 0;
	@Since(1.0)
	public int corrvalue = 0;
	
	public MessageToBot() {
		
	}
	/*
	public boolean getDrive() {
		return drive;
	}

	public void setDrive(boolean drive) {
		this.drive = drive;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getCorrvalue() {
		return corrvalue;
	}

	public void setCorrvalue(int corrvalue) {
		this.corrvalue = corrvalue;
	}
	*/
	
}