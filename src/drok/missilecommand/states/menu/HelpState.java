package drok.missilecommand.states.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Launch;
import drok.missilecommand.states.State;
import drok.missilecommand.utils.Util;

public class HelpState extends State {
	//Fields
	private String headtitle, title, overview, controls, back;
	
	public HelpState(int state) {
		super(state);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {		
		super.init(container, game);
		headtitle = "HELP";
		title = "Controls";
		overview = "Missile Command is a game where you try to save the Earth\n"
				+ " from asteroids by shooting them with your turret.";
		controls = "Aim:" + Util.addSpaces(font16.getWidth("Shoot") / font16.getWidth(" ")) + "Mouse\n"
				+ "Shoot:" + Util.addSpaces(font16.getWidth("Aim") / font16.getWidth(" ")) + "Left Click";
		back = "Back To Menu";
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setFont(font16);
		g.drawString(headtitle, container.getWidth() / 2 - 5, 50);
		Util.drawCenteredLines(g, font16, overview, container.getWidth() / 2, 100);
		g.drawString(title, container.getWidth() / 2 - 10, container.getHeight() / 5 * 2);
		Util.drawLines(g, font16, controls, 200, container.getHeight() / 5 * 2 + 30);
		g.drawString(back, container.getWidth() - font16.getWidth("Back To Menu") - 20, container.getHeight() - 50);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		if(button == Input.MOUSE_LEFT_BUTTON) {
			if(Util.isInside(x, y, new Rectangle(container.getWidth() - font16.getWidth("Back To Menu") - 20,
					container.getHeight() - 50, font16.getWidth("Back To Menu"), font16.getHeight()))) {
				game.enterState(Launch.MENUSTATE);
			}
		}
	}
}
