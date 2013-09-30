package drok.missilecommand.states.menu;

import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Launch;
import drok.missilecommand.Level;
import drok.missilecommand.Level.Spawnable;
import drok.missilecommand.states.State;
import drok.missilecommand.states.game.LevelBasedGameState;
import drok.missilecommand.utils.Button;
import drok.missilecommand.utils.ResourceManager;

public class LevelSelectState extends State {

	private static final int AREA_WIDTH = 2048, AREA_HEIGHT = 2048;
	private static final float MAX_ZOOM_OUT = 0.25f;
	private static Image moonImg, mask;
	private Button back;
	private float zoomLevel = 1;
	private float camPosx, camPosy;
	private List<Point> stars = new ArrayList<Point>();
	private List<LevelSelectPlanet> planets = new ArrayList<LevelSelectPlanet>();
	private float moonInfoTimer;
	private boolean hasRenderedMoonInfo;

	
	public LevelSelectState(int state) {
		super(state);
	}

	@Override
	public void firstTimeEnter() throws SlickException {
		super.firstTimeEnter();
		for(int i = 0; i < 1000; i++) {
			Point p = new Point((int) (Math.random() * (AREA_WIDTH + container.getWidth() / MAX_ZOOM_OUT) -  + container.getWidth() / 2 / MAX_ZOOM_OUT),
								(int) (Math.random() *(AREA_HEIGHT + container.getHeight() / MAX_ZOOM_OUT) -  + container.getHeight() / 2 / MAX_ZOOM_OUT));
			stars.add(p);
		}
		mask = ResourceManager.getImage("res/graphics/Planet Mask.png");
		moonImg = ResourceManager.getImage("res/graphics/Moon.png");
		moonImg = moonImg.getScaledCopy(SCALE / 2);
		back = new Button(10, 10, ResourceManager.getImage("res/graphics/BackButton.png").getWidth(), ResourceManager.getImage("res/graphics/BackButton.png").getHeight(), ResourceManager.getImage("res/graphics/BackButton.png"), 1);
		
		loadPlanets(new File("res/data/planets"));
		loadLevels(new File("res/data/levels"));
	}
	
