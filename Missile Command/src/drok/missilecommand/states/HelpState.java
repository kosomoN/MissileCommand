package drok.missilecommand.states;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

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
		controls = "In Menu:"
				+ "\nUse arrowkeys in the menu\n"
				+ "\nOR \n"
				+ "\nUse mouse\n"
				+ "In game:"
				+ "\nUse mouse";
		back = "Back To Menu";
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setFont(font);
		g.drawString(headtitle, container.getWidth() / 2 - 5, 50);
		drawCenteredLines(g, font, overview, container.getWidth() / 2, 100);
		//g.drawString(overview, container.getWidth() / 2 - 210, 100);
		g.drawString(title, container.getWidth() / 2 - 10, container.getHeight() / 5 * 2);
		g.drawString(controls, 200, container.getHeight() / 5 * 2 + 30);
		g.drawString(back, container.getWidth() - 100, container.getHeight() - 50);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

	}
	
	private void drawCenteredLines(Graphics g, Font font, String string, int firstLineMiddleX, int firstLineY) {
		g.setFont(font);
		for(String s : string.split("\n")) {
			firstLineY += font.getLineHeight() + 10;
			g.drawString(s, firstLineMiddleX - font.getWidth(s) / 2, firstLineY);
		}
	}
}
