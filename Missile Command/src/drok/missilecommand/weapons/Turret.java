package drok.missilecommand.weapons;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import drok.missilecommand.Planet;
import drok.missilecommand.states.State;
import drok.missilecommand.states.game.GameState;
import drok.missilecommand.utils.ResourceManager;
public class Turret implements Weapon {
	//Fields
	private static Image satelliteImg;
	private Satellite[] satellites = new Satellite[5];
	private float firstSatelliteAngle = 90;
	private Planet planet;
	
	public Turret(Planet planet) {
		this.planet = planet;
		Vector2f vector = new Vector2f(90);
		vector.scale(15);
		satellites[0] = new Satellite(planet.getX() + vector.x, planet.getY() + vector.y, planet);
		vector.setTheta(90 + 72);
		satellites[1] = new Satellite(planet.getX() + vector.x, planet.getY() + vector.y, planet);
		vector.setTheta(90 + 72 + 72);
		satellites[2] = new Satellite(planet.getX() + vector.x, planet.getY() + vector.y, planet);
		vector.setTheta(90 + 72 + 72 + 72);
		satellites[3] = new Satellite(planet.getX() + vector.x, planet.getY() + vector.y, planet);
		vector.setTheta(90 + 72 + 72 + 72);
		satellites[4] = new Satellite(planet.getX() + vector.x, planet.getY() + vector.y, planet);
	}
	
	public void fire(GameState gs, int x, int y) {
		Satellite closestSatellite = getClosestSatellite(x, y);
		float angle = (float) Math.toDegrees(Math.atan2(y - closestSatellite.y, x - closestSatellite.x));
		//float[] otherSatellites = {satellites[(closestSatelliteIndex + 1) % 3].x, satellites[(closestSatelliteIndex + 1) % 3].y, satellites[(closestSatelliteIndex + 2) % 3].x, satellites[(closestSatelliteIndex + 2) % 3].y };
		//gs.addEntity(new Laser(closestSatellite.x, closestSatellite.y, otherSatellites , angle, gs));
		gs.addEntity(new Missile(closestSatellite.x, closestSatellite.y , angle, gs.getDebris()));
	}
	
	public void update(GameState gs, int delta) {
		firstSatelliteAngle += 0.002f * delta;
		Vector2f vector = new Vector2f(firstSatelliteAngle);
		vector.scale(15);
		satellites[0].setPosition(planet.getX() + vector.x, planet.getY() + vector.y);
		vector.setTheta(firstSatelliteAngle + 72);
		satellites[1].setPosition(planet.getX() + vector.x, planet.getY() + vector.y);
		vector.setTheta(firstSatelliteAngle + 72 + 72);
		satellites[2].setPosition(planet.getX() + vector.x, planet.getY() + vector.y);
		vector.setTheta(firstSatelliteAngle + 72 + 72 + 72);
		satellites[3].setPosition(planet.getX() + vector.x, planet.getY() + vector.y);
		vector.setTheta(firstSatelliteAngle + 72 + 72 + 72 + 72);
		satellites[4].setPosition(planet.getX() + vector.x, planet.getY() + vector.y);
	}
	
	private Satellite getClosestSatellite(float x, float y) {
		Satellite closestSatellite = satellites[0];
		float closestDistance = Math.abs(satellites[0].x - x) + Math.abs(satellites[0].y - y);
		for(int i = 1; i < satellites.length; i++) {
			if(Math.abs(satellites[i].x - x) + Math.abs(satellites[i].y - y) < closestDistance) {
				closestSatellite = satellites[i];
				closestDistance = Math.abs(satellites[i].x - x) + Math.abs(satellites[i].y - y);
			}
		}
		
		return closestSatellite;
	}
	
	public void render(GameContainer container) {
		Satellite closestSatellite = getClosestSatellite(container.getInput().getMouseX() / State.SCALE,
														container.getInput().getMouseY() / State.SCALE);
		for(Satellite s : satellites) {
			if(s == closestSatellite)
				s.render(null);
			else
				s.render(Color.gray);
		}
	}
	
	public static void init() throws SlickException {
		satelliteImg = ResourceManager.getImage("res/graphics/Satellite.png");
		satelliteImg.setFilter(Image.FILTER_NEAREST);
		satelliteImg = satelliteImg.getScaledCopy(4);
	}
	
	private class Satellite {
		private float x, y, angle;
		
		public Satellite(float x, float y, Planet planet) {
			this.x = x;
			this.y = y;
			this.angle = (float) Math.toDegrees(Math.atan2(y - planet.getY(), x - planet.getX())) + 90;
		}
		
		public void setPosition(float x, float y) {
			this.x = x;
			this.y = y;
			this.angle = (float) Math.toDegrees(Math.atan2(y - planet.getY(), x - planet.getX())) + 90;
		}

		private void render(Color color) {
			satelliteImg.setRotation(angle);
			if(color != null)
				satelliteImg.draw(x * State.SCALE - satelliteImg.getWidth() / 2, y * State.SCALE - satelliteImg.getHeight() / 2, color);
			else
				satelliteImg.drawCentered(x * State.SCALE, y * State.SCALE);
		}
	}
}