	private void loadPlanets(File file) {
		for(File f : file.listFiles()) {
			if(f.isDirectory())
				loadLevels(f);
			else if(f.getName().substring(f.getName().lastIndexOf(".")).equals(".misscommplanet")) {
				FileReader fr = null;
				try {
					JSONParser jsonParser = new JSONParser();
					fr = new FileReader(f);
					JSONObject obj = (JSONObject) jsonParser.parse(fr);
					planets.add(new LevelSelectPlanet((String) obj.get("name"), ((Long) obj.get("x")).floatValue(), ((Long) obj.get("y")).floatValue()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void loadLevels(File file) {
		for(File f : file.listFiles()) {
			if(f.isDirectory())
				loadLevels(f);
			else if(f.getName().substring(f.getName().lastIndexOf(".")).equals(".misscommlvl")) {
				try {
					Level lvl = new Level(f);
					boolean found = false;
					for(LevelSelectPlanet pl : planets) {
						if(pl.getName().equals(lvl.getPlanetName())) {
							pl.addMoon(new Moon(lvl, pl));
							found = true;
						}
					}
					if(!found) {
						System.err.println("Didn't find planet: " + lvl.getPlanetName() + "!");
					}
				} catch (Exception e) {
					System.err.println("Failed to load level: " + f.getName());
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		camPosx = AREA_WIDTH / 2;
		camPosy = AREA_HEIGHT / 2;
		for(LevelSelectPlanet p : planets) {
			for(Moon m : p.moons)
				m.level.restart();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.scale(zoomLevel, zoomLevel);
		g.translate(-camPosx + container.getWidth() / 2 / zoomLevel, -camPosy + container.getHeight() / 2 / zoomLevel);
		super.render(container, game, g);
		for(LevelSelectPlanet planet : planets)
			planet.render();
		
		for(Point p : stars) {
			g.fillRect(p.x, p.y, 4, 4);
		}
		
		g.resetTransform();
		
		back.render(g);
		
		int x = container.getInput().getMouseX();
		int y = container.getInput().getMouseY();
		x /= zoomLevel;
		y /= zoomLevel;
		
		x += camPosx - container.getWidth() / 2 / zoomLevel;
		y += camPosy - container.getHeight() / 2 / zoomLevel;
		g.setFont(font16);
		
		hasRenderedMoonInfo = false;
		for(LevelSelectPlanet p : planets) {
			if(x > p.x - 64 && x < p.x + 64 && y > p.y - 64 && y < p.y + 64) { //8 * 8
				break;
			} else {
				for(Moon moon : p.moons) {
					if(x > moon.x - moonImg.getWidth() / 2 && x < moon.x + moonImg.getWidth() / 2 &&
							y > moon.y - moonImg.getHeight() / 2 && y < moon.y + moonImg.getHeight() / 2) {
						moon.stop = true;
						renderMoonInfo(container.getInput().getMouseX() - 5, container.getInput().getMouseY() - 5, moon, g);
						break;
					}
				}
			}
		}
	}
	
	private void renderMoonInfo(int x, int y, Moon moon, Graphics g) {
		hasRenderedMoonInfo = true;
		g.setColor(Color.white);
		int textY = y + 30;
		String str = "Name: " + moon.name;
		g.drawString(moonInfoTimer - ((textY - y) / 15) < 0 ? "" : (moonInfoTimer - ((textY - y) / 15) < str.length() ? str.substring(0, (int) moonInfoTimer - ((textY - y) / 15)) + (char)(Math.random() * 26 + 'a') : str), x + 15, textY);
		for(Spawnable sp : moon.level.getSpawnables()) {
			textY += 30;
			str = sp.getAmount() + " " + sp.getSpawnTypeName() + (sp.getAmount() > 1 ? "'s" : "");
			g.drawString(moonInfoTimer - ((textY - y) / 15) < 0 ? "" : (moonInfoTimer - ((textY - y) / 15) < str.length() ? str.substring(0, (int) moonInfoTimer - ((textY - y) / 15)) + (char)(Math.random() * 26 + 'a') : str), x + 15, textY);
		}
		textY += 30;
		str = "Missiles: " + moon.level.getMissileCount();
		g.drawString(moonInfoTimer - ((textY - y) / 15) < 0 ? "" : (moonInfoTimer - ((textY - y) / 15) < str.length() ? str.substring(0, (int) moonInfoTimer - ((textY - y) / 15)) + (char)(Math.random() * 26 + 'a') : str), x + 15, textY);
		textY += 30;
		str = "Rate: " + (1000d / moon.level.getSpawnTime()) + "/s";
		g.drawString(moonInfoTimer - ((textY - y) / 15) < 0 ? "" : (moonInfoTimer - ((textY - y) / 15) < str.length() ? str.substring(0, (int) moonInfoTimer - ((textY - y) / 15)) + (char)(Math.random() * 26 + 'a') : str), x + 15, textY);
		
		//moonInfoTimer < str.length() ? str.substring(0, (int) moonInfoTimer) + (char)(Math.random() * 26 + 'a') : str
	}

	@Override
	public void mouseWheelMoved(int newValue) {
		super.mouseWheelMoved(newValue);
		
		float x = (container.getInput().getMouseX() - container.getWidth() / 2) / zoomLevel;
		float y = (container.getInput().getMouseY() - container.getHeight() / 2) / zoomLevel;
		
		if(newValue > 0) {
			if(zoomLevel + 0.1 < 1)
				zoomLevel += 0.1f;
			else
				zoomLevel = 1;
		} else if(zoomLevel - 0.1 > MAX_ZOOM_OUT) {
			zoomLevel -= 0.1f;
		} else {
			zoomLevel = MAX_ZOOM_OUT;
		}
		
		x -= (container.getInput().getMouseX() - container.getWidth() / 2) / zoomLevel;
		y -= (container.getInput().getMouseY() - container.getHeight() / 2) / zoomLevel;
		
		camPosx += x;
		camPosy += y;
		
		clampCamPos();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		for(LevelSelectPlanet planet : planets)
			planet.update();
		moonInfoTimer += 0.2f;
		
		if(!hasRenderedMoonInfo)
			moonInfoTimer = 0;
		
		Input input = container.getInput();
		if(back.hoverOver(input.getMouseX(), input.getMouseY())) {
			back.changeImage(ResourceManager.getImage("res/graphics/BackButtonHover.png"));
			if(back.clicked(input.getMouseX(), input.getMouseY(), container)) {
				game.enterState(Launch.MENUSTATE);
			}
		} else {
			back.changeImage(ResourceManager.getImage("res/graphics/BackButton.png"));
		}
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		super.mouseDragged(oldx, oldy, newx, newy);
		camPosx += (oldx - newx) / zoomLevel;
		camPosy += (oldy - newy) / zoomLevel;
		clampCamPos();
	}
	
	private void clampCamPos() {
		if(camPosx < 0)
			camPosx = 0;
		else if(camPosx > AREA_WIDTH)
			camPosx = AREA_WIDTH;

		if(camPosy < 0)
			camPosy = 0;
		else if(camPosy > AREA_HEIGHT)
			camPosy= AREA_HEIGHT;
	}
	
	
	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);

		x /= zoomLevel;
		y /= zoomLevel;
		
		x += camPosx - container.getWidth() / 2 / zoomLevel;
		y += camPosy - container.getHeight() / 2 / zoomLevel;
		for(LevelSelectPlanet p : planets) {
			if(x > p.x - 64 && x < p.x + 64 && y > p.y - 64 && y < p.y + 64) { //8 * 8
				break;
			} else {
				for(Moon moon : p.moons) {
					if(x > moon.x - moonImg.getWidth() / 2 && x < moon.x + moonImg.getWidth() / 2 &&
							y > moon.y - moonImg.getHeight() / 2 && y < moon.y + moonImg.getHeight() / 2) {
						((LevelBasedGameState) game.getState(Launch.LEVELGAMESTATE)).setLevel(moon.level);
						((MenuState) game.getState(Launch.MENUSTATE)).fadeMusic(2000);
						game.enterState(Launch.LEVELGAMESTATE);
						break;
					}
				}
			}
		}
	}
	
	

	private class LevelSelectPlanet {
		private List<Moon> moons = new ArrayList<Moon>();
		public float x, y;
		private int timer;
		private int imgPos;
		private Image planetImg;
		private Image planetTexture;
		private String name;
		
		public LevelSelectPlanet(String name, float x, float y) {
			try {
				planetImg = new Image("res/graphics/EmptyPlanetTexture.png");
				planetImg.setFilter(Image.FILTER_NEAREST);
				planetTexture = ResourceManager.getImage("res/graphics/Planet " + name + " Texture.png");
			} catch (SlickException e) {
				e.printStackTrace();
			}
			
			createImg();
			
			this.name = name;
			
			this.x = x;
			this.y = y;
		}

		public String getName() {
			return name;
		}

		public void render() {
			planetImg.draw(x - planetImg.getWidth() * SCALE / 2, y - planetImg.getHeight() * SCALE / 2, SCALE);
			container.getGraphics().setColor(Color.gray);
			for(Moon moon : moons) {
				moon.render();
			}
		}
		
		public void update() {
			for(Moon moon : moons)
				moon.update();
			
			timer++;
			if(timer > 50) {
				imgPos += 1;
				if(imgPos >= 32)
					imgPos = 0;
				createImg();

				timer = 0;
			}
		}
		
		private void createImg() {
			try {
				Graphics g = planetImg.getGraphics();
				g.clear();
				g.drawImage(mask, 0, 0);
				g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
				g.drawImage(planetTexture, -imgPos, 0);
				if(imgPos > 16)
					g.drawImage(planetTexture, -imgPos + 32, 0);
				g.flush();
			} catch (SlickException e) {
				e.printStackTrace();
			}
		}
		
		public void addMoon(Moon moon) {
			moon.setIndex(moons.size() + 1);
			moons.add(moon);
		}
	}
	
	private class Moon {
		private Level level;
		private LevelSelectPlanet planet;
		private int moonIndex;
		private float angle = (float) (Math.random() * 360);
		private float x, y;
		private float rotSpeed;
		private float circleRadius;
		private String name;
		private boolean stop;
		
		public Moon(Level level, LevelSelectPlanet planet) {
			this.level = level;
			this.planet = planet;
			name = level.getName();
		}
		
		private void setIndex(int index) {
			this.moonIndex = index;
			rotSpeed = 0.05f / moonIndex * (Math.random() < 0.5f ? 1 : -1);
			circleRadius = 70 * moonIndex + 40;
		}
		
		private void update() {
			if(!stop) {
				angle += rotSpeed;
				x = (float) (planet.x - circleRadius * Math.cos(Math.toRadians(angle)));
				y = (float) (planet.y + circleRadius * Math.sin(Math.toRadians(angle)));
			} else {
				stop = false;
			}
		}
		
		private void render() {
			//container.getGraphics().drawOval(planet.x - circleRadius, planet.y - circleRadius, circleRadius * 2, circleRadius * 2);
			moonImg.draw(x - moonImg.getWidth() / 2, y - moonImg.getHeight() / 2, Color.darkGray);
		}
	}
}
