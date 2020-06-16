package view;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import bot.controller.ControllerMBot;
import controller.Controller;
import rsu.algorithms.QueueAlg;
import rsu.algorithms.TrafficLightAlg;
import rsu.server.ControllerRSU;
import rsu.view.ViewRSU;

public class MenuView extends JMenuBar {

	private static final String ADRESS_LAG_0 = "000D19033AF9";
	private static final String ADRESS_LAG_1 = "000D190336D0";
	private static final String ADRESS_LAG_2 = "000D19124CC0";
	

	public MenuView(MainPanel parent, Controller controller, JPanel mBotIntefacePanel, JFrame frame,ControllerRSU controllerRSU, ViewRSU viewRSU) {
		JMenu mBotMenu = new JMenu("Menu");
		
		JMenuItem itemConnectAll = new JMenuItem("Connect All");
		JMenuItem itemDriveAll = new JMenuItem("Drive All");
		JMenuItem itemSetTeam0 = new JMenuItem("Add bot 0, CARolus");
		JMenuItem itemSetTeam1 = new JMenuItem("Add bot 1, CARolin");
		JMenuItem itemSetTeam2 = new JMenuItem("Add bot 2, CARlos");
		JMenuItem itemSetTLA = new JMenuItem("Set Trafficlight alg.");
		JMenuItem itemSetQA = new JMenuItem("Set Queue alg.");
		
		// itemDrive.addActionListener(e -> controller.toggleDriving());
		
		itemConnectAll.addActionListener(e -> parent.connectAllBots());
		itemDriveAll.addActionListener(e -> parent.driveAllBots());
		
		itemSetTLA.addActionListener(e -> controllerRSU.setAlgorithmICWS(new TrafficLightAlg(), viewRSU));
		itemSetQA.addActionListener(e -> controllerRSU.setAlgorithmICWS(new QueueAlg(), viewRSU));
		
		
		itemSetTeam0.addActionListener(e -> {
			parent.addBotInteface("CARolus(0)", ADRESS_LAG_0, controller, mBotIntefacePanel, frame);
		});

		itemSetTeam1.addActionListener(e -> {
			parent.addBotInteface("CARolin(1)", ADRESS_LAG_1, controller, mBotIntefacePanel, frame);
		});
		
		itemSetTeam2.addActionListener(e -> {
			parent.addBotInteface("CARlos(2)", ADRESS_LAG_2, controller, mBotIntefacePanel, frame);
		});

		mBotMenu.add(itemConnectAll);
		//mBotMenu.add(itemDriveAll);
		mBotMenu.add(itemSetTLA);
		mBotMenu.add(itemSetQA);
		//mBotMenu.add(itemSetTeam0);
		//mBotMenu.add(itemSetTeam1);
		//mBotMenu.add(itemSetTeam2);

		add(mBotMenu);
	}

	
}
