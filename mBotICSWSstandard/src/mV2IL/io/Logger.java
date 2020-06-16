package mV2IL.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private String owner;
	private static File file = null;
	private static BufferedWriter bufferedWriter = null;
	private static final long SYSTEM_START_TIME = System.currentTimeMillis();
	private static final String LOCATION= "logs" + File.separator;
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss:SSS");
	private static SimpleDateFormat simpleDateFormatFileName = new SimpleDateFormat("MMM-dd-hh.mm.ss");
	
	public Logger(String owner) throws IOException {
		this.owner = owner;
		
		if (file == null) {
			String date = simpleDateFormatFileName.format(new Date());
			String fileName = "log_" + date + ".log";
			
			File directory = new File(LOCATION);
			directory.mkdirs();
			file = new File(LOCATION + fileName);
			bufferedWriter = new BufferedWriter(new FileWriter(file));
		}
	}
	
	public String logData(String str) throws IOException {				
		String date = simpleDateFormat.format(new Date(System.currentTimeMillis() - SYSTEM_START_TIME));
		String msg = date + "\t[" + owner + "]:\t" + str + "\n";
		
		System.out.print(msg);
		
		bufferedWriter.append(msg);
		bufferedWriter.flush();
		return msg;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public static void close() throws IOException {
		bufferedWriter.close();
	}
}
