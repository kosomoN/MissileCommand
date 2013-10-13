package drok.missilecommand.states.game;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Entity;
import drok.missilecommand.Launch;
import drok.missilecommand.Level;
import drok.missilecommand.Planet;
import drok.missilecommand.debris.Asteroid;
import drok.missilecommand.debris.Debris;
import drok.missilecommand.upgrades.Ware;
import drok.missilecommand.utils.ResourceManager;
import drok.missilecommand.utils.Util;

public class LevelBasedGameState extends GameState {
	
	private Level level;
	private Sound beepSound;
	private String[] story;
	private int delayInMilliseconds = 5000;
	private int countdown = 4000;
	private float timer;
	private float alpha = 1;
	private boolean enteredLevel, renderedStory;
	private boolean readyFaded, readyRendering, renderedLine;
	private int storyDialog = 0;
	
	private boolean hasSaved;
	private float scoreFromMissiles;
	private float tempMissileScore;
	private float debrisScore;
	private float tempDebrisScore;
	private boolean wasHighscore;
	private int ticksSinceSoundPlayed;
	
	private static Image youWinImg;
	
	public LevelBasedGameState(int state) {
		super(state);
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		planet = new Planet(container.getWidth() / 2 / SCALE, container.getHeight() / 2 / SCALE, level.getPlanetName());
		super.enter(container, game);
		for(Ware w : getWares()) {
			w.init(planet, this);
		}
		resetIntro();
	}

	@Override
	public void firstTimeEnter() throws SlickException {
		super.firstTimeEnter();
		youWinImg = ResourceManager.getImage("You Win.png");
		beepSound = new Sound("res/audio/Beep.ogg");
	}

