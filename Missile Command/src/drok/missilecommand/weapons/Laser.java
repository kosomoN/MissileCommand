package drok.missilecommand.weapons;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;

import drok.missilecommand.Entity;
import drok.missilecommand.debris.Debris;
import drok.missilecommand.states.game.GameState;

public class Laser implements Entity {
	
	private float startPosx, startPosy, endPosx, endPosy;
	private int timer;
	private Color color = new Color(1, 0, 0, 1f);
	private float[] otherLines;
	
	public Laser(float x, float y, float[] otherLines, float direction, GameState gs) {
		this.otherLines = otherLines;
		startPosx = x;
		startPosy = y;
		
		endPosx = (float) (x + Math.cos(Math.toRadians(direction)) * 500);
		endPosy = (float) (y + Math.sin(Math.toRadians(direction)) * 500);
		
		Line laserLine = new Line(startPosx, startPosy, endPosx, endPosy);
		
		for(Debris deb : gs.getDebris()) {
			if(!deb.isHit() && laserLine.intersects(deb.getBoundingCircle())) {
				gs.debrisDestroyed(deb);
				deb.hit(gs, this);
			}
		}
	}

	@Override
	public void render(Graphics g) {
		g.setColor(color);
		g.drawLine(startPosx, startPosy, endPosx, endPosy);
		for(int i = 0; i < otherLines.length / 2; i++) {
			g.drawLine(startPosx, startPosy, otherLines[i * 2], otherLines[i * 2 + 1]);
		}
	}

	@Override
	public boolean update(GameState gs, int delta) {
		timer += delta;
		if(timer > 500)
			return true;
		
		color.a = 1f - (timer / 500f) * (timer / 500f);
		return false;
	}

	@Override
	public boolean renderScaled() {
		return true;
	}
}