package drok.missilecommand.turret;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;

public class Turret {
	//Fields
	private static Image turret;
	private int x, y;
	private float angle;
	
	public Turret() {
		
	}
	
	public void render() {
		turret.draw(x, y);
	}
	
	public void update() {
		
	}
	
	private void calculateAngle(GameContainer container) {
		angle = (float) Math.toDegrees(Math.atan2(container.getInput().getMouseY() - y, container.getInput().getMouseX() - x));
	}
	
	//Getters
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public float getAngle() {
		return angle;
	}
}
