package bot.messages;

import mV2IL.messages.MessageJson;

public class MessageSettings implements MessageJson {
	private String adressRSU;
	private String adressBT;
	private String botName;
	private Boolean clockwise;
	private int baseMotorSpeed;

	public String getAdressRSU() {
		return adressRSU;
	}

	public String getAdressBT() {
		return adressBT;
	}

	public String getBotName() {
		return botName;
	}

	public Boolean getClockwise() {
		return clockwise;
	}

	public int getBaseMotorSpeed() {
		return baseMotorSpeed;
	}

	@Override
	public String toString() {
		return "MessageSettings [adressRSU=" + adressRSU + ", adressBT=" + adressBT + ", botName=" + botName
				+ ", clockwise=" + clockwise + ", baseMotorSpeed=" + baseMotorSpeed + "]";
	}
}
