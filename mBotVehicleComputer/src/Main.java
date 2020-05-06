import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import com.google.gson.Gson;

import bot.controller.ControllerMBot;
import bot.messages.MessageSettings;

public class Main {
	private static boolean isRunning = true;
	private static final String FILE_PATH_BOTS = "bots.dat";
	private static final String FILE_PATH_SETTINGS = "botSettings.json";
	public static final String ADRESS_PREFIX = "btspp://";
	public static final String ADRESS_POSTFIX = ":1;authenticate=false;encrypt=false;master=false";
	public static final String RSU_IP_ADRESS = "10.90.131.51";

	public static void printToConsol(String str) {
		System.out.println("$: " + str);
	}

	public static void printToConsol(Object obj) {
		printToConsol(obj.toString());
	}

	public Map<String, String> getBotsFromFile() throws IOException {
		Map<String, String> map = new TreeMap<String, String>();

		InputStream in = new FileInputStream(FILE_PATH_BOTS);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line;
		while ((line = br.readLine()) != null) {
			String[] values = line.split("\t");
			map.put(values[0], values[1]);
		}

		return map;
	}

	public static void main(String args[]) {		
		Main m = new Main();
		printToConsol("__mBot Controller Started__");

		String fileString;
		try {
			fileString = new String(Files.readAllBytes(Paths.get(FILE_PATH_SETTINGS)), StandardCharsets.UTF_8);

			Gson gson = new Gson();
			MessageSettings settings = gson.fromJson(fileString, MessageSettings.class);
			System.out.println("Contents (Java 7 with character encoding ) : " + settings.toString());
			
			ControllerMBot mBotController = new ControllerMBot(settings.getBotName(),
					ADRESS_PREFIX + settings.getAdressBT() + ADRESS_POSTFIX, settings.getAdressRSU());
			
			mBotController.setBaseMotorSpeed(settings.getBaseMotorSpeed());
			Thread.sleep(50);
			
			mBotController.setClockwise(settings.getClockwise());
			Thread.sleep(50);
			
			mBotController.connect();
			Thread.sleep(50);
			
			mBotController.setDriving(true);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * Map<String, String> map = null; try { map = m.getBotsFromFile(); } catch
		 * (IOException e1) { // TODO Auto-generated catch block e1.printStackTrace();
		 * System.exit(0); }
		 * 
		 * StringBuilder s = new StringBuilder(); List<String> list = new
		 * ArrayList(map.keySet());
		 * 
		 * int i = 0; for (String str : list) { s.append("[" + i + "]="); s.append(str +
		 * ", "); i++; } printToConsol(s);
		 * 
		 * Scanner scanner = new Scanner(System.in);
		 * 
		 * ControllerMBot mBotController = null;
		 * 
		 * while (isRunning) { System.out.print("-> "); String msg = scanner.nextLine();
		 * printToConsol(msg);
		 * 
		 * if (mBotController == null) { try { int selectionValue =
		 * Integer.parseInt(msg);
		 * 
		 * if (selectionValue < 0 || selectionValue >= list.size()) {
		 * printToConsol("error on input: " + msg); printToConsol(s); } else { try {
		 * mBotController = new ControllerMBot(list.get(selectionValue), ADRESS_PREFIX +
		 * map.get(list.get(selectionValue)) + ADRESS_POSTFIX, RSU_IP_ADRESS);
		 * mBotController.connect(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 * printToConsol("[d=drive, p=ping, ac = set Antoclocwise, c = set Clockwise, baseMotorSpeed = [value]]"
		 * ); } } catch (NumberFormatException e) { printToConsol("error on input: " +
		 * msg); printToConsol(s); } } else { switch (msg) { case "d":
		 * mBotController.toggleDriving(); break; case "p": mBotController.pingBot();
		 * break; case "ac": mBotController.setClockwise(false); break; case "c":
		 * mBotController.setClockwise(true); break; default:
		 * printToConsol("error on input: " + msg); break; } } }
		 */
	}
}
