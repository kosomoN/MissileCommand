package drok.missilecommand.debris;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import drok.missilecommand.Entity;
import drok.missilecommand.Planet;
import drok.missilecommand.states.game.GameState;
import drok.missilecommand.utils.ResourceManager;

public class Asteroid extends Debris {

	protected static Image asteroidImage;
	
	public Asteroid(float x, float y, float speed, float direction, Planet planet) {
		super(x, y, speed, direction, 4, planet);
	}

	public static void init() throws SlickException {
		asteroidImage = ResourceManager.getImage("res/graphics/Asteroid.png");
	}
	
	@Override
	public void render(Graphics g) {
		asteroidImage.setRotation((int) rotation);
		asteroidImage.drawCentered(x, y);
	}
	
	@Override
	public boolean update(GameState gs, int delta) {
		super.update(gs, delta);
		return isHit;
	}
	
	@Override
	public void hit(GameState gs) {
		super.hit(gs);
		for(int i = 0; i < 20; i++) {
			gs.addEntity(new Part(x, y, vector.x, vector.y, Math.random() * 360, 0.015f + Math.random() / 150f));
		}
	}
	
	private class Part implements Entity {
		
		private float x, y, dx, dy;
		
		public Part(float x, float y, float asteroidDx, float asteroidDy, double direction, double speed) {
			this.x = x;
			this.y = y;
			
			dx = asteroidDx + (float) (Math.cos(Math.toRadians(direction)) * speed);
			dy = asteroidDy + (float) (Math.sin(Math.toRadians(direction)) * speed);
		}
		
		@Override
		public void render(Graphics g) {
			g.setColor(Color.darkGray);
			g.fillRect(x, y, 1, 1);
		}
		
		@Override
		public boolean update(GameState gs, int delta) {
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

		@Override
		public boolean renderScaled() {
			return true;
		}
	}

	@Override
	public int getScore() {
		return 10;
	}

	@Override
	public boolean renderScaled() {
		return true;
	}
}