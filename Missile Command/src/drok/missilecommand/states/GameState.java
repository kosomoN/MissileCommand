package drok.missilecommand.states;

import java.awt.Font;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Entity;
import drok.missilecommand.Planet;
import drok.missilecommand.debris.Asteroid;
import drok.missilecommand.debris.Debris;
import drok.missilecommand.turret.Missile;

public class GameState extends State {

	private List<Entity> entities = new ArrayList<Entity>();
	private List<Debris> debris = new ArrayList<Debris>();
	private List<Point> stars = new ArrayList<Point>();
	private Planet planet;
	private int missiles, partsCollected, missileCost = 20;

	private int timeSinceLastDebris;
	private Image gameOver;
	private Color gameOverColor = new Color(1, 1, 1, -0.5f);
	private int score;
	private TrueTypeFont bigFont;

	public GameState(int state) {
		super(state);
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		super.init(container, game);
		planet = new Planet(container.getWidth() / 2 / SCALE, container.getHeight() / 2 / SCALE);

		gameOver = new Image("res/graphics/Game Over.png");
		
		Asteroid.init();
		Planet.init();
		Missile.init();
		
		for(int i = 0; i < 50; i++) {
			Point p = new Point((int) (Math.random() * screenImg.getWidth()), (int) (Math.random() * screenImg.getHeight()));
			if(planet.getX() - 8 < p.x && planet.getX() + 8 > p.x && planet.getY() - 8 < p.y && planet.getY() + 8 > p.y) {
				i--;
				continue;
			}
			stars.add(p);
		}
		
		restart();
		
	    try {
	        Font awtFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/pixelmix.ttf"));
	        awtFont = awtFont.deriveFont(32f); // set font size
	        bigFont = new TrueTypeFont(awtFont, false);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public void restart() {
		gameOverColor.a = -0.5f;
		entities.clear();
		debris.clear();
		missiles = 20;
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.white);
		for(Point p : stars) {
			g.fillRect(p.x, p.y, 1, 1);
		}
		
		for(Entity ent : entities) {
			ent.render(g);
		}
		planet.render(g);
		if(planet.isHit()) {
			gameOver.draw((screenImg.getWidth() - gameOver.getWidth()) / 2, (screenImg.getHeight() - gameOver.getHeight()) / 2, gameOverColor);
		}
		g.copyArea(screenImg, 0, 0);
		g.clear();
		screenImg.draw(0, 0, SCALE);
		
		g.setFont(font);
		g.setColor(Color.white);
		
		if(!planet.isHit()) {
			g.drawString("FPS: " + container.getFPS(), 10, 10);
			g.drawString("Missiles: " + missiles, 10, 35);
			g.drawString("Score: " + score, 10, 60);
		} else {
			g.setFont(bigFont);
			g.setColor(new Color(1, 1, 1, gameOverColor.a - 0.5f));
			g.drawString("Score: " + score, (container.getWidth() - bigFont.getWidth("Score: " + score)) / 2, container.getHeight() / 2 + 100);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		planet.update(this, delta);
		for(Iterator<Entity> it = entities.iterator(); it.hasNext();) {
			Entity ent = it.next();
			if(ent.update(this, delta)) {
				if(ent instanceof Debris)
					debris.remove(ent);
				it.remove();
			}
		}
		
		timeSinceLastDebris += delta;
		if(timeSinceLastDebris >= 1000 && !planet.isHit()) {
			double rand = Math.random();
			float x, y;
			if(rand < 0.5) {
				y = (float) (Math.random() * screenImg.getHeight());
				if(rand < 0.25)
					x = -16;
				else
					x = screenImg.getWidth() + 16;
			} else {
				x = (float) (Math.random() * screenImg.getWidth());
				if(rand < 0.75)
					y = -16;
				else
					y = screenImg.getHeight() + 16;
			}
			addDebris(new Asteroid(x, y, 0.02f, 0, planet));
			timeSinceLastDebris = 0;
		}
		
		if(planet.isHit()) {
			if(gameOverColor.a < 2) {
				gameOverColor.a += 0.0025f;
			}
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		if(missiles > 0 && !planet.isHit()) {
			missiles--;
			entities.add(new Missile(planet.getX(), planet.getY(), (float) Math.toDegrees(Math.atan2(y / SCALE - planet.getY(), x / SCALE - planet.getX())), debris));
		} else if(gameOverColor.a > 0.7f){
			planet.reset();
		}
	}

	public void addDebris(Debris deb) {
		debris.add(deb);
		entities.add(deb);
	}

	public boolean inBounds(float x, float y) {
		return !(x < 0 || y < 0 || x > screenImg.getWidth() || y > screenImg.getHeight());
	}

	public int getScreenWidth() {
		return screenImg.getWidth();
	}
	
	public int getScreenHeight() {
		return screenImg.getHeight();
	}
	
	public void smallHit() {
		partsCollected++;
		if(partsCollected >= missileCost) {
			missiles++;
			partsCollected = 0;
		}
	}

	public void planetHit(Debris debris) {
		planet.hit(debris);
	}

	public void debrisDestroyed(Debris debris) {
		score += debris.getScore();
	}
}