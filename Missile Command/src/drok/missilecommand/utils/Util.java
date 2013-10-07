package drok.missilecommand.utils;

import org.newdawn.slick.Color;
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
	
	public static boolean isInside(float x, float y, Rectangle rect) {
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
	
	/**
	 * Writes a line of text  with scrambled character at end
	 * @param str - The string to be rendered
	 * @param g - The graphics context
	 * @param alpha - The intensity of the color with which the text will be rendered
	 * @param x - The x coordinate for where to draw
	 * @param txtY - The y coordinate for where to draw
	 * @param y - The y coordinate for where the texts lowest part is
	 * @param timer - The time the writing should take
	 * @return Returns true when ready writing
	 */
	public static boolean renderTextLineWithRandomCharAtEnd(String str, Graphics g, float alpha, float x, float txtY, float y, float timer) {
		boolean ready = false;
		g.setColor(new Color(1f, 1f, 1f, alpha));
		if(timer - ((txtY - y) / 15) >= 0 ) {
			if((timer - ((txtY - y) / 15) < str.length())) {
				g.drawString(str.substring(0, (int) ((int) timer - ((txtY - y) / 15))) + (char)(Math.random() * 26 + 'a'), x, txtY);
				ready =  false;
			} else {
				g.drawString(str, x, txtY);
				ready = true;
			}
		}
		
		return ready;
	}
}
