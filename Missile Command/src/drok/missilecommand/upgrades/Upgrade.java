package drok.missilecommand.upgrades;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public interface Upgrade {
	public void render(Graphics g);
	public boolean update(int delta);
	public String getName();
	public String getDescription();
	public Image getImage();
	public int getPrice();
}
