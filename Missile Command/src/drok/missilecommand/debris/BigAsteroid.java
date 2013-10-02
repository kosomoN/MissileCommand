package drok.missilecommand.debris;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import drok.missilecommand.Planet;
import drok.missilecommand.states.game.GameState;
import drok.missilecommand.utils.ResourceManager;

public class BigAsteroid extends Debris {

	private static Image img;

	public BigAsteroid(float x, float y, float speed, float direction, Planet planet) {
		super(x, y, speed, direction, 8, planet);
	}

	@Override
	public void render(Graphics g) {
		img.setRotation((int) rotation);
		img.drawCentered(x, y);
	}

	@Override
	public boolean update(GameState gs, int delta) {
		if(isHit)
			return true;
		return super.update(gs, delta);
		
	}

	@Override
	public int getScore() {
		return 0;
	}
	
	public static void init() throws SlickException {
		img = ResourceManager.getImage("Big Asteroid.png");
		img.setFilter(Image.FILTER_NEAREST);
	}
	
	@Override
	public void hit(GameState gs) {
		super.hit(gs);
		gs.addEntity(new Asteroid(x, y, 0.05f, (float) Math.toDegrees(Math.atan2(planet.getY() - y, planet.getX() - x)) + 90, gs.getPlanet()));
		gs.addEntity(new Asteroid(x, y, 0.05f, (float) Math.toDegrees(Math.atan2(planet.getY() - y, planet.getX() - x)) - 90, gs.getPlanet()));
	}

	@Override
	public boolean renderScaled() {
		return true;
	}
}
