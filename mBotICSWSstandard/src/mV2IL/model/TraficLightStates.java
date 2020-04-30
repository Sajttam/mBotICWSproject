package mV2IL.model;

public enum TraficLightStates {
	ALL_RED(1), SOUTH_NORTH_GREEN(2), EAST_WEST_GREEN(3);
	
	int state;

	TraficLightStates(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}
}
