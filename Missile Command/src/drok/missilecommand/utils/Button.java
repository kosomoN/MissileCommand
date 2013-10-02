package drok.missilecommand.utils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

public class Button {
	//Fields
	private float x, y, width, height, scale;
	private Color color, txtColor;
	private String text;
	private Image img;
	private boolean selected = false;
	
	public Button(float x, float y, float width, float height, String text, Color color, Color txtColor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.color = color;
		this.txtColor = txtColor;
		this.scale = 1;
	}
	
	public Button(float x, float y, float width, float height, Image img, float scale) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.img = img;
		this.scale = scale;
	}
	
	public void renderWithoutBorder(Graphics g, Font font) {
		g.setColor(txtColor);
		font.drawString(x, y, text);
	}
	
	public void render(Graphics g, Font font) {
		g.setColor(color);
		g.fillRect(x, y, width * scale, height * scale);
		g.setColor(Color.white);
		g.drawRect(x, y, width * scale, height * scale);
		
		g.setColor(txtColor);
		font.drawString(x + (width * scale - font.getWidth(text)) / 2, y + (height * scale - font.getHeight(text)) / 2, text);
	}
	
	public void render(Graphics g) {
		img.draw(x, y, scale);
	}
	
	public void update(float x, float y, GameContainer container) {
		clicked(x, y, container);
		if(selected) {
			changeImage(ResourceManager.getImage("GUIsheet.png").getSubImage(37, 17, 10, 10));
		} else {
			changeImage(ResourceManager.getImage("GUIsheet.png").getSubImage(27, 17, 10, 10));
		}
	}
	
	public boolean hoverOver(float x, float y) {
		return Util.isInside(x, y, new Rectangle(getX(), getY(), width * scale, height * scale));
	}
	
	public boolean clicked(float x, float y, GameContainer container) {
		if(hoverOver(x, y)) {
			if(container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				if(img != null)
					selected = true;
				return true;
			}
		}
		return false;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setTextColor(Color txtColor) {
		this.txtColor = txtColor;
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
		return width * scale;
	}
	
	public float getHeight() {
		return height * scale;
	}
	
	public Image getImage() {
		return img;
	}
	
	public void changeImage(Image newImage) {
		img = newImage;
	}
}
