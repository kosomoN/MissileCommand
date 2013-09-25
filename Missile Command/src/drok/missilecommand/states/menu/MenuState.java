package drok.missilecommand.states.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Launch;
import drok.missilecommand.states.State;
import drok.missilecommand.utils.Button;
import drok.missilecommand.utils.ResourceManager;

public class MenuState extends State {
	//Fields
	private float selectx, selecty;
	private Music music;
	private Image arrow, background;
	private Input input;
	private boolean ignoreClick;
	private static boolean playArcade = false;
	private Button arcade, campaign, help, exit;

	public MenuState(int state) {
		super(state);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		input = container.getInput();
		
		campaign = new Button(container.getWidth() - 80 - font32.getWidth("Campaign"), container.getHeight() - font32.getLineHeight() * 4 - 40, font32.getWidth("Campaign"), font32.getHeight(), "Campaign", new Color(200, 0, 50, 1f), Color.white);
		arcade = new Button(container.getWidth() - 80 - font32.getWidth("Arcade"), container.getHeight() - font32.getLineHeight() * 3 - 30, font32.getWidth("Arcade"), font32.getHeight(), "Arcade", new Color(20, 0, 50, 0f), Color.white);
		help = new Button(container.getWidth() - 80 - font32.getWidth("Help"), container.getHeight() - font32.getLineHeight() * 2 - 20, font32.getWidth("Help"), font32.getHeight(), "Help", new Color(20, 0, 50, 0f), Color.white);
		exit = new Button(container.getWidth() - 80 - font32.getWidth("Exit"), container.getHeight() - font32.getLineHeight() - 10, font32.getWidth("Exit"), font32.getHeight(), "Exit", new Color(20, 0, 50, 0f), Color.white);
	}
	
	@Override
	public void firstTimeEnter() throws SlickException {
		super.firstTimeEnter();
		ignoreClick = false;
		music = new Music("res/audio/music_space.ogg", true);
		arrow = ResourceManager.getImage("res/graphics/Arrow.png");
		background = ResourceManager.getImage("res/graphics/Background.png");
		background = background.getScaledCopy((float) container.getHeight() / background.getHeight());
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)	throws SlickException {
		ignoreClick = true;
		super.enter(container, game);
		selectx =  container.getWidth() - (80 - arrow.getWidth());
		selecty =  container.getHeight() - 160;
		if(!music.playing())
			music.loop();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//background.draw((container.getWidth() - background.getWidth()) / 2, 0);
		arrow.draw(selectx, selecty);

		arcade.renderWithoutBorder(g, font32);
		campaign.renderWithoutBorder(g, font32);
		help.renderWithoutBorder(g, font32);
		exit.renderWithoutBorder(g, font32);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(!ignoreClick) {
			if(campaign.hoverOver(input.getMouseX(), input.getMouseY())) {
				selecty = campaign.getY() + campaign.getHeight() / 4;
				if(campaign.clicked(input.getMouseX(), input.getMouseY(), container)) {
					music.fade(2000, 0, true);
					//Must check if campaign already started
					playArcade = true;
					game.enterState(Launch.LEVELSELECTSTATE);
				}
			} else if(arcade.hoverOver(input.getMouseX(), input.getMouseY())) {
				selecty = arcade.getY() + arcade.getHeight() / 4;
				if(arcade.clicked(input.getMouseX(), input.getMouseY(), container)) {
					music.fade(2000, 0, true);
					game.enterState(Launch.GAMEMODESTATE);
				}
			} else if(help.hoverOver(input.getMouseX(), input.getMouseY())) {
				selecty = help.getY() + help.getHeight() / 4;
				if(help.clicked(input.getMouseX(), input.getMouseY(), container))
					game.enterState(Launch.HELPSTATE);
			} else if(exit.hoverOver(input.getMouseX(), input.getMouseY())) {
				selecty = exit.getY() + exit.getHeight() / 4;
				if(exit.clicked(input.getMouseX(), input.getMouseY(), container))
					container.exit();
			}
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		if(ignoreClick)
			ignoreClick = false;
	}
	
	public static boolean isPlayingArcade() {
		return playArcade;
	}
}