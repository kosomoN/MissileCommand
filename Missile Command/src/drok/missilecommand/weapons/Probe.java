package drok.missilecommand.weapons;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import drok.missilecommand.debris.Debris;
import drok.missilecommand.states.game.GameState;
import drok.missilecommand.upgrades.Ware;
import drok.missilecommand.utils.ResourceManager;

public class Probe extends Ware implements Weapon {
	private float x, y;
	private List<Debris> debris;
	private int timeSinceFire;
	private GameState gs;
	private double angle;
	
	private static int fireRate;
	private static Image probeImg;
	
	public Probe(float x, float y, GameState gs, int fireRate) {
		this.x = x;
		this.y = y;
		this.gs = gs;
		debris = gs.getDebris();
		Probe.fireRate = fireRate;
		description = "A defense probe which will shoot asteroids for you. Fire Rate: " + fireRate / 1000 + "s";
	}
	
	public static void init() {
		probeImg = ResourceManager.getImage("Probe.png");
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
	public void render(Graphics g) {
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
	public int getPrice() {
		return 10;
	}

	@Override
	public int getLevel() {
		return 1;
	}
}
