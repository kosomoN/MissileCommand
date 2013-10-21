package drok.missilecommand.states.menu;

import java.io.File;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Launch;
import drok.missilecommand.states.State;
import drok.missilecommand.utils.Button;
import drok.missilecommand.utils.ResourceManager;

public class GameSaveState extends State {
	//Fields
//	private ArrayList<GameSave> saves = new ArrayList<GameSave>();
	private ArrayList<Button> buttons = new ArrayList<Button>();
	private File savesFolder;
	private Button create, load;
	private Input input;
	private GameContainer container; 
	
	
	public GameSaveState(int state) {
		super(state);
		savesFolder = new File("res/data/saves");
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		this.container = container;
		input = container.getInput();
		
		Image img = ResourceManager.getImage("LoadAndCreateButtons.png");
		
		load = new Button(img.getSubImage(0, 0, 64, 32), 1);
		load.setDimensions(64, 32);
		load.setX(container.getWidth() * 3 / 4 - 64 * 2 - 50);
		load.setY(container.getHeight() * 3 / 4 + 10);
		
		create = new Button(img.getSubImage(0, 33, 64, 32), 1);
		create.setDimensions(64, 32);
		create.setX(container.getWidth() * 3 / 4 - 64);
		create.setY(container.getHeight() * 3 / 4 + 10);
		
		loadSaves(savesFolder);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		super.render(container, game, g);
		
		for(Button b : buttons) {
			b.renderWithoutBorder(g, font16);
		}
		
		g.drawRect(container.getWidth() / 4, container.getHeight() / 4, container.getWidth() / 2, container.getHeight() / 2);
		
		load.renderImage(g);
		create.renderImage(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if(!buttons.isEmpty() && load.hoverOver(container.getInput().getMouseX(), container.getInput().getMouseY())) {
			load.changeImage(ResourceManager.getImage("LoadAndCreateButtons.png").getSubImage(64, 0, 64, 32));
			if(load.clicked(container)) {
				game.enterState(Launch.LEVELSELECTSTATE);
			}
		} else {
			load.changeImage(ResourceManager.getImage("LoadAndCreateButtons.png").getSubImage(0, 0, 64, 32));
		}
		
		if(create.hoverOver(container.getInput().getMouseX(), container.getInput().getMouseY())) {
			create.changeImage(ResourceManager.getImage("LoadAndCreateButtons.png").getSubImage(64, 32, 64, 32));
			if(create.clicked(container)) {
				game.enterState(Launch.GAMEMODESTATE);
			}
		} else {
			create.changeImage(ResourceManager.getImage("LoadAndCreateButtons.png").getSubImage(0, 32, 64, 32));
		}
		
		handleButtons(container);
	}
	
	private void handleButtons(GameContainer container) {
		for(Button b : buttons) {
			if(b.isSelected()) {
				b.setBackColor(new Color(1f, 1f, 1f, 0.5f));
			}
			
			if(b.hoverOver(input.getMouseX(), input.getMouseY())) {
				System.out.println(b.isSelected());
				b.setBackColor(new Color(10, 10, 10));	
				if(b.clicked(container)) {
					b.setSelected(true);
				}
			}
		}
	}

	/**
	 * Loads saves from a folder
	 * @param folder - The folder to load saves from
	 */
	private void loadSaves(File folder) {
		//clearing buttons
		buttons.clear();
		
		//Checking that file is a directory
		while(!folder.isDirectory()) {
			folder = folder.getParentFile();
		}
		
		File f;
		for(int i = 0; i < folder.listFiles().length; i++) {
			f = folder.listFiles()[i];
			if(f.isFile()) {
				try {
					Button button = new Button(f.getName().substring(0, f.getName().lastIndexOf(".")), new Color(0, 0, 0, 0), Color.white, 1);
					button.setDimensions(container.getWidth() / 2, font16.getHeight() + 4);
					button.setX(container.getWidth() / 4);
					button.setY(container.getHeight() / 2 - button.getHeight() * (buttons.size() + 1) / 2 + i * 30);
					buttons.add(button);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		if(!buttons.isEmpty()) {
			buttons.get(0).setSelected(true);
		}
	}
	
	/**
	 * 
	 * @param newPath - The new path to the folder
	 */
	public void changeSaveFolder(String newPath) {
		savesFolder.renameTo(new File(newPath));
		loadSaves(savesFolder);
	}
}
