package drok.missilecommand.states.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Entity;
import drok.missilecommand.Launch;
import drok.missilecommand.Planet;
import drok.missilecommand.debris.Asteroid;
import drok.missilecommand.debris.BigAsteroid;
import drok.missilecommand.debris.Debris;
import drok.missilecommand.debris.HeavyFighter;
import drok.missilecommand.states.State;
import drok.missilecommand.upgrades.Upgrade;
import drok.missilecommand.utils.ResourceManager;
import drok.missilecommand.weapons.Nuke;
import drok.missilecommand.weapons.Probe;
import drok.missilecommand.weapons.Turret;

public abstract class GameState extends State {

	private List<Entity> entities = new ArrayList<Entity>(), newEntities = new ArrayList<Entity>();
	private List<Debris> debris = new ArrayList<Debris>();
	private List<Point> stars = new ArrayList<Point>();
	private Upgrade[] upgrades;
	protected Planet planet;
	protected Nuke nuke;
	private Turret turret;
	protected int missiles;
	private int partsCollected;
	private int missileCost = 15;
	private boolean nukeLaunch;
	
	protected static Image gameOver;
	private static Image pauseImg;
	protected Color gameOverColor = new Color(1, 1, 1, -0.5f);
	protected int score;
	protected boolean paused;
	private Color pauseColor = new Color(1, 1, 1, 1f);
	private float pauseColorTimer;

	public GameState(int state) {
		super(state);
	}

	@Override
	public void firstTimeEnter() throws SlickException {
		for(int i = 0; i < 50; i++) {
			Point p = new Point((int) (Math.random() * screenImg.getWidth()), (int) (Math.random() * screenImg.getHeight()));
			if(planet.getX() - 8 < p.x && planet.getX() + 8 > p.x && planet.getY() - 8 < p.y && planet.getY() + 8 > p.y) {
				i--;
				continue;
			}
			stars.add(p);
		}

		gameOver = ResourceManager.getImage("Game Over.png");
		pauseImg = ResourceManager.getImage("Paused.png");
		
		Asteroid.init();
		Planet.init();
		BigAsteroid.init();
		Turret.init();
		Probe.init();
		HeavyFighter.init();
		
		turret = new Turret(planet, 5);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		upgrades = ((ShopState) game.getState(Launch.SHOPSTATE)).getBoughtUpgrades().toArray(new Upgrade[((ShopState) game.getState(Launch.SHOPSTATE)).getBoughtUpgrades().size()]);
		nuke = new Nuke(planet, container, this);
		nukeLaunch = false;
		restart();
	}

	public void restart() {
		entities.clear();
		debris.clear();
		missiles = 20;
		score = 0;
		turret.resetTurrets();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);
		for(Entity ent : entities) {
			if(!ent.renderScaled())
				ent.render(g);
		}
		g.setFont(font16);
		g.setColor(Color.white);
		g.drawString("FPS: " + container.getFPS(), 10, 10);
		if(!planet.isHit()) {
			turret.render(container);
		}
	}
	
	@Override
	protected void renderScaled(Graphics g) {
		g.setColor(Color.white);
		for(Point p : stars) {
			g.fillRect(p.x, p.y, 1, 1);
		}
		
		for(Entity ent : entities) {
			if(ent.renderScaled())
				ent.render(g);
		}
		
		if(upgrades.length > 0) {
			for(Upgrade u : upgrades) {
				if(u.getName() != "Nuke") {
					u.render(g);
				} else {
					nuke.render(g);
				}
			}
		}
		
		if(nukeLaunch) {
			nuke.render(g);
		}
		planet.render(g);
		
		if(paused) {
			pauseImg.draw((screenImg.getWidth() - pauseImg.getWidth()) / 2, (screenImg.getHeight() - pauseImg.getHeight()) / 2, pauseColor);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		for(Entity e : newEntities) { // To avoid ConcurrentModificationException
			if(e instanceof Debris)
				debris.add((Debris) e);
			entities.add(e);
		}
		newEntities.clear();
		if(!paused) {
			planet.update(this, delta);
			turret.update(this, delta);
			//probe.update(delta);
			if(nukeLaunch)
				nuke.update(delta);
			
			for(Iterator<Entity> it = entities.iterator(); it.hasNext();) {
				Entity ent = it.next();
				if(ent.update(this, delta)) {
					if(ent instanceof Debris)
						debris.remove(ent);
					it.remove();
				} else if(ent instanceof Debris) {
					nuke.update(delta, (Debris) ent);
				}
			}
			
			if(upgrades.length > 0) {
				for(Upgrade u : upgrades) {
					if(u.getName() != "Nuke") {
						u.update(delta);
					} else
						nuke.update(delta);
				}
			}
			
			
			if(planet.isHit()) {
				if(gameOverColor.a < 2) {
					gameOverColor.a += 0.0025f;
				}
			}
			
			if(nuke.isOutOfScreen()) {
				nukeLaunch = false;
				nuke = new Nuke(planet, container, this);
			}
		} else {
			pauseColorTimer += 0.05f;
			pauseColor.a = (float) (Math.sin(pauseColorTimer) + 1) / 2;
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		if(button == Input.MOUSE_LEFT_BUTTON) {
			if(missiles > 0 && !planet.isHit()) {
				missiles--;
				turret.fire(this, x / SCALE, y / SCALE);
			}
		} else if(button == Input.MOUSE_RIGHT_BUTTON) {
			nukeLaunch = true;
		}
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if(key == Input.KEY_ESCAPE)
			game.enterState(Launch.MENUSTATE);//pause();
	}
	/*
	private void pause() {
		paused = !paused;
		pauseColor.a = 1;
		pauseColorTimer = 0;
	}*/

	public void addEntity(Entity ent) {
		newEntities.add(ent);
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

	public Planet getPlanet() {
		return planet;
	}

	public int getScore() {
		return score;
	}

	public List<Debris> getDebris() {
		return debris;
	}

	public GameContainer getContainer() {
		return container;
	}
}