package drok.missilecommand.states.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.Transition;

import drok.missilecommand.Launch;
import drok.missilecommand.states.State;
import drok.missilecommand.utils.Util;

public class GameModeState extends State {
	//Fields
	private GameContainer container;
	private Image GUISheet, leftArrow, rightArrow, key;
	private static int difflevel = 1;
	private int arrowScale = 2;
	private int halfwidth, halfheight;
	private String command1 = "When you are ready,";
	private String command2 = "take the key and get into your turret!";
	private boolean leaving = false;
	
	public enum Difficulty {
		EASY, NORMAL, HARD;
		
		public String toString() {
			return super.toString();
		}
	}
	
	public GameModeState(int state) {
		super(state);
		System.out.println(Difficulty.EASY.toString());
		try {
			GUISheet = new Image("res/graphics/GUIsheet.png");
			leftArrow = GUISheet.getSubImage(0, 0, 11, 11);
			leftArrow.setFilter(Image.FILTER_NEAREST);
			leftArrow = leftArrow.getScaledCopy(arrowScale);
			
			rightArrow = GUISheet.getSubImage(11, 0, 11, 11);
			rightArrow.setFilter(Image.FILTER_NEAREST);
			rightArrow = rightArrow.getScaledCopy(arrowScale);
			
			key = GUISheet.getSubImage(26, 26, 12, 5);
			key.setFilter(Image.FILTER_NEAREST);
			key = key.getScaledCopy(SCALE);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		this.container = container;
		halfwidth = container.getWidth() / 2;
		halfheight = container.getHeight() / 2;
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		leaving = false;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if(!leaving) {
			//Changing font to pixelmix
			g.setFont(font16);
			
			//Rendering commands
			g.drawString(command1, halfwidth - 100, 50);
			g.drawString(command2, halfwidth - 200, 75);
			
			//Bad code!
			//Drawing difficulty
			if(difflevel == 0) {
				g.drawString("Easy", halfwidth - 20, halfheight);
			} else if(difflevel == 1) {
				g.drawString("Medium", halfwidth - 30, halfheight);
			} else if(difflevel == 2) {
				g.drawString("Hard", halfwidth - 20, halfheight);
			}
			
			//Drawing arrows and difficulty title
			g.drawString("Difficulty", halfwidth - 50, halfheight - 30);
			leftArrow.draw(halfwidth - leftArrow.getWidth() - 50, halfheight);
			rightArrow.draw(halfwidth + 50, halfheight);
			
			//Drawing play button and key
			g.drawString("Play", halfwidth - 20, container.getHeight() - key.getHeight() - 50);
			key.drawCentered(halfwidth, container.getHeight() - key.getHeight() - 10);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if(key == Input.KEY_ENTER) {
			leaving = true;
			game.enterState(Launch.GAMESTATE, new Transition() {
				private int delta = 0;
				@Override
				public void update(StateBasedGame game, GameContainer container, int delta) throws SlickException {
					this.delta += delta;
				}
				
				@Override
				public void preRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
					g.setFont(font32);
					g.drawString("Loading...", container.getWidth() / 2 - font16.getWidth("Loading...") / 2, container.getHeight() / 2 - 10);
				}
				
				@Override
				public void postRender(StateBasedGame game, GameContainer container, Graphics g) throws SlickException {
					g.clear();
				}
				
				@Override
				public boolean isComplete() {
					return delta > 1000;
				}
				
				@Override
				public void init(GameState gs1, GameState gs2) {
					
				}
			}, null);
		} else if(key == Input.KEY_LEFT) {
			if(difflevel > 0)
				difflevel--;
		} else if(key == Input.KEY_RIGHT) {
			if(difflevel < 2) {
				difflevel++;
			}
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		if(button == Input.MOUSE_LEFT_BUTTON) {
			if(Util.isInside(x, y, new Rectangle(halfwidth - leftArrow.getWidth() - 50, halfheight, 11 * arrowScale, 11* arrowScale))) {
				if(difflevel > 0)
					difflevel--;
			} else if(Util.isInside(x, y, new Rectangle(halfwidth + 50, halfheight, 11 * arrowScale, 11  * arrowScale))) {
				if(difflevel < 2)
					difflevel++;
			} else if(Util.isInside(x, y, new Rectangle(halfwidth - 20, container.getHeight() - key.getHeight() - 50, 41, 20))) {
				game.enterState(Launch.GAMESTATE);
			}
		}
	}

	public static String getDifficulty() {
		if(difflevel == 0) {
			return Difficulty.EASY.toString();
		} else if(difflevel == 1) {
			return Difficulty.NORMAL.toString();
		} else if(difflevel == 2) {
			return Difficulty.NORMAL.toString();
		} else {
			return "Exception in: GameModeState.java - public static String getDifficulty() [lines:166-176]";
		}
	}
}
