package drok.missilecommand;

import org.newdawn.slick.Graphics;

public interface Entity {
	public void render(Graphics g);
	public boolean update(int delta);
}
