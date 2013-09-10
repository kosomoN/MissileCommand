package drok.missilecommand.debris;

public abstract class Debris {
	//Field
	protected float x, y;
	protected float speed, direction, rotation;
	
	public Debris(int x, int y, float speed, float direction) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.direction = direction;
	}
	
	public abstract void render();
	
	public void update(int delta) {
		rotation += 0.01 * delta;
		x += Math.cos(Math.toRadians(direction)) * speed * delta;
		y += Math.sin(Math.toRadians(direction)) * speed * delta;
	}
	
	//Getters
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	public float getSpeed() {
		return speed;
	}
}
