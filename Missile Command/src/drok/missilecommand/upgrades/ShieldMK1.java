package drok.missilecommand.upgrades;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import drok.missilecommand.states.game.GameState;

public class ShieldMK1 extends Shield {
	//Fields
	private static int durability = 3;
	private Color color = new Color(1, 1, 1, 1f);
	
	public ShieldMK1(int centerx, int centery, Image shieldImg) {
		super(centerx, centery, durability, shieldImg);
	}
	
	public ShieldMK1(int durability) {
		super(durability);
	}

	@Override
	public void render() {
		if(color.a > 0) {
			shieldImg.draw(x - shieldImg.getWidth() / 2, y - shieldImg.getHeight() / 2 + 1, color);
		}
	}
	
	@Override
	public boolean update(GameState gs) {
		if(isDestroyed())
			color.a -= 0.02f;
		return super.update(gs);
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
