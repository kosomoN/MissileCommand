package drok.missilecommand.states.menu;

import org.newdawn.slick.Font;
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
		controls = "Aim:" + addSpaces(font.getWidth("Shoot")) + "Mouse\n"
				+ "Shoot:" + addSpaces(font.getWidth("Aim")) + "Left Click";
		back = "Back To Menu";
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setFont(font);
		g.drawString(headtitle, container.getWidth() / 2 - 5, 50);
		drawCenteredLines(g, font, overview, container.getWidth() / 2, 100);
		g.drawString(title, container.getWidth() / 2 - 10, container.getHeight() / 5 * 2);
		drawLines(g, font, controls, 200, container.getHeight() / 5 * 2 + 30);
		g.drawString(back, container.getWidth() - font.getWidth("Back To Menu") - 20, container.getHeight() - 50);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		if(button == Input.MOUSE_LEFT_BUTTON) {
			if(Util.isInside(x, y, new Rectangle(container.getWidth() - font.getWidth("Back To Menu") - 20,
					container.getHeight() - 50, font.getWidth("Back To Menu"), font.getHeight()))) {
				game.enterState(Launch.MENUSTATE);
			}
		}
	}

	private void drawCenteredLines(Graphics g, Font font, String string, int firstLineMiddleX, int firstLineY) {
		g.setFont(font);
		for(String s : string.split("\n")) {
			g.drawString(s, firstLineMiddleX - font.getWidth(s) / 2, firstLineY);
			firstLineY += font.getLineHeight() + 10;
		}
	}
	
	private void drawLines(Graphics g, Font font, String string, int firstLineX, int firstLineY) {
		g.setFont(font);
		for(String s : string.split("\n")) {
			g.drawString(s, firstLineX, firstLineY);
			firstLineY += font.getLineHeight() + 10;
		}
	}
	
	private String addSpaces(int spaces) {
		String string = "";
		
		if(spaces < 0)
			spaces = 0;
		
		for(int i = 0; i < spaces; i++) {
			string += " ";
		}
		return string;
	}
}
