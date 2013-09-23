package drok.missilecommand.utils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

public class Button {
	//Fields
	private float x, y, width, height;
	private Color color;
	private String text;
	private boolean selected = false;
	
	public Button(float x, float y, float width, float height, String text, Color color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.color = color;
	}
	
	public void render(Graphics g, Font font) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
		g.setColor(Color.white);
		g.drawRect(x, y, width, height);
		
		g.setColor(Color.white);
		font.drawString(x + (width - font.getWidth(text)) / 2, y + (height - font.getHeight(text)) / 2, text);
	}
	
	public void update() {
		
	}
	
	public boolean hooverOver(int x, int y) {
		return Util.isInside(x, y, new Rectangle(getX(), getY(), width, height));
	}
	
	public boolean clicked(int x, int y, GameContainer container) {
		if(hooverOver(x, y))
			if(container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				return true;
			}
			
		return false;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public String getText() {
		return text;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
}
