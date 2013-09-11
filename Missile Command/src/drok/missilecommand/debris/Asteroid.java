package drok.missilecommand.debris;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import drok.missilecommand.Planet;

public class Asteroid extends Debris {
	
	private static Image asteroidImage;
	
	public Asteroid(float x, float y, float speed, float direction) {
		super(x, y, speed, direction);
	}
	/**
	 * Creates an asteroid at the given location,
	 * pointing in the direction of the planet
	 * @param x 
	 * @param y
	 * @param speed
	 * @param planet
	 */
	public Asteroid(float x, float y, float speed, Planet planet) {
		super(x, y, speed, (float) Math.toDegrees(Math.atan2(planet.getY() - y, planet.getX() - x)));
	}

	public static void init() throws SlickException {
		asteroidImage = new Image("res/graphics/Asteroid.png");
		asteroidImage.setFilter(Image.FILTER_NEAREST);
	}
	
	@Override
	public void render(Graphics g) {
		asteroidImage.setRotation(rotation);
		asteroidImage.drawCentered(x, y);
	}
}
