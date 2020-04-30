import java.io.IOException;
import javax.swing.JFrame;

import rsu.server.ControllerRSU;
import rsu.view.ViewRSU;

public class Main {
	public static void main(String args[]) throws IOException {
		
		
		//START RSU 
		ControllerRSU cRSU = new ControllerRSU();
		Thread tRSU = new Thread(cRSU);
		tRSU.start();
		
		//CREATE VIEW
		
		//Create server GUI
		JFrame frameRSU = new JFrame();
		ViewRSU v = new ViewRSU();
		frameRSU.setVisible(true);
		frameRSU.add(v);
		frameRSU.pack();
	}
}
