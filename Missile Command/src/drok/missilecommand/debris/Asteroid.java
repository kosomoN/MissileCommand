package drok.missilecommand.debris;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import drok.missilecommand.Planet;
import drok.missilecommand.states.GameState;

public class Asteroid extends Debris {

	protected static Image asteroidImage;
	private List<Part> parts = new ArrayList<Part>();
	
	public Asteroid(float x, float y, float speed, float direction, Planet planet) {
		super(x, y, speed, direction, 4, planet);
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
		super(x, y, speed, (float) Math.toDegrees(Math.atan2(planet.getY() - y, planet.getX() - x)), 4, planet);
	}

	public static void init() throws SlickException {
		asteroidImage = new Image("res/graphics/Asteroid.png");
		asteroidImage.setFilter(Image.FILTER_NEAREST);
	}
	
	@Override
	public void render(Graphics g) {
		if(!isHit) {
			asteroidImage.setRotation((int) rotation);
			asteroidImage.drawCentered(x, y);
		} else {
			g.setColor(Color.darkGray);
			for(Part p : parts)
				p.render(g);
		}
	}
	
	@Override
	public boolean update(GameState gs, int delta) {
		super.update(gs, delta);
		if(isHit) {
			for(Iterator<Part> it = parts.iterator(); it.hasNext();) {
				if(it.next().update(gs, delta)) {
					it.remove();
				}
			}
			
			return parts.isEmpty();
		}
		return false;
	}
	
	@Override
	public void hit() {
		super.hit();
		for(int i = 0; i < 20; i++) {
			parts.add(new Part(x, y, vector.x, vector.y, Math.random() * 360, 0.015f + Math.random() / 150f));
		}
	}
	
	private class Part {
		
		private float x, y, dx, dy;
		
		public Part(float x, float y, float asteroidDx, float asteroidDy, double direction, double speed) {
			this.x = x;
			this.y = y;
			
			dx = asteroidDx + (float) (Math.cos(Math.toRadians(direction)) * speed);
			dy = asteroidDy + (float) (Math.sin(Math.toRadians(direction)) * speed);
		}

		private void render(Graphics g) {
			g.fillRect(x, y, 1, 1);
		}
		
		private boolean update(GameState gs, int delta) {
			Vector2f vector = planet.getGravitationVector(x, y);
			dx += vector.x;
			dy += vector.y;
			if(!planet.isHit()) {
				dx *= 0.999f;
				dy *= 0.999f;
			}
			x += dx * delta;
			y += dy * delta;
			if((planet.getX() - x) * (planet.getX() - x) + (planet.getY() - y) * (planet.getY() - y) < 81 && !planet.isHit()) {//9 * 9
				gs.smallHit();
				return true;
			}
			return false;
		}
	}

	@Override
	public int getScore() {
		return 10;
	}
}