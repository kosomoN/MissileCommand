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
	private int mouseX, mouseY;
	private Image GUISheet;
	private static Difficulty difficulty = Difficulty.NORMAL;
	private int arrowScale = 2;
	private String command1 = "When you are ready,";
	private String command2 = "take the key and get ready for battle!";
	private boolean leaving = false;
	private Button leftArrow, rightArrow, play;
	
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
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		GUISheet = ResourceManager.getImage("res/graphics/GUIsheet.png");
		
		//Creating the buttons needed
		leftArrow = new Button(container.getWidth() / 2 - 22 - 50 , container.getHeight() * 2 / 3, 11, 11, GUISheet.getSubImage(26, 0, 11, 11), arrowScale);
		rightArrow = new Button(container.getWidth() / 2 + 50 , container.getHeight() * 2 / 3, 11, 11, GUISheet.getSubImage(37, 0, 11, 11), arrowScale);
		play = new Button(container.getWidth() / 2 - 6 * SCALE , container.getHeight() - 40 - 10, 12, 5, GUISheet.getSubImage(26, 11, 12, 5), SCALE);
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
			leftArrow.render(g);
			rightArrow.render(g);
			
			//Drawing play button and key
			g.drawString("Play", container.getWidth() / 2 - g.getFont().getWidth("Play") / 2, container.getHeight() - play.getHeight() - 50);
			play.render(g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		mouseX = container.getInput().getMouseX();
		mouseY = container.getInput().getMouseY();
		
		if(leftArrow.clicked(mouseX, mouseY, container)) {
			if(difficulty.asNumber > 0)
				difficulty = difficulty.getFromNumber(difficulty.getAsNumber() - 1);
		} else if(rightArrow.clicked(mouseX, mouseY, container)) {
			if(difficulty.asNumber < 2)
				difficulty = difficulty.getFromNumber(difficulty.getAsNumber() + 1);
		} else if(play.clicked(mouseX, mouseY, container)) {
			if(MenuState.isPlayingArcade())
				game.enterState(Launch.LEVELGAMESTATE);
			else
				game.enterState(Launch.ENDLESSGAMESTATE);
		}
	}

	public static String getDifficulty() {
		return difficulty.toString();
	}
}
