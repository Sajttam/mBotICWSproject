package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bot.controller.ControllerMBot;
import modelDebug.MBotModelDebug;


public class Controller {

	public static final String ADRESS_PREFIX = "btspp://";
	public static final String ADRESS_POSTFIX = ":1;authenticate=false;encrypt=false;master=false";
	
	private List<ControllerMBot> mBotControllers = null;

	public Controller() {
		mBotControllers = new ArrayList<ControllerMBot>();
	}
	
	public Controller(BotJSON[] bots) throws IOException {
		mBotControllers = new ArrayList<ControllerMBot>();
		for (BotJSON b : bots) {
			generateNewBot(b.botName, ADRESS_PREFIX + b.adressBT + ADRESS_POSTFIX);
		}
	}
	
	public ControllerMBot generateNewBot(String botName, String adressBT) throws IOException {
		MBotModelDebug model = new MBotModelDebug(botName, adressBT);
		ControllerMBot mBot = new ControllerMBot(model);
		
		if (mBotControllers.contains(mBot)) {
			mBot = null;
		}
		else {
			mBotControllers.add(mBot);
		}
		return mBot;
	}

	public List<ControllerMBot> getmBotControllers() {
		return mBotControllers;
	}
}
