import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.Controller;
import rsu.server.ControllerRSU;
import rsu.view.ViewRSU;
import view.MainPanel;

public class Main {
	public static void main(String args[]) throws IOException {

		// START RSU
		ControllerRSU cRSU = new ControllerRSU();
		Thread tRSU = new Thread(cRSU);
		tRSU.start();

		// INIT CONTROLLER
		Controller controller = new Controller();

		// CREATE VIEW
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());
		frame.setTitle("mBot remote BT controller");

		JPanel mainPanel = new MainPanel(controller, frame);

		frame.add(mainPanel, BorderLayout.CENTER);
		
		

		// Create server GUI
		//JFrame frameRSU = new JFrame();
		ViewRSU v = new ViewRSU();
		frame.add(v, BorderLayout.NORTH);
		//frameRSU.setVisible(true);
		//frameRSU.add(v);
		//frameRSU.pack();
		
		cRSU.getAlgorithmICWS().addPropertyChangeListener(v);
		
		frame.pack();
		frame.setSize(480, 100);
	}
}
