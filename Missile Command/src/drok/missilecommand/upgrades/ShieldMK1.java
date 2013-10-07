package drok.missilecommand.upgrades;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import drok.missilecommand.Planet;
import drok.missilecommand.states.game.GameState;

public class ShieldMK1 extends Shield {
	//Fields
	private Color color = new Color(1, 1, 1, 1f);
	
	public ShieldMK1(Planet planet, Image shieldImg, GameState gs) {
		super(planet, 3, shieldImg, gs);
	}

	@Override
	public void render(Graphics g) {
		if(color.a > 0) {
			shieldImg.draw(x - shieldImg.getWidth() / 2, y - shieldImg.getHeight() / 2, color);
		}
	}
	
	@Override
	public boolean update(int delta) {
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
