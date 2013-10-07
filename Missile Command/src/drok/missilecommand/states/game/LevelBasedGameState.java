package drok.missilecommand.states.game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Launch;
import drok.missilecommand.Level;
import drok.missilecommand.Planet;
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
	private boolean enteredLevel, renderedInfo, renderedStory;
	private boolean ready, renderedLine;
	private boolean skipIntro;
	private int storyDialog = 0;
	
	private static Image youWinImg;
	
	public LevelBasedGameState(int state) {
		super(state);
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		planet = new Planet(container.getWidth() / 2 / SCALE, container.getHeight() / 2 / SCALE, level.getPlanetName());
		System.out.println("planet is set");
		super.enter(container, game);
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
//		level.restart();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(enteredLevel) {
			if(countdown <= 1000) {
				super.update(container, game, delta);
				if(!paused) {
					level.update(delta, this);
					
					if(level.hasWon() || planet.isHit()) {
						if(gameOverColor.a < 1.5f) {
							gameOverColor.a += 0.0025f;
						}
					}
				}
			}
			countdown -= delta;
		} else {
			timer += 0.2f;
			if(!renderedInfo && timer % 1 < 0.2f && !ready) {
				beepSound.play();
			} else if(!renderedStory && renderedInfo && timer % 1 < 0.2f && !renderedLine) {
				beepSound.play();
			} else if(!ready && renderedInfo) {
				delayInMilliseconds -= delta;
			} else if(ready) {
				delayInMilliseconds -= delta;
			}
		}
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if(enteredLevel) {
			super.render(container, game, g);
			if(level.hasWon() || planet.isHit()) {
				g.setFont(font32);
				g.setColor(new Color(1, 1, 1, gameOverColor.a - 0.5f));
				g.drawString("Score: " + score, (container.getWidth() - font32.getWidth("Score: " + score)) / 2, container.getHeight() / 2 + 100);
				
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
			if(!skipIntro) {
				g.setColor(Color.white);
				
				//Rendering level name
				if(!renderedInfo) {
					ready = Util.renderTextLineWithRandomCharAtEnd("Level: " + level.getName(), g, alpha, 30, container.getHeight() - 60, container.getHeight() - 90, timer);
					if(ready) {
						if(delayInMilliseconds <= 0) {
							//Runs once
							timer = 0;
							delayInMilliseconds = 5000;
							alpha = 1f;
							renderedInfo = true;
						} else if(delayInMilliseconds <= 2500) {
							alpha  -= 0.01;
	//						txtX += 0.2;
						}
					}
				} else if(!renderedStory) {
					ready = renderStory(story[storyDialog], g);
					if(ready) {
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
				
				if(renderedInfo && renderedStory) {
					enteredLevel = true;
				}
			} else {
				enteredLevel = true;
			}
		}
	}

	@Override
	protected void renderScaled(Graphics g) {
		if(enteredLevel) {
			super.renderScaled(g);
			if(planet.isHit()) {
				gameOver.draw((screenImg.getWidth() - gameOver.getWidth()) / 2, (screenImg.getHeight() - gameOver.getHeight()) / 2, gameOverColor);
			} else if(level.hasWon()) {
				youWinImg.draw((screenImg.getWidth() - gameOver.getWidth()) / 2, (screenImg.getHeight() - gameOver.getHeight()) / 2, gameOverColor);
			}
		}
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		super.mousePressed(button, x, y);
		if(gameOverColor.a > 0.7) {
			game.enterState(Launch.SHOPSTATE);
		} else if(!enteredLevel) {
			skipIntro = true;
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
			if(delayInMilliseconds <= 0) {
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
		renderedInfo = false;
		renderedStory = false;
		renderedLine = false;
		ready = false;
		skipIntro = false;
		
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
	
	public int getScore() {
		return super.getScore();
	}
}
