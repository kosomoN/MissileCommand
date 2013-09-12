package drok.missilecommand.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class State extends BasicGameState {
	int state;
	protected static final int SCALE = 8;
	protected Image screenImg;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		screenImg = new Image(container.getWidth() / SCALE, container.getHeight() / SCALE);
	}

	public State(int state) {
		this.state = state;
	}

	public int getID() {
		return state;
	}

}