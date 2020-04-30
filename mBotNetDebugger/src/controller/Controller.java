package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bot.controller.ControllerMBot;
import modelDebug.MBotModelDebug;


public class Controller {
	private List<ControllerMBot> mBotControllers = null;

	public Controller() {
		mBotControllers = new ArrayList<ControllerMBot>();
	}
	
	public ControllerMBot generateNewBot(String botName, String adressBT) throws IOException {
		MBotModelDebug model = new MBotModelDebug("NA", adressBT);
		ControllerMBot mBot = new ControllerMBot(model);
		
		if (mBotControllers.contains(mBot)) {
			mBot = null;
		}
		else {
			mBotControllers.add(mBot);
		}
		return mBot;
	}
}
