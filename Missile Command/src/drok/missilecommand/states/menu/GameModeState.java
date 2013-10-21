package drok.missilecommand.states.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Launch;
import drok.missilecommand.states.State;
import drok.missilecommand.utils.Button;
import drok.missilecommand.utils.ResourceManager;

public class GameModeState extends State {
	//Fields
	private Image GUISheet;
	private static Difficulty difficulty = Difficulty.NORMAL;
	private int arrowScale = 2;
	private String command1 = "When you are ready,";
	private String command2 = "take the key and get ready for battle!";
	private boolean leaving = false;
	private Button leftArrow, rightArrow, play, back;
	
	public enum Difficulty {
		EASY(0), NORMAL(1), HARD(2);
		
		int asNumber;
		private Difficulty(int asNumber) {
			this.asNumber = asNumber;
		}
		
		public int getAsNumber() {
			return asNumber;
		}
		
		public Difficulty getFromNumber(int num) {
			for(Difficulty d : Difficulty.values())
				if(d.asNumber == num)
					return d;
			
			return null;
		}
		
		public String toString() {
			String firstPart = super.toString().substring(0, 1);
			String secondPart = super.toString().substring(1).toLowerCase();
			return firstPart + secondPart;
		}
	}
	
	public enum GameMode {
		ARCADE, CAMPAIGN;
	}
	
	public GameModeState(int state) {
		super(state);
	}

	@Override
	public void firstTimeEnter() throws SlickException {
		super.firstTimeEnter();
		GUISheet = ResourceManager.getImage("GUIsheet.png");
		
		leftArrow = new Button(container.getWidth() / 2 - 22 - 50 , container.getHeight() * 2 / 3, 11, 11, GUISheet.getSubImage(26, 0, 11, 11), arrowScale);
		rightArrow = new Button(container.getWidth() / 2 + 50 , container.getHeight() * 2 / 3, 11, 11, GUISheet.getSubImage(37, 0, 11, 11), arrowScale);
		play = new Button(container.getWidth() / 2 - 6 * SCALE , container.getHeight() - 40 - 10, 12, 5, GUISheet.getSubImage(26, 11, 12, 5), SCALE);
		back = new Button(10, 10, ResourceManager.getImage("BackButton.png").getWidth(), ResourceManager.getImage("BackButton.png").getHeight(), ResourceManager.getImage("BackButton.png"), 1);
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
			g.drawString(command1, container.getWidth() / 2 - 100, 50);
			g.drawString(command2, container.getWidth() / 2 - 200, 75);
			
			//Drawing difficulty
			g.drawString(difficulty.toString(), container.getWidth() / 2 - g.getFont().getWidth(difficulty.toString()) / 2, container.getHeight() * 2 / 3);
			
			//Drawing arrows and difficulty title
			g.drawString("Difficulty", container.getWidth() / 2 - g.getFont().getWidth("Difficulty") / 2, container.getHeight() * 2 / 3 - g.getFont().getLineHeight() / 2 - 50);
			leftArrow.renderImage(g);
			rightArrow.renderImage(g);
			
			//Drawing play button and key
			g.drawString("Play", container.getWidth() / 2 - g.getFont().getWidth("Play") / 2, container.getHeight() - play.getHeight() - 50);
			play.renderImage(g);
			
			//rendering back button
			back.renderImage(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(leftArrow.clicked(container)) {
			if(difficulty.asNumber > 0)
				difficulty = difficulty.getFromNumber(difficulty.getAsNumber() - 1);
		} else if(rightArrow.clicked(container)) {
			if(difficulty.asNumber < 2)
				difficulty = difficulty.getFromNumber(difficulty.getAsNumber() + 1);
		} else if(play.clicked(container)) {
			if(MenuState.isPlayingArcade()) {
				((MenuState) game.getState(Launch.MENUSTATE)).getMusic().fade(2000, 0, true);
				game.enterState(Launch.ENDLESSGAMESTATE);
			} else
				game.enterState(Launch.LEVELGAMESTATE);
		}
		
		if(back.hoverOver(container.getInput().getMouseX(), container.getInput().getMouseY())) {
			back.changeImage(ResourceManager.getImage("BackButtonHover.png"));
			if(back.clicked(container))
				game.enterState(Launch.MENUSTATE);
		} else {
			back.changeImage(ResourceManager.getImage("BackButton.png"));
		}
	}

	public static String getDifficulty() {
		return difficulty.toString();
	}
}