	@Override
	public void restart() {
		super.restart();
		missiles = level.getMissileCount();
		gameOverColor.a = -0.1f;
		
		hasSaved = false;
		scoreFromMissiles = 0;
		debrisScore = 0;
		tempMissileScore = -1;
		tempDebrisScore = -1;
		wasHighscore = false;
//		level.restart();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(enteredLevel) {
			super.update(container, game, delta);
			if(countdown <= 1000) {
				if(!paused) {
					level.update(delta, this);
					
					if(level.hasWon()) {
						boolean containsParts = false;
						for(Entity ent : entities) {
							if(ent instanceof Asteroid.Part) {
								containsParts = true;
								((Asteroid.Part) ent).setFriction(0.99f);
							}
						}
						if(!containsParts) {
							gameOverColor.a += 0.0025f;
							if(level.hasWon()) {
								if(gameOverColor.a - 0.75f > 0.7f) {
									if(tempDebrisScore == -1)
										tempDebrisScore = debrisScore;
									if(tempDebrisScore > 0) {
										score += debrisScore / 100f;
										tempDebrisScore -= debrisScore / 100f;
										ticksSinceSoundPlayed++;
										if(ticksSinceSoundPlayed >= 5) {
											beepSound.play();
											ticksSinceSoundPlayed = 0;
										}
									}
								}
								
								if(gameOverColor.a - 1.25f > 0.7f) {
									if(scoreFromMissiles / 2f <= missiles) {
										scoreFromMissiles += missiles / 100f;
										ticksSinceSoundPlayed++;
										if(ticksSinceSoundPlayed >= 5) {
											beepSound.play();
											ticksSinceSoundPlayed = 0;
										}
									} else if(tempMissileScore > 0 || tempMissileScore == -1) {
										if(tempMissileScore == -1)
											tempMissileScore = scoreFromMissiles;
										missiles = -1;
										score += scoreFromMissiles / 100f;
										tempMissileScore -= scoreFromMissiles / 100;
										
										ticksSinceSoundPlayed++;
										if(ticksSinceSoundPlayed >= 5) {
											beepSound.play();
											ticksSinceSoundPlayed = 0;
										}
									} else if(!hasSaved){
										score = debrisScore + scoreFromMissiles; //To fix rounding errors
										save();
									}
								}
							}
						} else {
							planet.setGravity(0.0008f);
						}
					}
					if(planet.isHit()) {
						gameOverColor.a += 0.0025f;
					}
				}
			}
			countdown -= delta;
		} else {
			timer += 0.2f;
			if(timer % 1 < 0.2f) {
				if(!renderedStory && timer % 1 < 0.2f && !renderedLine) {
					beepSound.play();
				} 
			} if(!readyRendering) {
				delayInMilliseconds -= delta;
			}
		}
	}
	
	private void save() {
		System.out.println("Saving");
		List<String> tempList = new ArrayList<String>();
		hasSaved = true;
		wasHighscore = getCurrentSave().setHighscore(level.getName(), (int) score);
		for(Ware ware : getItemHandler().getItems()) {
			tempList.add(Integer.toString(ware.getLevel()));
			tempList.add(Integer.toString(ware.getPrice()));
			tempList.add(Boolean.toString(ware.isUpgradeable()));
			tempList.add(Boolean.toString(ware.isMaxUpgraded()));
			
			getCurrentSave().setItem(ware.getName(), tempList);
			tempList.clear();
		}
		getCurrentSave().save();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if(enteredLevel) {
			super.render(container, game, g);
			if(planet.isHit()) {
				gameOver.draw((container.getWidth() - gameOver.getWidth()) / 2, (container.getHeight() - gameOver.getHeight()) / 2, gameOverColor);
			} else if(level.hasWon()) {
				youWinImg.draw((container.getWidth() - youWinImg.getWidth()) / 2, (container.getHeight() - youWinImg.getHeight()) / 2, gameOverColor);
				g.setFont(font32);
				if(hasSaved && wasHighscore) {
					g.setColor(new Color(1, 1, 0, gameOverColor.a - 0.5f));
					String str = "New highscore!";
					g.drawString(str, (container.getWidth() - font32.getWidth(str)) / 2, container.getHeight() / 2 + 50);
				}
				g.setColor(new Color(1, 1, 1, gameOverColor.a - 0.5f));
				String str = "Score: " + (int) score;
				g.drawString(str, (container.getWidth() - font32.getWidth(str)) / 2, container.getHeight() / 2 + 100);
				
				g.setColor(new Color(1, 1, 1, gameOverColor.a - 0.75f));
				str = "Destroyed debris: " + (int) debrisScore;
				g.drawString(str, (container.getWidth() - font32.getWidth(str)) / 2, container.getHeight() / 2 + 150);
				g.setColor(new Color(1, 1, 1, gameOverColor.a - 0.75f));
				str = "Missiles: " + (int) scoreFromMissiles;
				g.drawString(str, (container.getWidth() - font32.getWidth(str)) / 2, container.getHeight() / 2 + 200);
			} else {
				g.drawString("Missiles: " + missiles, 10, 35);
				g.drawString("Score: " + score, 10, 60);
			}
			
			if(countdown >= 1000) {
			font32.drawString((container.getWidth() - 
					font32.getWidth(Integer.toString((int) Math.floor(countdown / 1000 )))) / 2,
					(container.getHeight() - font32.getHeight()) / 2,
					Integer.toString((int) Math.floor(countdown / 1000 )));
			}
		} else {
			g.setColor(Color.white);
			g.setFont(font16);
			
			if(!renderedStory) {
				readyFaded = renderStory(story[storyDialog], g);
				if(readyFaded) {
					renderedLine = false;
					alpha = 1f;
					delayInMilliseconds = 5000;
					timer = 0;
					storyDialog++;
					if(storyDialog >= story.length) {
						renderedStory = true;
					}
				}
			}
			
			if(renderedStory) {
				enteredLevel = true;
			}
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if(!enteredLevel) {
			enteredLevel = true;
		} else if(countdown <= 1000){
			super.mousePressed(button, x, y);
			if(gameOverColor.a > 0.7) {
				score = debrisScore + scoreFromMissiles;
				game.enterState(Launch.SHOPSTATE);
			}
		}
	}

	/**
	 * This method is used to render dialogs in  story
	 * @param sentence - An array of the dialogs
	 * @param g A - graphics context to which the text is drawn
	 * @return Returns true if ready writing
	 */
	public boolean renderStory(String sentence, Graphics g) {
		boolean ready = false;
		if(Util.renderTextLineWithRandomCharAtEnd(sentence, g, alpha, 30, container.getHeight() - 60, container.getHeight() - 90, timer)) {
			renderedLine = true;
			if(alpha <= 0) {
				ready = true;
			} else if(delayInMilliseconds <= 2500) {
				alpha  -= 0.01;
			}
		}
		return ready;
	}
	
	/**
	 * Resets all values needed for the intro
	 */
	private void resetIntro() {
		enteredLevel = false;
		renderedStory = false;
		renderedLine = false;
		readyFaded = false;
		readyRendering = false;
		
		delayInMilliseconds = 5000;
		countdown = 4000;
		timer = 0;
		storyDialog = 0;
		alpha = 1f;
		story = level.getStory();
	}
	
	public void setLevel(Level level) {
		this.level = level;
	}
	

	@Override
	public void debrisDestroyed(Debris debris) {
		debrisScore += debris.getScore();
	}
}
