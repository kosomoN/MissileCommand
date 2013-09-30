package drok.missilecommand.weapons;

import java.util.List;

import org.newdawn.slick.Image;

import drok.missilecommand.debris.Debris;
import drok.missilecommand.states.game.GameState;
import drok.missilecommand.upgrades.Upgrade;
import drok.missilecommand.utils.ResourceManager;

public class Probe implements Weapon, Upgrade {
	private static Image probeImg;
	
	private float x, y;
	private List<Debris> debris;
	private int timeSinceFire, fireRate = 5000;
	private GameState gs;
	private double angle;
	
	
	public Probe(float x, float y, GameState gs) {
		this.x = x;
		this.y = y;
		this.gs = gs;
		debris = gs.getDebris();
	}
	
	public Probe() {
		
	}
	
	public static void init() {
		probeImg = ResourceManager.getImage("res/graphics/Probe.png");
	}

	public boolean update(int delta) {
		angle += 0.05 * delta;
		x = (float) (gs.getPlanet().getX() - 30 * Math.cos(Math.toRadians(angle)));
		y = (float) (gs.getPlanet().getY() + 40 * Math.sin(Math.toRadians(angle)));
		
		timeSinceFire += delta;
		if(timeSinceFire >= fireRate) {
			if(!debris.isEmpty()) {
				Debris closestDebris = debris.get(0);
				float closestDebrisDistance = Math.abs(closestDebris.getX() - gs.getPlanet().getX()) + Math.abs(closestDebris.getY() - gs.getPlanet().getY());
				for(Debris deb : debris) {
					if(Math.abs(deb.getX() - gs.getPlanet().getX()) + Math.abs(deb.getY() - gs.getPlanet().getY()) < closestDebrisDistance) {
						closestDebris = deb;
					}
				}
				float angle = (float) Math.toDegrees(Math.atan2(closestDebris.getY() - y,  closestDebris.getX() - x));
				gs.addEntity(new Laser(x, y, new float[] {}, angle, gs));
				timeSinceFire -= fireRate;
			}
		}
		
		return false;
	}
	
	@Override
	public void render() {
		probeImg.drawCentered(x, y);
	}

	@Override
	public String getName() {
		return "Probe";
	}

	@Override
	public Image getImage() {
		return probeImg;
	}
	
	@Override
	public String getDescription() {
		return "A defense probe which will\nshoot asteroids for you.\nFire Rate: " + fireRate / 1000 + "s";
	}

	@Override
	public int getPrice() {
		return 10;
	}
}
