package drok.missilecommand;

import org.newdawn.slick.Graphics;

import drok.missilecommand.states.game.GameState;

public interface Entity {
	public void render(Graphics g);
	public boolean update(GameState gs, int delta);
	public boolean renderScaled();
}