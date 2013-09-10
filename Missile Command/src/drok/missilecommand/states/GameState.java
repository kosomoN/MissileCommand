package drok.missilecommand.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Planet;
import drok.missilecommand.debris.Asteroid;
import drok.missilecommand.debris.Debris;
import drok.missilecommand.turret.Turret;

public class GameState extends State {
	//Fields
	private List<Debris> debris = new ArrayList<Debris>();
	private Turret turret;
	private Planet planet;
	private Image image;
	
	public GameState(int state) {
		super(state);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		planet = new Planet(container.getWidth() / 2 / 6, container.getHeight() / 2 / 6);

		Asteroid.init();
		Planet.init();
		
		image = new Image(container.getWidth() / 6, container.getHeight() / 6);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		for(Debris deb : debris) {
			deb.render();
		}
		planet.render();
		
		g.copyArea(image, 0, 0);
		g.clear();
		image.draw(0, 0, 6);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		for(Debris deb : debris) {
			deb.update(delta);
		}
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		addDebris(new Asteroid(x / 6, y / 6, 0.01f, (float) Math.toDegrees(Math.atan2(60 - y / 6, 90 - x / 6))));
	}

	public void addDebris(Debris deb) {
		debris.add(deb);
	}
}
