package drok.missilecommand.upgrades;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class Ware {
	protected int level = 1;
	protected String description;
	
	public void upgrade() {
		level++;
	}
	public abstract void render(Graphics g);
	public abstract boolean update(int delta);
	
	public String getDescription(Font font, int width) {
		StringBuffer sb = new StringBuffer();
		String[] strArray = description.split(" ");
		String line = "";
		for(String s : strArray) {
			if(font.getWidth(line + " " + s) > width) {
				sb.append(line + "\n");
				line = s + " ";
			} else {
				line += s + " ";
			}
		}
		sb.append(line);
		
		return sb.toString();
	}
	
	public abstract String getName();
	public abstract Image getImage();
	public abstract int getPrice();
	public abstract int getLevel();
}
