package drok.missilecommand.upgrades;

import org.newdawn.slick.Image;

public interface Upgrade {
	
	public String getName();
	public String getDescription();
	public Image getImage();
	public int getPrice();
}
