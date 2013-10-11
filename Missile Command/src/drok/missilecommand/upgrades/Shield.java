package drok.missilecommand.upgrades;

import java.util.Iterator;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import drok.missilecommand.Planet;
import drok.missilecommand.debris.Debris;
import drok.missilecommand.states.game.GameState;

public class Shield extends Ware {
	//Fields
	private GameState gs;
	private Image shieldImg;
	private float x, y;
	private boolean isDestroyed;
	private int durability;
	private String name;
	private Color color = new Color(1f, 1f, 1f, 1f);
	
	public Shield(Planet planet, int durability, Image img, GameState gs) {
		this.x = planet.getX();
		this.y = planet.getY();
		this.durability = durability;
		this.gs = gs;
		
		shieldImg = img;
		isDestroyed = false;
		description = "Protects from " + durability + (durability == 1 ? " hit" : "hits");
		name = "ShieldMK" + durability;
	}
	
	public void render(Graphics g) {
		if(color.a > 0) {
			shieldImg.draw(x - shieldImg.getWidth() / 2, y - shieldImg.getHeight() / 2, color);
		}
	}
	
	public void hit() {
		durability--;
	}
	
	public boolean isDestroyed() {
		return isDestroyed;
	}

	@Override
	public boolean update(int delta) {
		if(isDestroyed) {
			color.a -= 0.02f;
		}
		for(Iterator<Debris> debris = gs.getDebris().iterator(); debris.hasNext();) {
			Debris deb = debris.next();
			if(!isDestroyed && (deb.getX() - x) * (deb.getX() - x) + (deb.getY() - y) * (deb.getY() - y) < Math.pow(shieldImg.getWidth() / 2 + deb.getBoundingCircle().getRadius(), 2)) {
				deb.hit(gs, null);
				hit();
				if(durability <= 0) {
					isDestroyed = true;
				}
			}
		}
		return isDestroyed;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Image getImage() {
		return shieldImg;
	}

	@Override
	public int getPrice() {
		return 0;
	}

	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public boolean isUpgradeable() {
		return durability < 3;
	}
}