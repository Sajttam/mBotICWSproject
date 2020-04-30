package mV2IL.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mV2IL.messages.MessageJson;


public class OutputController implements Runnable {
	private Queue<MessageJson> messageQueueOutput;
	private boolean isRunning = true;
	private OutputStream outputStream;
	private Gson gson;
	private Semaphore messageSemaphore = new Semaphore(0);
	
	private boolean logging = false;
	private Logger logger = null;

	public OutputController(OutputStream outputStream) {
		this.outputStream = outputStream;
		messageQueueOutput = new ConcurrentLinkedQueue<MessageJson>();
		try {
			logger = new Logger("Output");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		GsonBuilder builder = new GsonBuilder();
		builder.setVersion(1.0);

		gson = builder.create();
	}

	public void stop() {
		isRunning = false;
	}

	synchronized public void queueMessage(MessageJson m) {		
		if (messageQueueOutput.add(m)) {
			messageSemaphore.release();
		}
	}

	public void clearBuffer() {
		messageQueueOutput.clear();
	}
	
	

	public boolean isLogging() {
		return logging;
	}

	public void setLogging(boolean logging) {
		this.logging = logging;
	}

	@Override
	public void run() {
		try {
			while (isRunning) {
				messageSemaphore.acquire();
				String jsonMessage = gson.toJson(messageQueueOutput.poll());
				
				if (logging) logger.logData(jsonMessage);
				
				for (byte b : jsonMessage.getBytes()) {
					outputStream.write(b);
				}
				outputStream.write('\n');
			}
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
