import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.gson.Gson;

import bot.messages.MessageSettings;
import controller.BotJSON;
import controller.Controller;
import rsu.server.ControllerRSU;
import rsu.view.ViewRSU;
import view.MainPanel;

public class Main {
	private static final String FILE_PATH_SETTINGS = "botsInitSettings.json";

	public static void main(String args[]) throws IOException {
		try {
			// READ INIT FILE
			String fileString = new String(Files.readAllBytes(Paths.get(FILE_PATH_SETTINGS)), StandardCharsets.UTF_8);
			System.out.println(fileString);
			Gson gson = new Gson();
			BotJSON[] bots = gson.fromJson(fileString, BotJSON[].class);

			// START RSU
			ControllerRSU cRSU = new ControllerRSU();
			Thread tRSU = new Thread(cRSU);
			tRSU.start();

			// INIT CONTROLLER
			Controller controller = new Controller(bots);

			// CREATE VIEW
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setLayout(new BorderLayout());
			frame.setTitle("mBot net debugger");

			JPanel mainPanel = new MainPanel(controller, frame);

			frame.add(mainPanel, BorderLayout.CENTER);

			ViewRSU v = new ViewRSU();
			frame.add(v, BorderLayout.NORTH);

			cRSU.getAlgorithmICWS().addPropertyChangeListener(v);

			frame.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
