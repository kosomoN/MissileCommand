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
	private Color backColor, txtColor;
	private String text;
	private Image img;
	private boolean selected, pressed;
	
	public Button(float x, float y, float width, float height, String text, Color backColor, Color txtColor) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.backColor = backColor;
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
	
	public Button(Image img, float scale) {
		this.img = img;
		this.scale = scale;
	}
	
	public Button(String text, Color backColor, Color txtColor, int scale) {
		this.text = text;
		this.backColor = backColor;
		this.txtColor = txtColor;
		this.scale = scale;
	}
	
	public void renderWithoutBorder(Graphics g, Font font) {
		g.setColor(backColor);
		g.fillRect(x, y, width, height);
		
		g.setColor(txtColor);
		font.drawString(x + (width - font.getWidth(text)) / 2, y + (height - font.getHeight(text)) / 2, text);
	}
	
	public void render(Graphics g, Font font) {
		g.setColor(backColor);
		g.fillRect(x, y, width * scale, height * scale);
		g.setColor(Color.white);
		g.drawRect(x, y, width * scale, height * scale);
		
		g.setColor(txtColor);
		font.drawString(x + (width * scale - font.getWidth(text)) / 2, y + (height * scale - font.getHeight(text)) / 2, text);
	}
	
	public void renderImage(Graphics g) {
		img.draw(x, y, scale);
	}
	
	public void update(GameContainer container) {
		
	}
	
	public boolean hoverOver(float x, float y) {
		return Util.isInside(x, y, new Rectangle(getX(), getY(), width * scale, height * scale));
	}
	
	public boolean clicked(GameContainer container) {
		if(hoverOver(container.getInput().getMouseX(), container.getInput().getMouseY())) {
			if(container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
				pressed = true;
			}
			
			if(released(container)) {
				if(img != null)
					selected = true;
				return true;
			}
		}
		
		return false;
	}
	
	private boolean released(GameContainer container) {
		if(!container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
			if(pressed == true) {
				pressed = false;
				return true;
			} else {
				return false;
			}
		}
			
		return false;
	}
	
	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}
	
	public void setTextColor(Color txtColor) {
		this.txtColor = txtColor;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void setDimensions(float width, float height) {
		this.width = width;
		this.height = height;
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
	
	public Color getColor() {
		return backColor;
	}
	
	public Image getImage() {
		return img;
	}
	
	public void changeImage(Image newImage) {
		img = newImage;
	}
}
