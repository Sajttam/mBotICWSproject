package bot.messages;

import bot.model.MBotModel.LineState;
import mV2IL.messages.MessageJson;

public class MessageFromBot implements MessageJson {

	private int messageId;
	private int pos;
	private LineState state;
	private long timeStamp;

	public MessageFromBot(int messageId, int pos, int state) {
		this.messageId = messageId;
		this.pos = pos;
		this.state = null;

		for (LineState s : LineState.values()) {
			if (s.getState() == state) {
				this.state = s;
				break;
			}
		}

		timeStamp = System.nanoTime();
	}

	public int getMessageId() {
		return messageId;
	}

	public int getPos() {
		return pos;
	}

	public LineState getState() {
		return state;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();

		s.append("messageId:" + messageId);
		s.append(" pos:" + pos);
		s.append(" state:" + state);
		s.append(" timeStamp:" + timeStamp);

		return s.toString();
	}
}
