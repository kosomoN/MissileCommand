package drok.missilecommand.weapons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import drok.missilecommand.Planet;
import drok.missilecommand.debris.Debris;
import drok.missilecommand.states.State;
import drok.missilecommand.states.game.GameState;
import drok.missilecommand.utils.ResourceManager;
public class Turret implements Weapon {
	//Fields
	private static Image satelliteImg;
	private List<Satellite> satellites = new ArrayList<Satellite>();
	private float firstSatelliteAngle = 90;
	private Planet planet;
	private int turretCount;
	
	public Turret(Planet planet, int turrentCount) {
		this.planet = planet;
		this.turretCount = turrentCount;
		resetTurrets();
	}
	
	public void resetTurrets() {
		Vector2f vector = new Vector2f(90);
		vector.scale(15);
		satellites.clear();
		for(int i = 0; i < turretCount; i++) {
			satellites.add(new Satellite(planet.getX() + vector.x, planet.getY() + vector.y, planet, i));
			vector.setTheta(vector.getTheta() + 360f / turretCount);
		}
	}
	
	public void fire(GameState gs, int x, int y) {
		Satellite closestSatellite = getClosestSatellite(x, y);
		if(closestSatellite != null) {
			float angle = (float) Math.toDegrees(Math.atan2(y - closestSatellite.y, x - closestSatellite.x));
			//float[] otherSatellites = {satellites[(closestSatelliteIndex + 1) % 3].x, satellites[(closestSatelliteIndex + 1) % 3].y, satellites[(closestSatelliteIndex + 2) % 3].x, satellites[(closestSatelliteIndex + 2) % 3].y };
			//gs.addEntity(new Laser(closestSatellite.x, closestSatellite.y, otherSatellites , angle, gs));
			gs.addEntity(new Missile(closestSatellite.x, closestSatellite.y , angle, gs.getDebris()));
		}

	}
	
	public void update(GameState gs, int delta) {
		firstSatelliteAngle += 0.002f * delta;
		Vector2f vector = new Vector2f(firstSatelliteAngle);
		vector.scale(15);
		for(Satellite sat : satellites) {
			vector.setTheta(firstSatelliteAngle + 360f / turretCount * sat.getIndex());
			sat.setPosition(planet.getX() + vector.x, planet.getY() + vector.y);
		}
		
		for(Debris debris : gs.getDebris()) {
			for(Iterator<Satellite> iterator = satellites.iterator(); iterator.hasNext();) {
				if(debris.getBoundingCircle().intersects(iterator.next().getBoundingCircle())) {
					debris.hit(gs, null);
					iterator.remove();
					break;
				}
			}
		}
	}
	
	private Satellite getClosestSatellite(float x, float y) {
		Satellite closestSatellite = null;
		float closestDistance = 100000;
		for(Satellite sat : satellites) {
			if(Math.abs(sat.x - x) + Math.abs(sat.y - y) < closestDistance) {
				closestSatellite = sat;
				closestDistance = Math.abs(sat.x - x) + Math.abs(sat.y - y);
			}
		}
		
		return closestSatellite;
	}
	
	public void render(GameContainer container) {
		Satellite closestSatellite = getClosestSatellite(container.getInput().getMouseX() / State.SCALE,
														container.getInput().getMouseY() / State.SCALE);
		for(Satellite s : satellites) {
			if(s == closestSatellite)
				s.render(null, container);
			else
				s.render(Color.gray, container);
		}
	}
	
	public static void init() throws SlickException {
		satelliteImg = ResourceManager.getImage("Satellite.png");
		satelliteImg.setFilter(Image.FILTER_NEAREST);
		satelliteImg = satelliteImg.getScaledCopy(State.SCALE * 2 / 3);
	}
	
	private class Satellite {
		private float x, y, angle;
		private Circle boundingCircle;
		private int turrentIndex;
		
		public Satellite(float x, float y, Planet planet, int turretIndex) {
			this.x = x;
			this.y = y;
			this.turrentIndex = turretIndex;
			this.angle = (float) Math.toDegrees(Math.atan2(y - planet.getY(), x - planet.getX())) + 90;
			boundingCircle = new Circle(x, y, 3);
		}
		
		public float getIndex() {
			return turrentIndex;
		}

		public Circle getBoundingCircle() {
			boundingCircle.setCenterX(x);
			boundingCircle.setCenterY(y);
			return boundingCircle;
		}

		public void setPosition(float x, float y) {
			this.x = x;
			this.y = y;
			this.angle = (float) Math.toDegrees(Math.atan2(y - planet.getY(), x - planet.getX())) + 90;
		}

		private void render(Color color, GameContainer container) {
			satelliteImg.setRotation(angle);
			if(color != null)
				satelliteImg.draw(x * State.SCALE - satelliteImg.getWidth() / 2, y * State.SCALE - satelliteImg.getHeight() / 2, color);
			else
				satelliteImg.drawCentered(x * State.SCALE, y * State.SCALE);
		}
	}
}
