package drok.missilecommand.weapons;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import drok.missilecommand.Entity;
import drok.missilecommand.debris.Debris;
import drok.missilecommand.states.game.GameState;

public class Missile implements Entity {
	
	protected List<Debris> debris;
	private float x, y, dx, dy;
	private float tailX, tailY;
	private int timer;
	private boolean isHit;
	private Color tailColor = new Color(0.75f, 0.75f, 0.75f, 1f);
	
	public Missile(float x, float y, float direction, List<Debris> debris) {
		this.x = x;
		this.y = y;
		this.debris = debris;
		
		tailX = x;
		tailY = y;
		
		dx = (float) (Math.cos(Math.toRadians(direction)) * 0.1);
		dy = (float) (Math.sin(Math.toRadians(direction)) * 0.1);
	}

	@Override
	public void render(Graphics g) {
		g.drawGradientLine(x, y, tailColor, tailX, tailY, Color.transparent);
	}

	@Override
	public boolean update(GameState gs, int delta) {
		if(timer > 500 || isHit) {
			if(isHit) {
				tailColor.a -= 0.003f * delta;
				if(Math.abs(x - tailX) < 2 && Math.abs(x - tailX) < 2) {
					return true;
				}
			}
			tailX += dx * delta;
			tailY += dy * delta;

		} else {
			timer += delta;
		}
		
		if(!isHit) {
			x += dx * delta;
			y += dy * delta;
			
			for(Debris deb : debris) {
				if(!deb.isHit() && deb.getX() - 4 < x && deb.getX() + 4 > x && deb.getY() - 4 < y && deb.getY() + 4 > y) {
					deb.hit(gs);
					gs.debrisDestroyed(deb);
					isHit = true;
				}
			}
			if(!gs.inBounds(x, y)) {
				isHit = true;
			}
		}
		return false;
	}
}