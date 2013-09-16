package drok.missilecommand.debris;

import org.newdawn.slick.geom.Vector2f;

import drok.missilecommand.Entity;
import drok.missilecommand.Planet;
import drok.missilecommand.states.game.GameState;

public abstract class Debris implements Entity {
	
	protected float x, y;
	protected float rotation = (float) (Math.random() * 360);
	protected Vector2f vector;
	protected boolean isHit = false;
	protected Planet planet;
	private int radiusPlusPlanetRadiusSquared;
	
	public Debris(float x, float y, float speed, float direction, int circleHitboxRadius, Planet planet) {
		this.x = x;
		this.y = y;
		this.planet = planet;
		this.radiusPlusPlanetRadiusSquared = (circleHitboxRadius + 8) * (circleHitboxRadius + 8);
		
		vector = new Vector2f((float) Math.cos(Math.toRadians(direction)) * speed,
								(float) Math.sin(Math.toRadians(direction)) * speed);
	}
	
	@Override
	public boolean update(GameState gs, int delta) {
		rotation += 0.1;
		
		vector.add(planet.getGravitationVector(x, y));
		if(!planet.isHit())
			vector.scale(0.999f);
		
		x += vector.x * delta;
		y += vector.y * delta;
		
		if(!isHit && (planet.getX() - x) * (planet.getX() - x) + (planet.getY() - y) * (planet.getY() - y) < radiusPlusPlanetRadiusSquared && !planet.isHit()) {
			gs.planetHit(this);
			hit();
		}
		return false;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public void hit() {
		isHit  = true;
	}

	public boolean isHit() {
		return isHit;
	}

	public abstract int getScore();
}