package drok.missilecommand;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import drok.missilecommand.debris.Debris;
import drok.missilecommand.states.GameState;
import drok.missilecommand.utils.ResourceManager;

public class Planet {
	//Fields
	private static Image planet, planetTexture, mask;
	private int x, y;
	private int timer = 5000;
	private int imgPos;
	private Vector2f gravityVector = new Vector2f();
	private boolean isHit;
	
	private List<Part> parts = new ArrayList<Part>();
	private Color green = new Color(8, 57, 12), blue = new Color(2, 23, 82);

	public Planet(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public static void init() throws SlickException {
		planetTexture = ResourceManager.getImage("res/graphics/Planet Texture.png");
		planetTexture.setFilter(Image.FILTER_NEAREST);
		
		planet = new Image(16, 16);
		planet.setFilter(Image.FILTER_NEAREST);
		
		mask = ResourceManager.getImage("res/graphics/Planet Mask.png");
		mask.setFilter(Image.FILTER_NEAREST);
	}
	
	public void render(Graphics g) {
		if(!isHit)
			planet.drawCentered(x, y);
		else {
			for(int i = 0; i < parts.size(); i++) {
				parts.get(i).render(g);
			}
		}
	}
	
	public void update(GameState gs, int delta) {
		if(!isHit) {
			timer += delta;
			
			if(timer > 500) {
				imgPos += 1;
				if(imgPos >= 32)
					imgPos = 0;
				try {
					Graphics g = planet.getGraphics();
					g.clear();
					g.drawImage(mask, 0, 0);
					g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
					g.drawImage(planetTexture, -imgPos, 0);
					g.drawImage(planetTexture, -imgPos + 32, 0);
					g.flush();
				} catch (SlickException e) {
					e.printStackTrace();
				}
				timer = 0;
			}
		} else {
			for(Iterator<Part> it = parts.iterator(); it.hasNext();) {
				if(it.next().update(delta)) {
					it.remove();
				}
			}
			
			if(parts.isEmpty()) {
				gs.restart();
				isHit = false;
			}
		}
	}
	
	public void reset() {
		for(Part p : parts) {
			p.returnToInitialPos();
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public Vector2f getGravitationVector(float x, float y) {
		if(!isHit) {
			float dir = (float) Math.toDegrees(Math.atan2(this.y - y, this.x - x));
			gravityVector.x = (float) Math.cos(Math.toRadians(dir)) * 0.0002f;
			gravityVector.y = (float) Math.sin(Math.toRadians(dir)) * 0.0002f;
		} else {
			gravityVector.x = 0;
			gravityVector.y = 0;
		}
		return gravityVector;
	}
	
	public boolean isHit() {
		return isHit;
	}

	public void hit(Debris debris) {
		isHit = true;
		System.out.println("Hit");
		Color c;
		for(int i = 0; i < planet.getWidth(); i++) {
			for(int j = 0; j < planet.getHeight(); j++) {
				c = planet.getColor(i, j);
				if(c.a == 0)
					continue;
				
				if(c.b > c.g)
					c = blue;
				else
					c = green;
				
				parts.add(new Part(x + i - 8, y + j - 8, Math.random() * 360, 0.005f + Math.random() / 90f, c));
			}
		}
	}	
	
	private class Part {
		
		private float x, y, dx, dy;
		private Color color;
		private float initialPosX, initialPosY;
		private int ticksLeftToInitialPos = -1;
		
		public Part(float x, float y, double direction, double speed, Color color) {
			this.x = x;
			this.y = y;
			this.initialPosX = x;
			this.initialPosY = y;
			this.color = color;
			
			dx = (float) (Math.cos(Math.toRadians(direction)) * speed);
			dy = (float) (Math.sin(Math.toRadians(direction)) * speed);
		}

		private void render(Graphics g) {
			g.setColor(color);
			g.fillRect(x, y, 1, 1);
		}
		
		private boolean update(int delta) {
			x += dx * delta;
			y += dy * delta;
			
			if(ticksLeftToInitialPos != -1) {
				dx *= 1.01;
				dy *= 1.01;
				ticksLeftToInitialPos--;
				if(ticksLeftToInitialPos == 0) {
					return true;
				}
			}
			return false;
		}
		
		private void returnToInitialPos() {
			ticksLeftToInitialPos = 200;
			dx = (initialPosX - x) / 6300;
			dy = (initialPosY - y) / 6300;
		}
	}
}