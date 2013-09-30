package drok.missilecommand.upgrades;

import org.newdawn.slick.Image;

import drok.missilecommand.states.game.GameState;

public class ShieldMK2 extends Shield {
	//Fields
	
	public ShieldMK2(float centerx, float centery, Image img, GameState gs) {
		super(centerx, centery, 5, img, gs);
	}

	@Override
	public void render() {
		if(!isDestroyed)
			shieldImg.drawCentered(x, y);
	}
	
	@Override
	public boolean update(int delta) {
		return super.update(gs);
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