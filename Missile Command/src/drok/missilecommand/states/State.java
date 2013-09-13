package drok.missilecommand.states;

import java.awt.Font;
import java.io.File;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class State extends BasicGameState {
	private int state;
	protected StateBasedGame game;
	protected static final int SCALE = 6;
	protected Image screenImg;
	protected TrueTypeFont font;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.game = game;
		screenImg = new Image(container.getWidth() / SCALE, container.getHeight() / SCALE);
		// load font from a .ttf file
	    try {
	        Font awtFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/pixelmix.ttf"));
	        awtFont = awtFont.deriveFont(16f); // set font size
	        font = new TrueTypeFont(awtFont, false);

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