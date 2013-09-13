package drok.missilecommand.turret;

import org.newdawn.slick.Image;

public class Bullet {
	//Fields
	private float x, y;
	private float speed;
	private int direction;
	private Image bulletImage;
	
	public Bullet(float x, float y, float speed, int direction) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.direction = direction;
	}
	
	public static void init() {
		//Loading images and sounds
	}
	
	public void render() {
		bulletImage.draw(x, y);
	}
	
	public void update(int delta) {
		//Updates and logics
		x += Math.cos(Math.toRadians(direction)) * speed * delta;
		y += Math.sin(Math.toRadians(direction)) * speed * delta;
	}
}
