package drok.missilecommand;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.states.GameState;

public class Launch extends StateBasedGame {
	//Fields
	private static final int MENUSTATE = 0, GAMESTATE = 1;
	
	public Launch() {
		super("Missile Command");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new GameState(GAMESTATE));
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Launch(), 1080, 720, false);
		app.start();
	}

}
