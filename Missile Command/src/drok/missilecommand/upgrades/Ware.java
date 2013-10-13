package drok.missilecommand.upgrades;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import drok.missilecommand.Planet;
import drok.missilecommand.states.game.GameState;

public abstract class Ware {
	protected int level = 1;
	protected int price = 0;
	protected String description = "";
	protected boolean isUpgradeable;
	protected boolean maxUpgraded;
	protected GameState gs;
	
	public void upgrade() {
		level++;
	}
	
	public abstract void refresh();
	public abstract void init(Planet planet, GameState gs);
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
	public int getLevel() {
		return level;
	}
	public abstract float getX();
	public abstract float getY();
	public abstract boolean isUpgradeable();
	public abstract boolean isMaxUpgraded();
	
	public void setDescription(String string) {
		description = string;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	public void setIsUpgradeable(boolean upgrade) {
		isUpgradeable = upgrade;
	}
	
	public void setIsMaxUpgraded(boolean maxed) {
		maxUpgraded = maxed;
	}
	
	public void setGameState(GameState gs) {
		this.gs = gs;
	}
}
