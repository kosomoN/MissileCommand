package drok.missilecommand.debris;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import drok.missilecommand.Planet;
import drok.missilecommand.states.State;
import drok.missilecommand.states.game.GameState;
import drok.missilecommand.utils.ResourceManager;

public class HeavyFighter extends Debris {

	private static Image fighterImg;

	public HeavyFighter(float x, float y, float speed, float direction, Planet planet) {
		super(x, y, speed, direction, 5, planet);
	}
	
	@Override
	public boolean update(GameState gs, int delta) {
		super.update(gs, delta);
		rotation = (float) Math.toDegrees(Math.atan2(y - planet.getY(), x - planet.getX())) + 90;
		return isHit;
	}
	
	@Override
	public void render(Graphics g) {
		fighterImg.setRotation(rotation);
		fighterImg.drawCentered(x * State.SCALE, y * State.SCALE);
	}

	@Override
	public int getScore() {
		return 50;
	}

	@Override
	public boolean renderScaled() {
		return false;
	}
	
	public static void init() {
		fighterImg = ResourceManager.getImage("Heavy Fighter.png").getScaledCopy(2);
	}
}
