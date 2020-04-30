package bot.messages;

import mV2IL.messages.MessageJson;

public class MessagePing implements MessageJson {
	private String ping;
	private long timeStamp = 0;
	
	public MessagePing(int length) {
		int count = length;
		if (count > 75) count = 75;
		if (count < 1) count = 1;
		String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ012345678912345";
		StringBuilder builder = new StringBuilder();
		builder.append(ALPHA_NUMERIC_STRING, 0, count-1);
		ping = builder.toString();
		timeStamp = System.currentTimeMillis();		
	}
}