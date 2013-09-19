package drok.missilecommand.utils;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class Util {
	/**
	 * 
	 * @param x The x coordinate to check
	 * @param y The y coordinate to check
	 * @param rect
	 * @return Returns true if the point(x, y) is inside the rectangle.
	 */
	
	public static boolean isInside(int x, int y, Rectangle rect) {
		return x > rect.getX() && x < rect.getX() + rect.getWidth() &&
				y > rect.getY() && y < rect.getY() + rect.getHeight();
	}
	
	public static void drawCenteredLines(Graphics g, Font font, String string, int firstLineMiddleX, int firstLineY) {
		g.setFont(font);
		for(String s : string.split("\n")) {
			g.drawString(s, firstLineMiddleX - font.getWidth(s) / 2, firstLineY);
			firstLineY += font.getLineHeight() + 10;
		}
	}
	
	public static void drawLines(Graphics g, Font font, String string, int firstLineX, int firstLineY) {
		g.setFont(font);
		for(String s : string.split("\n")) {
			g.drawString(s, firstLineX, firstLineY);
			firstLineY += font.getLineHeight() + 10;
		}
	}
	
	public static String addSpaces(int spaces) {
		String string = "";
		
		if(spaces < 0)
			spaces = 0;
		
		for(int i = 0; i < spaces; i++) {
			string += " ";
		}
		return string;
	}
}
