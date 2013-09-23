package drok.missilecommand.effects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class DebrisTrail {
	private static Image image;
	private float x, y, scale = 8;
	
	public DebrisTrail(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void render() {
		image.draw(x - image.getWidth() / 2 * scale, y - image.getHeight() / 2 * scale, scale, new Color(1, 1, 1, scale / 8));
	}
	
	public boolean update(int delta) {
		scale -= 0.01 * delta;
		if(scale <= 0) {
			return true;
		}
		return false;
	}
	
	public static void init() throws SlickException {
		image = new Image("res/graphics/DebrisTrail.png");
		image.setFilter(Image.FILTER_NEAREST);
	}
}
