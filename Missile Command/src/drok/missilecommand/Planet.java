package drok.missilecommand;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Planet {
	//Fields
	private static Image planet, planetTexture, mask;
	private static int x, y;
	private int timer = 5000;
	private int imgPos;
	
	public Planet(int x, int y) {
		Planet.x = x;
		Planet.y = y;
	}
	
	public static void init() throws SlickException {
		planetTexture = new Image("res/graphics/Planet Texture.png");
		planetTexture.setFilter(Image.FILTER_NEAREST);
		
		planet = new Image(16, 16);
		planet.setFilter(Image.FILTER_NEAREST);
		
		mask = new Image("res/graphics/Planet Mask.png");
		mask.setFilter(Image.FILTER_NEAREST);
	}
	
	public void render(Graphics g) {
		planet.drawCentered(x, y);
	}
	
	public void update(int delta) {
		timer += delta;
		
		if(timer > 500) {
			imgPos += 1;
			if(imgPos >= 32)
				imgPos = 0;
			try {
				Graphics g = planet.getGraphics();
				g.clear();
				g.drawImage(mask, 0, 0);
				g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
				g.drawImage(planetTexture, -imgPos, 0);
				g.drawImage(planetTexture, -imgPos + 32, 0);
				g.flush();
			} catch (SlickException e) {
				e.printStackTrace();
			}
			timer = 0;
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
