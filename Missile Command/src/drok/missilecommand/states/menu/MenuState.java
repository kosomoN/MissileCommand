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
	private Image arrow;
	private Input input;
	private boolean ignoreClick = false;

	public MenuState(int state) {
		super(state);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		music = new Music("res/audio/music_space.ogg", true);
		input = container.getInput();
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)	throws SlickException {
		super.enter(container, game);
		ignoreClick = true;
		arrow = ResourceManager.getImage("res/graphics/Arrow.png");
		selectx =  container.getWidth() - (80 - arrow.getWidth());
		selecty =  container.getHeight() - 160;
		music.loop();
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		super.leave(container, game);
		music.stop();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//planet.drawCentered(screenImg.getWidth() / 2, screenImg.getHeight() / 2);
		g.copyArea(screenImg, 0, 0);
		g.clear();
		screenImg.draw(0, 0, SCALE);
		arrow.draw(selectx, selecty);
		g.setFont(bigFont);
		g.setColor(Color.white);
		g.drawString("Start", container.getWidth() - 80 - bigFont.getWidth("Start"), container.getHeight() - 160);
		g.drawString("Help", container.getWidth() - 80 - bigFont.getWidth("Help"), container.getHeight() -  110);
		g.drawString("Exit", container.getWidth() - 80 - bigFont.getWidth("Exit"), container.getHeight() -  60);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(Util.isInside(input.getMouseX(), input.getMouseY(), new Rectangle(container.getWidth() - 80 - bigFont.getWidth("Start"), container.getHeight() - 160,
				bigFont.getWidth("Start"), bigFont.getHeight()))) {
			selecty = container.getHeight() - 160;
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && !ignoreClick)
				game.enterState(Launch.GAMEMODESTATE);
		} else if(Util.isInside(input.getMouseX(), input.getMouseY(), new Rectangle(container.getWidth() - 80 - bigFont.getWidth("Help"), container.getHeight() - 110,
				bigFont.getWidth("Help"), bigFont.getHeight()))) {
			selecty = container.getHeight() - 110;
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON) && !ignoreClick)
				game.enterState(Launch.HELPSTATE);
		} else if(Util.isInside(input.getMouseX(), input.getMouseY(), new Rectangle(container.getWidth() - 80 - bigFont.getWidth("Exit"), container.getHeight() - 60,
				bigFont.getWidth("Exit"), bigFont.getHeight()))) {
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