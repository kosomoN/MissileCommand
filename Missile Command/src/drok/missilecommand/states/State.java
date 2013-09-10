package drok.missilecommand.states;

import org.newdawn.slick.state.BasicGameState;

public abstract class State extends BasicGameState {
	//Fields
	int state;
	
	public State(int state) {
		this.state = state;
	}

	public int getID() {
		return state;
	}

}
