package drok.missilecommand;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.states.State;
import drok.missilecommand.states.game.EndlessGameState;
import drok.missilecommand.states.game.LevelBasedGameState;
import drok.missilecommand.states.game.ShopState;
import drok.missilecommand.states.menu.GameModeState;
import drok.missilecommand.states.menu.HelpState;
import drok.missilecommand.states.menu.LevelSelectState;
import drok.missilecommand.states.menu.LoadingState;
import drok.missilecommand.states.menu.MenuState;

public class Launch extends StateBasedGame {
	//Fields
	public static final int LOADINGSTATE = 0, MENUSTATE = 1, GAMEMODESTATE = 2,
							ENDLESSGAMESTATE = 3, HELPSTATE = 4, LEVELGAMESTATE = 5,
							SHOPSTATE = 6, LEVELSELECTSTATE = 7;
	
	public static final Properties PROPERTIES = new Properties();
	
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
	
	private static void loadProperties(GameContainer container) {
		File file = new File("misscomm.properties");
		if(file.exists()) {
			InputStream is = null;
			try {
				is = new FileInputStream(file);
				PROPERTIES.load(is);
			} catch (Exception e) {
				System.err.println("Failed to load properties");
				e.printStackTrace();
			} finally {
				try {
					if(is != null)
						is.close();
				} catch (IOException e) {
					System.err.println("Error loading properties (failed to close stream)");
					e.printStackTrace();
				}
			}
		}
		
		container.setSoundVolume(Float.valueOf(PROPERTIES.getProperty("SoundVolume", "1")));
		container.setMusicVolume(Float.valueOf(PROPERTIES.getProperty("MusicVolume", "1")));
		
		OutputStream os;
		try {
			os = new FileOutputStream(file);
			PROPERTIES.store(os, "Settings for missilecommand");
		} catch(IOException e) {
			System.err.println("Failed to save properties");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Launch());
		app.setDisplayMode(app.getScreenWidth(), app.getScreenHeight(), true);
//		app.setDisplayMode(700, 600, false);
		app.setTargetFrameRate(60);
		loadProperties(app);
//		app.setVSync(true);
		app.setMaximumLogicUpdateInterval(10);
		app.setMinimumLogicUpdateInterval(10);
//		app.setShowFPS(false);
		app.start();
	}

}
