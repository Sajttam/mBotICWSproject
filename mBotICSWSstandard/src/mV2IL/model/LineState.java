package mV2IL.model;

public enum LineState {
	LINESTATE_OUTSIDE(0), LINESTATE_INSIDE(1), LINESTATE_HALFINSIDE_10(2), LINESTATE_HALFINSIDE_01(3),
	LINESTATE_HALFOUTSIDE_10(4), LINESTATE_HALFOUTSIDE_01(5), LINESTATE_OUT_OF_LINE(6);

	int state;

	LineState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}
}
