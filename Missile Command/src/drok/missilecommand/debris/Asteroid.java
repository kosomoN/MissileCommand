package drok.missilecommand.debris;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Asteroid extends Debris {
	
	private static Image asteroidImage;
	
	public Asteroid(int x, int y, float speed, float direction) {
		super(x, y, speed, direction);
	}
	
	public static void init() throws SlickException {
		asteroidImage = new Image("res/graphics/Asteroid.png");
		asteroidImage.setFilter(Image.FILTER_NEAREST);
	}
	
	@Override
	public void render() {
		asteroidImage.setRotation(rotation);
		asteroidImage.drawCentered(x, y);
	}

}
