package drok.missilecommand.states;

import java.awt.Font;
import java.io.File;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.utils.ResourceManager;

public abstract class State extends BasicGameState {
	private int state;
	protected GameContainer container;
	protected StateBasedGame game;
	public static final int SCALE = 6;
	protected static Image screenImg;
	protected static TrueTypeFont font16, font32;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.container = container;
		this.game = game;
	}

	public static void init(GameContainer container) {
	    try {
	    	ResourceManager.loadResources();
	    	screenImg = new Image(container.getWidth() / SCALE, container.getHeight() / SCALE);
	        Font awtFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/pixelmix.ttf"));
	        font16 = new TrueTypeFont(awtFont.deriveFont(16f), false);
	        font32 = new TrueTypeFont(awtFont.deriveFont(32f), false);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }  
	}
	
	public State(int state) {
		this.state = state;
	}

	public int getID() {
		return state;
	}

}