package drok.missilecommand;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.states.*;

public class Launch extends StateBasedGame {
	//Fields
	public static final int MENUSTATE = 0, GAMESTATE = 1, HELPSTATE = 2, LOADINGSTATE = 3;
	
	public Launch() {
		super("Missile Command");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		State.init(container);
		addState(new LoadingState(LOADINGSTATE));
		addState(new MenuState(MENUSTATE));
		addState(new GameState(GAMESTATE));
		addState(new HelpState(HELPSTATE));
	}

	public static void main(String[] args) throws SlickException {
		System.out.println();
		AppGameContainer app = new AppGameContainer(new Launch());
		app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
		//app.setDisplayMode(1080, 680, false);
		app.setTargetFrameRate(60);
		app.setMaximumLogicUpdateInterval(10);
		app.setMinimumLogicUpdateInterval(10);
		app.setShowFPS(false);
		app.start();
	}

}
