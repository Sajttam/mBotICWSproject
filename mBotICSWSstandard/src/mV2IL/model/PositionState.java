package mV2IL.model;

/**
 * Represents a mBots positions on the map.
 * 
 * @author Mattias Sikvall Källström
 *
 */
public enum PositionState {
	POSITION_STATE_UNKNOWN(1), POSITION_STATE_ENTER_NORTH(2), POSITION_STATE_EXIT_NORTH(3),
	POSITION_STATE_ENTER_SOUTH(4), POSITION_STATE_EXIT_SOUTH(5), POSITION_STATE_ENTER_WEST(6),
	POSITION_STATE_EXIT_WEST(7), POSITION_STATE_ENTER_EAST(8), POSITION_STATE_EXIT_EAST(9);

	int state;

	PositionState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}
}
