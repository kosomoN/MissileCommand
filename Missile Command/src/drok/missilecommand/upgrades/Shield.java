package drok.missilecommand.upgrades;

import java.util.Iterator;

import org.newdawn.slick.Image;

import drok.missilecommand.debris.Debris;
import drok.missilecommand.states.game.GameState;

public abstract class Shield implements Upgrade {
	//Fields
	protected int x, y;
	protected boolean isDestroyed;
	protected Image shieldImg;
	protected int durability;
	
	public Shield(int x, int y, int durability, Image img) {
		this.x = x;
		this.y = y;
		this.durability = durability;
		shieldImg = img;
		isDestroyed = false;
	}
	
	public Shield(int durability) {
		
	}
	
	public abstract void render();
	
	public boolean update(GameState gs) {
		for(Iterator<Debris> debris = gs.getDebris().iterator(); debris.hasNext();) {
			Debris deb = debris.next();
			if(!isDestroyed && (deb.getX() - x) * (deb.getX() - x) + (deb.getY() - y) * (deb.getY() - y) < Math.pow(shieldImg.getWidth() / 2 + deb.getBoundingCircle().getRadius(), 2)) {
				deb.hit(gs);
				hit();
				if(durability <= 0) {
					isDestroyed = true;
					return true;
				}
			}
		}
		return false;
	}
	
	public void hit() {
		durability--;
	}
	
	public boolean isDestroyed() {
		return isDestroyed;
	}
}