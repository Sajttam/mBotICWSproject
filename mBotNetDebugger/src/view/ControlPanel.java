package view;

import java.awt.FlowLayout;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import bot.controller.ControllerMBot;

public class ControlPanel extends JPanel {
	
	private final static String STRING_CLOCKWISE = "Clockwise";
	private final static String STRING_ANTICLOCKWISE = "Anticlockwise";
	
	JButton connectButton = new JButton("Connect");
	JButton disconnectButton = new JButton("Disonnect");
	JButton driveButton = new JButton("Drive");
	JButton pingButton = new JButton("Ping");
	JButton powerButton = new JButton("Motor Power: 20");
	JButton clockwiseButton = new JButton(STRING_CLOCKWISE);
	
	JTextField baseMotorSpeed = new JTextField(3);
	
	public ControlPanel(ControllerMBot mBotController) {
		setLayout(new FlowLayout());
		JSlider slider = new JSlider(0, 200, 100);
		//slider.addChangeListener(e -> controller.setTurnValue(slider.getValue()));
		
		setDissabled();		
		
		driveButton.addActionListener(e -> {
			int speed = 20;
			
			try {
				speed = Integer.parseInt(baseMotorSpeed.getText());
				if (speed < 0) {
					speed = 0;
					baseMotorSpeed.setText("0");
				}
				if (speed > 100) {
					speed = 100;
					baseMotorSpeed.setText("100");
				}
			}
			catch (NumberFormatException exc) {
				baseMotorSpeed.setText("20");
			}
			
			mBotController.setBaseMotorSpeed(speed);
			mBotController.toggleDriving();
		});		
		pingButton.addActionListener(e -> mBotController.pingBot());
		
		disconnectButton.addActionListener(e -> {
			try {
				mBotController.disconnect();
				setDissabled();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});
		
		clockwiseButton.addActionListener(e -> {
			if (clockwiseButton.getText().equals(STRING_CLOCKWISE)) {
				clockwiseButton.setText(STRING_ANTICLOCKWISE);
				mBotController.setClockwise(false);
			}
			else {
				clockwiseButton.setText(STRING_CLOCKWISE);
				mBotController.setClockwise(true);
			}
			
		});
		
		connectButton.addActionListener(e -> {
			connectoToMBot(mBotController);
		});
		
		//add(slider);
		add(connectButton);
		add(disconnectButton);
		add(driveButton);
		add(clockwiseButton);
		add(pingButton);
		add(baseMotorSpeed);
	}
	
	private void setDissabled() {
		driveButton.setEnabled(false);
		pingButton.setEnabled(false);
		disconnectButton.setEnabled(false);
		connectButton.setEnabled(true);
		powerButton.setEnabled(false);
		baseMotorSpeed.setEnabled(false);
		clockwiseButton.setEnabled(false);
		baseMotorSpeed.setText("20");
	}
	
	public void connectoToMBot(ControllerMBot mBotController) {
		try {
			mBotController.connect();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driveButton.setEnabled(true);
		disconnectButton.setEnabled(true);
		pingButton.setEnabled(true);
		powerButton.setEnabled(true);
		connectButton.setEnabled(false);
		baseMotorSpeed.setEnabled(true);
		clockwiseButton.setEnabled(true);
	}
}
