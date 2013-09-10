package drok.missilecommand;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Planet {
	//Fields
	private static Image planet;
	private static int x, y;
	
	public Planet(int x, int y) {
		Planet.x = x;
		Planet.y = y;
	}
	
	public static void init() throws SlickException {
		planet = new Image("res/graphics/Planet.png");
		planet.setFilter(Image.FILTER_NEAREST);
	}
	
	public void render() {
		planet.drawCentered(x, y);
	}
	
	public void update() {
		
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
