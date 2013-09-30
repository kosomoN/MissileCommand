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
	public static final int LOADINGSTATE = 0, MENUSTATE = 1, GAMEMODESTATE = 2,
							ENDLESSGAMESTATE = 3, HELPSTATE = 4, LEVELGAMESTATE = 5,
							SHOPSTATE = 6, LEVELSELECTSTATE = 7;
	
	public Launch() {
		super("Missile Command");
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		State.init(container);
		addState(new LoadingState(LOADINGSTATE));
		addState(new MenuState(MENUSTATE));
		addState(new GameModeState(GAMEMODESTATE));
		addState(new EndlessGameState(ENDLESSGAMESTATE));
		addState(new LevelBasedGameState(LEVELGAMESTATE));
		addState(new HelpState(HELPSTATE));
		addState(new ShopState(SHOPSTATE));
		addState(new LevelSelectState(LEVELSELECTSTATE));
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Launch());
//		app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
		app.setDisplayMode(1080, 600, false);
		//app.setTargetFrameRate(60);
		app.setMaximumLogicUpdateInterval(10);
		app.setMinimumLogicUpdateInterval(10);
		app.setShowFPS(false);
		app.start();
	}

}
