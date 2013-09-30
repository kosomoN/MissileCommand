package drok.missilecommand.states.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Planet;
import drok.missilecommand.debris.Asteroid;
import drok.missilecommand.debris.BigAsteroid;

public class EndlessGameState extends GameState {

	private int timeSinceLastDebris;
	
	public EndlessGameState(int state) {
		super(state);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		planet = new Planet(container.getWidth() / 2 / SCALE, container.getHeight() / 2 / SCALE, "Earth");
		super.enter(container, game);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		super.update(container, game, delta);
		if(!paused) {
			timeSinceLastDebris += delta;
			if(timeSinceLastDebris >= 1000 && !planet.isHit()) {
				double rand = Math.random();
				float x, y;
				if(rand < 0.5) {
					y = (float) (Math.random() * screenImg.getHeight());
					if(rand < 0.25)
						x = -16;
					else
						x = screenImg.getWidth() + 16;
				} else {
					x = (float) (Math.random() * screenImg.getWidth());
					if(rand < 0.75)
						y = -16;
					else
						y = screenImg.getHeight() + 16;
				}
				rand = 1;//Math.random(); //Uncomment to enable different debris
				if(rand < 0.1)
					addEntity(new BigAsteroid(x, y, 0.02f, (float) (Math.random() * 360), planet));
				else
					addEntity(new Asteroid(x, y, 0.02f, (float) (Math.random() * 360), planet));
				timeSinceLastDebris = 0;
			}
		}
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);
		if(planet.isHit()) {
			g.setFont(font32);
			g.setColor(new Color(1, 1, 1, gameOverColor.a - 0.5f));
			g.drawString("Score: " + score, (container.getWidth() - font32.getWidth("Score: " + score)) / 2, container.getHeight() / 2 + 100);
		} else {
			g.drawString("Missiles: " + missiles, 10, 35);
			g.drawString("Score: " + score, 10, 60);
		}
	}

	@Override
	protected void renderScaled(Graphics g) {
		super.renderScaled(g);
		if(planet.isHit()) {
			gameOver.draw((screenImg.getWidth() - gameOver.getWidth()) / 2, (screenImg.getHeight() - gameOver.getHeight()) / 2, gameOverColor);
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		 if(gameOverColor.a > 0.7f){
			planet.reset();
		}
	}
}
