package drok.missilecommand.states;

import java.awt.Font;
import java.io.InputStream;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

public abstract class State extends BasicGameState {
	int state;
	protected static final int SCALE = 8;
	protected Image screenImg;
	TrueTypeFont font;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		screenImg = new Image(container.getWidth() / SCALE, container.getHeight() / SCALE);
		// load font from a .ttf file
	    try {
	        InputStream inputStream = ResourceLoader.getResourceAsStream("res/fonts/pixelmix.ttf");

	        Font awtFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
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