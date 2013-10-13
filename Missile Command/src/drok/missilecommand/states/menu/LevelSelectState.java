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
import org.newdawn.slick.Sound;
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
	private static Image moonImg, mask16, mask32;
	private Button back;
	private float zoomLevel = 1;
	private float camPosx, camPosy;
	private float initialCamX, initialCamY, goToX, goToY, initialZoom, camT;
	private boolean isMovingCam;
	private List<Point> stars = new ArrayList<Point>();
	private List<LevelSelectPlanet> planets = new ArrayList<LevelSelectPlanet>();
	private float moonInfoTimer;
	private boolean hasRenderedMoonInfo;
	private Sound beepSound;
	private boolean doneRenderingInfo;

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
		mask16 = ResourceManager.getImage("Planet Mask 16.png");
		mask32 = ResourceManager.getImage("Planet Mask 32.png");
		moonImg = ResourceManager.getImage("Moon.png");
		moonImg = moonImg.getScaledCopy(SCALE / 2);
		back = new Button(10, 10, ResourceManager.getImage("BackButton.png").getWidth(), ResourceManager.getImage("BackButton.png").getHeight(), ResourceManager.getImage("BackButton.png"), 1);
		
		beepSound = new Sound("res/audio/Beep.ogg");
		
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
		zoomLevel = MAX_ZOOM_OUT;
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
				
		for(Point p : stars) {
			g.fillRect(p.x, p.y, 4, 4);
		}
		
		for(LevelSelectPlanet planet : planets)
			planet.render();
		
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
		doneRenderingInfo = true;
		g.setColor(Color.white);
		x += 15;
		int textY = y + 30; 
		renderMoonInfoLine(g, x, textY, y, "Name: " + moon.name);
		
		int debrisCount = 0;
		for(Spawnable sp : moon.level.getSpawnables()) {
			debrisCount += sp.getAmount();
		}
		textY += 30;
		renderMoonInfoLine(g, x, textY, y, debrisCount + " Debris");
		textY += 30;
		int highscore = getCurrentSave().getHighscore(moon.level);
		renderMoonInfoLine(g, x, textY, y, "Highscore: " + (highscore != -1 ? highscore : "Not completed"));
		/*
		textY += 30;
		renderMoonInfoLine(g, x, textY, y, "Missiles: " + moon.level.getMissileCount());
		
		textY += 30;
		str = "Rate: " + (1000d / moon.level.getSpawnTime()) + "/s";
		renderMoonInfoLine(g, x, textY, y, str);*/
	}
	
	private void renderMoonInfoLine(Graphics g, int x, int textY, int y, String str) {
		if(moonInfoTimer - ((textY - y) / 15) >= 0 ) {
			if((moonInfoTimer - ((textY - y) / 15) < str.length())) {
				g.drawString(str.substring(0, (int) moonInfoTimer - ((textY - y) / 15)) + (char)(Math.random() * 26 + 'a'), x, textY);
				doneRenderingInfo = false;
			} else
				g.drawString(str, x, textY);
			
			
		}
	}
	
	private float cosInterpolation(float x1, float x2, float t) {
	   double t2 = (1 - Math.cos(t * Math.PI)) / 2;
	   return (float) (x1 * (1 - t2) + x2 * t2);
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
		if(!doneRenderingInfo && moonInfoTimer % 1 < 0.2)
			beepSound.play();
		
		if(!hasRenderedMoonInfo) {
			moonInfoTimer = 0;
			doneRenderingInfo = false;
		}
		
		if(isMovingCam) {
			if(camT + 0.01 < 1) {
				camT += 0.01;
				camPosx = cosInterpolation(initialCamX, goToX, camT);
				camPosy = cosInterpolation(initialCamY, goToY, camT);
				zoomLevel = cosInterpolation(initialZoom, 1, camT);
			} else {
				camT = 0;
				camPosx = goToX;
				camPosy = goToY;
				zoomLevel = 1;
				isMovingCam = false;
			}
		}
		
		if(container.getInput().isKeyPressed(Input.KEY_S)) {
			game.enterState(Launch.SHOPSTATE);
		}
		
		if(back.hoverOver(container.getInput().getMouseX(), container.getInput().getMouseY())) {
			back.changeImage(ResourceManager.getImage("BackButtonHover.png"));
		} else {
			back.changeImage(ResourceManager.getImage("BackButton.png"));
		}
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		super.mouseDragged(oldx, oldy, newx, newy);
		if(!isMovingCam) {
			camPosx += (oldx - newx) / zoomLevel;
			camPosy += (oldy - newy) / zoomLevel;
			clampCamPos();
		}
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
	public void mouseClicked(int button, int x, int y, int clickCount) {
		super.mouseClicked(button, x, y, clickCount);
		
		if(back.hoverOver(container.getInput().getMouseX(), container.getInput().getMouseY())) {
			game.enterState(Launch.MENUSTATE);
		}
		
		x /= zoomLevel;
		y /= zoomLevel;
		
		x += camPosx - container.getWidth() / 2 / zoomLevel;
		y += camPosy - container.getHeight() / 2 / zoomLevel;
		for(LevelSelectPlanet p : planets) {
			if(x > p.x - 64 && x < p.x + 64 && y > p.y - 64 && y < p.y + 64) { //8 * 8
				if(!isMovingCam) {
					initialZoom = zoomLevel;
					goToX = p.x;
					goToY = p.y;
					initialCamX = camPosx;
					initialCamY = camPosy;
					isMovingCam = true;
				}
				break;
			} else {
				for(Moon moon : p.moons) {
					if(x > moon.x - moonImg.getWidth() / 2 && x < moon.x + moonImg.getWidth() / 2 &&
							y > moon.y - moonImg.getHeight() / 2 && y < moon.y + moonImg.getHeight() / 2) {
						((LevelBasedGameState) game.getState(Launch.LEVELGAMESTATE)).setLevel(moon.level);
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
		private int timer = (int) (Math.random() * 50);
		private int imgPos;
		private Image planetImg;
		private Image planetTexture;
		private String name;
		
		public LevelSelectPlanet(String name, float x, float y) {
			planetTexture = ResourceManager.getImage("Planet " + name + " Texture.png");
			try {
				planetImg = new Image(planetTexture.getHeight(), planetTexture.getHeight());
				planetImg.setFilter(Image.FILTER_NEAREST);
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
				if(imgPos >= planetTexture.getWidth())
					imgPos = 0;
				createImg();

				timer = 0;
			}
		}
		
		private void createImg() {
			try {
				Graphics g = planetImg.getGraphics();
				g.clear();
				if(planetImg.getHeight() == 16)
					g.drawImage(mask16, 0, 0);
				else
					g.drawImage(mask32, 0, 0);
				g.setDrawMode(Graphics.MODE_COLOR_MULTIPLY);
				g.drawImage(planetTexture, -imgPos, 0);
				if(imgPos > planetTexture.getWidth() / 2)
					g.drawImage(planetTexture, -imgPos + planetTexture.getWidth(), 0);
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
				x = (float) (planet.x + circleRadius * Math.cos(Math.toRadians(angle)));
				y = (float) (planet.y + circleRadius * Math.sin(Math.toRadians(angle)));
			}
		}
		
		private void render() {
			moonImg.draw(x - moonImg.getWidth() / 2, y - moonImg.getHeight() / 2, new Color(0.3f, 0.3f, 0.3f, zoomLevel * 2 - 1));
			if(stop)
				stop = false;
		}
	}
}