package drok.missilecommand;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.states.State;
import drok.missilecommand.states.game.*;
import drok.missilecommand.states.menu.*;

public class Launch extends StateBasedGame {
	//Fields
	public static final int MENUSTATE = 0, GAMEMODESTATE = 1, GAMESTATE = 2, HELPSTATE = 3, LOADINGSTATE = 4,
			SHOPSTATE = 5;
	
	public Launch() {
		super("Missile Command");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		State.init(container);
		addState(new LoadingState(LOADINGSTATE));
		addState(new MenuState(MENUSTATE));
		addState(new GameModeState(GAMEMODESTATE));
		addState(new GameState(GAMESTATE));
		addState(new HelpState(HELPSTATE));
		addState(new ShopState(SHOPSTATE));
	}

	public static void main(String[] args) throws SlickException {
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
