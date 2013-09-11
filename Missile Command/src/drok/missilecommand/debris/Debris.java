package drok.missilecommand.debris;

import drok.missilecommand.Entity;

public abstract class Debris implements Entity {
	
	protected float x, y;
	protected float speed, direction, rotation = (float) (Math.random() * 360);
	private boolean isHit = false;
	
	public Debris(float x, float y, float speed, float direction) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.direction = direction;
	}
	
	public boolean update(int delta) {
		if(isHit)
			return true;
		rotation += 0.01 * delta;
		x += Math.cos(Math.toRadians(direction)) * speed * delta;
		y += Math.sin(Math.toRadians(direction)) * speed * delta;
		return false;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void hit() {
		isHit  = true;
	}
}
