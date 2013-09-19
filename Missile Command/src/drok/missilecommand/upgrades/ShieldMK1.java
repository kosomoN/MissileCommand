package drok.missilecommand.upgrades;

import org.newdawn.slick.Image;

public class ShieldMK1 extends Shield {
	//Fields
	private static int durability = 3;
	
	public ShieldMK1(int centerx, int centery, Image shieldImg) {
		super(centerx, centery, durability, shieldImg);
	}
	
	public ShieldMK1(int durability) {
		super(durability);
	}

	@Override
	public void render() {
		if(!isDestroyed)
			shieldImg.drawCentered(x, y);
	}
	
	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	@Override
	public String getName() {
		return "Shield MKI";
	}
	
	@Override
	public Image getImage() {
		return shieldImg;
	}

	@Override
	public String getDescription() {
		return "A basic shield.\nProtects from 3 hits";
	}

	@Override
	public int getPrice() {
		return 10;
	}
}
