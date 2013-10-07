package drok.missilecommand.upgrades;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import drok.missilecommand.Planet;
import drok.missilecommand.states.game.GameState;

public class ShieldMK2 extends Shield {
	//Fields
	
	public ShieldMK2(Planet planet, Image img, GameState gs) {
		super(planet, 5, img, gs);
	}

	@Override
	public void render(Graphics g) {
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