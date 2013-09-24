package drok.missilecommand.states.game;

import java.io.File;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Level;
import drok.missilecommand.utils.ResourceManager;

public class LevelBasedGameState extends GameState {

	private Level level;
	private static Image youWinImg;
	
	public LevelBasedGameState(int state) {
		super(state);
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		youWinImg = ResourceManager.getImage("res/graphics/You Win.png");
	}

	@Override
	public void restart() {
		super.restart();
		gameOverColor.a = -0.1f;
		try {
			level = new Level(this, new File("res/level/test.misscommlvl"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		super.update(container, game, delta);
		if(!paused) {
			level.update(delta, this);
			
			if(level.hasWon() || planet.isHit()) {
				if(gameOverColor.a < 1.5f) {
					gameOverColor.a += 0.0025f;
				}
			}
		}
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);
		if(level.hasWon() || planet.isHit()) {
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
		} else if(level.hasWon()) {
			youWinImg.draw((screenImg.getWidth() - gameOver.getWidth()) / 2, (screenImg.getHeight() - gameOver.getHeight()) / 2, gameOverColor);
		}
	}
}