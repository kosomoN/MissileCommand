package drok.missilecommand.weapons;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

import drok.missilecommand.Planet;
import drok.missilecommand.debris.Debris;
import drok.missilecommand.states.State;
import drok.missilecommand.states.game.GameState;
import drok.missilecommand.upgrades.Ware;

public class Nuke extends Ware implements Weapon {
	//Fields
	private GameContainer container;
	private Image nukeImg;
	private Planet planet;
	private Circle c;
	private Color color;
	private boolean rendered, isOnScreen;
	private GameState gs;
	
	public Nuke(Planet planet, GameContainer container, GameState gs) {
		c = new Circle(planet.getX(), planet.getY(), planet.getRadius());
		this.container = container;
		this.gs = gs;
		this.planet = planet;
		isOnScreen = false;
		color = new Color(255, 115, 0); //200, 30
		description = "Do you really need an explanation? Destroys all debris";
		
		try {
			nukeImg = new Image((int) (planet.getRadius() * 2), (int) (planet.getRadius() * 2));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(Graphics g) {
		if(isOnScreen) {
			/*glColor3f(1.0f, 1.0f, 1.0f);
			
			int sections = 20;

			glEnable(GL_TRIANGLE_FAN);
				glVertex2f(planet.getX(), planet.getY());
				for(int i = 0; i < 4; i++) {
					glVertex2d(c.getRadius() * Math.cos(Math.PI * 2 * i / sections), c.getRadius() * Math.sin(Math.PI * 2 * i / sections));
				}
			glEnd();*/
			
			g.setColor(color);
			g.draw(c);
			if(!rendered)
				g.copyArea(nukeImg, (int) c.getX(), (int) c.getY());
		}
	}

	@Override
	public boolean update(int delta) {
		if(isOnScreen) {
			c.setRadius(c.getRadius() + 0.5f);
			c.setCenterX(planet.getX());
			c.setCenterY(planet.getY());
			isOnScreen = c.getRadius() * c.getRadius() * 4 > Math.pow(container.getWidth(), 2) + Math.pow(container.getHeight(), 2) / State.getScale();
		}
		return isOnScreen;
	}
	
	public boolean update(int delta, List<Debris> deb) {
		if(isOnScreen) {
			for(Debris d : deb) {
				if(Math.pow(c.getCenterX() - d.getX(), 2) + Math.pow(c.getCenterY() - d.getY(), 2) < Math.pow(c.getRadius() + d.getBoundingCircle().getRadius(), 2)) {
					d.hit(gs);
					return true;
				}
			}
		}
		return false;
	}

	public void setRadius(float radius) {
		c.setRadius(radius);
	}
	
	public void setIsOnScreen(boolean isOutOfScreen) {
		this.isOnScreen = isOutOfScreen;
	}
	
	public boolean isOnScreen() {
		return isOnScreen;
	}
	
	@Override
	public String getName() {
		return "Nuke";
	}

	@Override
	public Image getImage() {
		return nukeImg;
	}

	@Override
	public int getPrice() {
		return 10;
	}

	@Override
	public int getLevel() {
		return 1;
	}

	@Override
	public boolean isUpgradeable() {
		return false;
	}
}
