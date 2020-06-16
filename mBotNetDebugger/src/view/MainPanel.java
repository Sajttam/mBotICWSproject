package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import bot.controller.ControllerMBot;
import controller.Controller;
import modelDebug.MBotModelDebug;

public class MainPanel extends JPanel {
	public static final String ADRESS_PREFIX = "btspp://";
	public static final String ADRESS_POSTFIX = ":1;authenticate=false;encrypt=false;master=false";
	JPanel mBotIntefacePanel;
	
	public MainPanel(Controller controller, JFrame frame) throws IOException {
		setLayout(new BorderLayout());
		
		//Generate container for interface panel (mBot controller)
		mBotIntefacePanel = new JPanel();		
		BoxLayout boxlayout = new BoxLayout(mBotIntefacePanel, BoxLayout.Y_AXIS);
		mBotIntefacePanel.setLayout(boxlayout);		
		
		frame.setJMenuBar(new MenuView(this, controller, mBotIntefacePanel, frame));
		
		//add(new MbotAddPanel(this, controller, mBotIntefacePanel, frame),BorderLayout.WEST);
		add(mBotIntefacePanel,BorderLayout.EAST);
		
		for (ControllerMBot b : controller.getmBotControllers()) {
			InformationPanel ip = new InformationPanel();
			MBotModelDebug deBot = (MBotModelDebug) b.getmBotModel();
			deBot.addPropertyChangeListener(ip);
			
			mBotIntefacePanel.add(ip);
			mBotIntefacePanel.add(new ControlPanel(b));
			
			b.getmBotModel().setRoundTripTime(99999);			
			String oldName = b.getmBotModel().getBotName();
			b.getmBotModel().setBotName("");
			b.getmBotModel().setBotName(oldName);
			
			frame.pack();
		}
	}
	
	public void addBotInteface(String botName, String adressBT, Controller controller, JPanel mBotIntefacePanel,
			JFrame frame) {
		try {
			ControllerMBot mBotController = controller.generateNewBot(botName, ADRESS_PREFIX + adressBT + ADRESS_POSTFIX);
			if (mBotController != null) {
				InformationPanel ip = new InformationPanel();
				MBotModelDebug deBot = (MBotModelDebug) mBotController.getmBotModel();
				deBot.addPropertyChangeListener(ip);
				
				mBotIntefacePanel.add(ip);
				mBotIntefacePanel.add(new ControlPanel(mBotController));
				
				mBotController.getmBotModel().setRoundTripTime(99999);
				mBotController.getmBotModel().setBotName(botName);
				
				frame.pack();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connectAllBots() {
		for (Component c : mBotIntefacePanel.getComponents()) {
			if (c instanceof ControlPanel) {
				ControlPanel controlPanel = (ControlPanel) c;
				controlPanel.connectButton.doClick();
			}
		}
	}

	public void driveAllBots() {
		for (Component c : mBotIntefacePanel.getComponents()) {
			if (c instanceof ControlPanel) {
				ControlPanel controlPanel = (ControlPanel) c;
				controlPanel.driveButton.doClick();
			}
		}
	}
}
