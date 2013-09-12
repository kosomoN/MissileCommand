package drok.missilecommand.debris;

import org.newdawn.slick.geom.Vector2f;

import drok.missilecommand.Entity;
import drok.missilecommand.Planet;
import drok.missilecommand.states.GameState;

public abstract class Debris implements Entity {
	
	protected float x, y;
	protected float rotation = (float) (Math.random() * 360);
	protected Vector2f vector;
	protected boolean isHit = false;
	protected Planet planet;
	
	public Debris(float x, float y, float speed, float direction, Planet planet) {
		this.x = x;
		this.y = y;
		this.planet = planet;
		
		vector = new Vector2f((float) Math.cos(Math.toRadians(direction)) * speed,
								(float) Math.sin(Math.toRadians(direction)) * speed);
	}
	
	@Override
	public boolean update(GameState gs, int delta) {
		rotation += 0.01 * delta;
		
		vector.add(planet.getGravitationVector(x, y));
		
		x += vector.x * delta;
		y += vector.y * delta;
		//x += Math.cos(Math.toRadians(direction)) * speed * delta;
		//y += Math.sin(Math.toRadians(direction)) * speed * delta;
		
		if(!isHit && x + 4 > planet.getX() - 8 && x - 4 < planet.getX() + 8 && y + 4 > planet.getY() - 8 && y - 4 < planet.getY() + 8) {
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
}