package drok.missilecommand.upgrades;

import org.newdawn.slick.Image;

public class ShieldMK2 extends Shield {
	//Fields
	private static int durability = 5;
	
	public ShieldMK2(int x, int y, Image img) {
		super(x, y, durability, img);
	}

	@Override
	public void render() {
		if(!isDestroyed)
			shieldImg.drawCentered(x, y);
	}
	
	@Override
	public String getName() {
		return "Shield MKII";
	}
	
	@Override
	public Image getImage() {
		return shieldImg;
	}

	@Override
	public String getDescription() {
		return "A good shield.\nProtects from 5 hits";
	}

	@Override
	public int getPrice() {
		return 0;
	}
}