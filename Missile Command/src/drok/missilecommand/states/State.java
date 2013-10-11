package drok.missilecommand.states;

import java.io.File;
import java.util.Date;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.GameSave;

public abstract class State extends BasicGameState {
	public static final int SCALE = 6;
	
	protected GameContainer container;
	protected StateBasedGame game;
	protected static Image screenImg;
	protected static TrueTypeFont font16, font32;
	private static GameSave currentSave;
	protected Music music;
	private int state;
	private boolean firstTime = true;
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		this.container = container;
		this.game = game;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		renderScaled(g);
		g.copyArea(screenImg, 0, 0);
		g.clear();
		screenImg.draw(0, 0, SCALE);
	}

	protected void renderScaled(Graphics g) {}

	public void firstTimeEnter() throws SlickException {}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		if(firstTime) {
			firstTimeEnter();
			firstTime = false;
		}
	}

	public static void init(GameContainer container) {
		try {
			screenImg = new Image(container.getWidth() / SCALE, container.getHeight() / SCALE);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		try {
			java.awt.Font awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("res/fonts/minecraftia.ttf"));
			font16 = new TrueTypeFont(awtFont.deriveFont(16f), false);
	        font32 = new TrueTypeFont(awtFont.deriveFont(32f), false);
		} catch (Exception e) {
			System.err.println("Failed to load font");
			e.printStackTrace();
		}

		try {
			currentSave = new GameSave("Default");
		} catch (Exception e) {
			System.err.println("Failed to load save, is the file corrupted?");
			e.printStackTrace();
			File file = new File("res/data/saves/Default.sav");
			file.renameTo(new File("res/data/saves/Default corrupted backup " + new Date().toString().replaceAll(":", "-") + ".backupsav"));
			file = new File("res/data/saves/Default.sav");
			if(file.exists())
				file.delete();
			try {
				currentSave = new GameSave("Default");
			} catch (Exception e1) {
				System.err.println("Failed to create new save");
				e1.printStackTrace();
			}
		}
	}
	
	public void changeMusic(Music music) {
		this.music = music;
	}
	
	public State(int state) {
		this.state = state;
	}

	public int getID() {
		return state;
	}

	protected GameSave getCurrentSave() {
		return currentSave;
	}
}