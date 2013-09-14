package drok.missilecommand.states;

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
import drok.missilecommand.utils.ResourceManager;
import drok.missilecommand.utils.Util;

public class MenuState extends State {
	//Fields
	private float selectx, selecty;
	private Input input;
	private GameContainer container;
	private Music music;
	
	private Image planet;
	private Image mask;
	private Image planetTexture;
	private int timer;
	private int imgPos;
	private Image arrow;

	public MenuState(int state) {
		super(state);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
	    selectx =  screenImg.getWidth() - 170 / SCALE;
		selecty =  screenImg.getHeight() - 100 / SCALE;
		this.container = container;
		input = container.getInput();
		
		/*planet = new Image(120, 120);
		planet.setFilter(Image.FILTER_NEAREST);
		planetTexture = new Image("res/graphics/Big Planet Texture.png");
		planetTexture.setFilter(Image.FILTER_NEAREST);
		mask = new Image("res/graphics/Big Planet Mask.png");
		mask.setFilter(Image.FILTER_NEAREST);*/
		
		music = new Music("res/audio/music_space.ogg");
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)	throws SlickException {
		super.enter(container, game);
		arrow = ResourceManager.getImage("res/graphics/Arrow.png");
		music.play();
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
		arrow.draw(container.getWidth() - 70, selecty);
		g.setFont(bigFont);
		g.setColor(Color.white);
		g.drawString("Start", container.getWidth() - 80 - bigFont.getWidth("Start"), container.getHeight() - 160);
		g.drawString("Help", container.getWidth() - 80 - bigFont.getWidth("Help"), container.getHeight() -  110);
		g.drawString("Exit", container.getWidth() - 80 - bigFont.getWidth("Exit"), container.getHeight() -  60);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		//Moving background
		/*
		timer += delta;
		
		if(timer > 500) {
			imgPos += 1;
			if(imgPos >= 256)
				imgPos = 0;
			try {
				Graphics g = planet.getGraphics();
				g.clear();
				g.drawImage(mask, 0, 0);
				g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
				g.drawImage(planetTexture, -imgPos, 0);
				g.drawImage(planetTexture, -imgPos + 256, 0);
				g.flush();
			} catch (SlickException e) {
				e.printStackTrace();
			}
			timer = 0;
		}*/
		
		//Changing menu selections
		if(Util.isInside(input.getMouseX(), input.getMouseY(), new Rectangle(container.getWidth() - 200, container.getHeight() - 165, 150, 50))) {
			selecty = container.getHeight() - 150;
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
				game.enterState(Launch.GAMESTATE);
		} else if(Util.isInside(input.getMouseX(), input.getMouseY(), new Rectangle(container.getWidth() - 200, container.getHeight() - 115, 150, 50))) {
			selecty = container.getHeight() - 100;
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
				game.enterState(Launch.HELPSTATE);
		} else if(Util.isInside(input.getMouseX(), input.getMouseY(), new Rectangle(container.getWidth() - 200, container.getHeight() - 65, 150, 65))) {
			selecty = container.getHeight() - 50;
			if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON))
				container.exit();
		}
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if(key == Input.KEY_DOWN) {
			if(selecty < container.getHeight() - 41) {
				selecty += 30;
			}
		} else if(key == Input.KEY_UP) {
			if(selecty > container.getHeight() - 100) {
				selecty -= 30;
			}
		} else if(key == Input.KEY_ENTER) {
			if(selecty == container.getHeight() - 100) {
				game.enterState(Launch.GAMESTATE);
			} else if(selecty == container.getHeight() - 70) {
				game.enterState(Launch.HELPSTATE);
			} else if(selecty == container.getHeight() - 40) {
				System.exit(0);
			}
		}
	}
}