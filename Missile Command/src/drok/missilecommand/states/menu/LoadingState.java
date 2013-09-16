package drok.missilecommand.states.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Launch;
import drok.missilecommand.states.State;
import drok.missilecommand.utils.ResourceManager;

public class LoadingState extends State {

	private boolean hasRendered;

	public LoadingState(int state) {
		super(state);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		hasRendered = true;
		g.setFont(bigFont);
		g.drawString("Loading", (container.getWidth() - bigFont.getWidth("Loading")) / 2, container.getHeight() / 2);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(hasRendered) {
			ResourceManager.loadResources();
			game.enterState(Launch.MENUSTATE);
		}
	}
}
