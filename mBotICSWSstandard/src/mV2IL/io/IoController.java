package mV2IL.io;

import javax.microedition.io.StreamConnection;

public abstract class IoController {
	private InputController inputController = null;
	private OutputController outputController = null;
	private StreamConnection streamConnection = null;
	private String adress = null;
	
	

	public InputController getInputController() {
		return inputController;
	}

	public void setInputController(InputController inputController) {
		this.inputController = inputController;
	}

	public OutputController getOutputController() {
		return outputController;
	}

	public void setOutputController(OutputController outputController) {
		this.outputController = outputController;
	}

	public StreamConnection getStreamConnection() {
		return streamConnection;
	}

	public void setStreamConnection(StreamConnection streamConnection) {
		this.streamConnection = streamConnection;
	}

	public String getAdress() {
		return adress;
	}

	public void setAdress(String adress) {
		this.adress = adress;
	}

}
