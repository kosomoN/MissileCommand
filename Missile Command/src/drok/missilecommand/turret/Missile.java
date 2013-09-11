package drok.missilecommand.turret;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import drok.missilecommand.Entity;
import drok.missilecommand.debris.Debris;

public class Missile implements Entity {
	
	private static Image missileImage;
	protected List<Debris> debris;
	private float x, y, direction;
	private float tailX, tailY;
	private int timer;
	private boolean isHit;
	private Color tailColor = new Color(0.75f, 0.75f, 0.75f, 1f);
	
	public Missile(float x, float y, float direction, List<Debris> debris) {
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.debris = debris;
		
		tailX = x;
		tailY = y;
	}

	public static void init() throws SlickException {
		missileImage = new Image("res/graphics/Missile.png");
		missileImage.setFilter(Image.FILTER_NEAREST);
	}
	
	@Override
	public void render(Graphics g) {
		g.drawGradientLine(x, y, tailColor, tailX, tailY, Color.transparent);
		/*if(!isHit) {
			missileImage.setRotation(direction + 90);
			missileImage.drawCentered(x, y);
		}*/
	}

	@Override
	public boolean update(int delta) {
		float dx = (float) (Math.cos(Math.toRadians(direction)) * 0.1 * delta);
		float dy = (float) (Math.sin(Math.toRadians(direction)) * 0.1 * delta);
		if(timer > 500 || isHit) {
			if(isHit) {
				dx *= 2;
				dy *= 2;
				tailColor.a -= 0.003f * delta;
			}
			tailX += dx;
			tailY += dy;
			if(Math.abs(x - tailX) < 2 && Math.abs(x - tailX) < 2) {
				return true;
			}
		} else {
			timer += delta;
		}
		if(!isHit) {
			x += dx;
			y += dy;
		}
		
		for(Debris deb : debris) {
			if(deb.getX() - 4 < x && deb.getX() + 4 > x && deb.getY() - 4 < y && deb.getY() + 4 > y) {
				deb.hit();
				isHit = true;
			}
		}
		return false;
	}

}
