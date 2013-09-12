package drok.missilecommand.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MenuState extends State {

	private Image planet;
	private Image mask;
	private Image planetTexture;
	private int timer;
	private int imgPos;

	public MenuState(int state) {
		super(state);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		planet = new Image(120, 120);
		planet.setFilter(Image.FILTER_NEAREST);
		planetTexture = new Image("res/graphics/Big Planet Texture.png");
		planetTexture.setFilter(Image.FILTER_NEAREST);
		mask = new Image("res/graphics/Big Planet Mask.png");
		mask.setFilter(Image.FILTER_NEAREST);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		planet.drawCentered(screenImg.getWidth() / 2, screenImg.getHeight() / 2);
		g.copyArea(screenImg, 0, 0);
		g.clear();
		screenImg.draw(0, 0, SCALE);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
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
		}
	}

}