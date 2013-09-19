package drok.missilecommand.states.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Launch;
import drok.missilecommand.states.State;
import drok.missilecommand.utils.ResourceManager;
import drok.missilecommand.utils.Util;

public class MenuState extends State {
	//Fields
	private float selectx, selecty;
	private Music music;
	private Image arrow, background;
	private Input input;
	private boolean ignoreClick = false;

	public MenuState(int state) {
		super(state);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);

		input = container.getInput();
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)	throws SlickException {
		super.enter(container, game);
		ignoreClick = true;
		if(music == null) {
			music = new Music("res/audio/music_space.ogg", true);
			arrow = ResourceManager.getImage("res/graphics/Arrow.png");
			background = ResourceManager.getImage("res/graphics/Background.png");
			background = background.getScaledCopy((float) container.getHeight() / background.getHeight());
		}
		selectx =  container.getWidth() - (80 - arrow.getWidth());
		selecty =  container.getHeight() - 160;
		if(!music.playing())
			music.loop();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		background.draw((container.getWidth() - background.getWidth()) / 2, 0);
		arrow.draw(selectx, selecty);
		g.setFont(font32);
		g.setColor(Color.white);
		g.drawString("Start", container.getWidth() - 80 - font32.getWidth("Start"), container.getHeight() - 160);
		g.drawString("Help", container.getWidth() - 80 - font32.getWidth("Help"), container.getHeight() -  110);
		g.drawString("Exit", container.getWidth() - 80 - font32.getWidth("Exit"), container.getHeight() -  60);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(Util.isInside(input.getMouseX(), input.getMouseY(), new Rectangle(container.getWidth() - 80 - font32.getWidth("Start"), container.getHeight() - 160,
				font32.getWidth("Start"), font32.getHeight()))) {
			selecty = container.getHeight() - 160;
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && !ignoreClick) {
				music.stop();
				game.enterState(Launch.GAMEMODESTATE);
			}
		} else if(Util.isInside(input.getMouseX(), input.getMouseY(), new Rectangle(container.getWidth() - 80 - font32.getWidth("Help"), container.getHeight() - 110,
				font32.getWidth("Help"), font32.getHeight()))) {
			selecty = container.getHeight() - 110;
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && !ignoreClick)
				game.enterState(Launch.HELPSTATE);
		} else if(Util.isInside(input.getMouseX(), input.getMouseY(), new Rectangle(container.getWidth() - 80 - font32.getWidth("Exit"), container.getHeight() - 60,
				font32.getWidth("Exit"), font32.getHeight()))) {
			selecty = container.getHeight() - 60;
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && !ignoreClick)
				container.exit();
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		if(ignoreClick)
			ignoreClick = false;
	}
	
	
}