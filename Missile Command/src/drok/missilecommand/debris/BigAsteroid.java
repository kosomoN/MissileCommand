package drok.missilecommand.debris;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import drok.missilecommand.Entity;
import drok.missilecommand.Planet;
import drok.missilecommand.states.game.GameState;
import drok.missilecommand.utils.ResourceManager;
import drok.missilecommand.weapons.Missile;

public class BigAsteroid extends Debris {

	private static Image img;

	public BigAsteroid(float x, float y, float speed, float direction, Planet planet) {
		super(x, y, speed, direction, 8, planet);
	}

	@Override
	public void render(Graphics g) {
		img.setRotation((int) rotation);
		img.drawCentered(x, y);
	}

	@Override
	public boolean update(GameState gs, int delta) {
		if(isHit)
			return true;
		return super.update(gs, delta);
		
	}

	@Override
	public int getScore() {
		return 0;
	}
	
	public static void init() throws SlickException {
		img = ResourceManager.getImage("Big Asteroid.png");
		img.setFilter(Image.FILTER_NEAREST);
	}
	
	@Override
	public void hit(GameState gs, Entity hitBy) {
		super.hit(gs, hitBy);
		if(hitBy instanceof Missile) {
			Missile miss = (Missile) hitBy;
			vector.x += miss.getDX();
			vector.y += miss.getDY();
			Asteroid ast = new Asteroid(x, y, vector.length(), (float) vector.getTheta(), gs.getPlanet());
			for(int i = 0; i < 10; i++) {
				gs.addEntity(ast.new Part(x, y, vector.x, vector.y, Math.random() * 360, 0.015f + Math.random() / 150f));
			}
			gs.addEntity(ast);
		}
	}

	@Override
	public boolean renderScaled() {
		return true;
	}
}
