package drok.missilecommand;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Planet {
	//Fields
	private static Image planet, planetTexture, mask;
	private int x, y;
	private int timer = 5000;
	private int imgPos;
	private Vector2f gravityVector = new Vector2f();
	
	public Planet(int x, int y) {
		this.x = x;
		this.y = y;
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

	public Vector2f getGravitationVector(float x, float y) {
		float dir = (float) Math.toDegrees(Math.atan2(this.y - y, this.x - x));
		gravityVector.x = (float) Math.cos(Math.toRadians(dir)) * 0.0002f;
		gravityVector.y = (float) Math.sin(Math.toRadians(dir)) * 0.0002f;
		return gravityVector;
	}
}