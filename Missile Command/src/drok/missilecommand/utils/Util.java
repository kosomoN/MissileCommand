package drok.missilecommand.utils;

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
}
