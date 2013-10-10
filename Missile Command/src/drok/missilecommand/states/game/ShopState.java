package drok.missilecommand.states.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import drok.missilecommand.Launch;
import drok.missilecommand.states.State;
import drok.missilecommand.upgrades.Shield;
import drok.missilecommand.upgrades.Ware;
import drok.missilecommand.utils.Button;
import drok.missilecommand.utils.ResourceManager;
import drok.missilecommand.utils.Util;
import drok.missilecommand.weapons.Nuke;
import drok.missilecommand.weapons.Probe;

public class ShopState extends State {
	//Fields
	private List<Ware> wares = new ArrayList<Ware>();
	private List<Ware> supplements = new ArrayList<Ware>();
	private List<Ware> upgrades = new ArrayList<Ware>();
	
	private int shieldLevel;
	private List<Button> buttons = new LinkedList<Button>();
	private Input input;
	private Button buy, back;
	private int selectedBtnIndex;
	private int money = 0;
	
	public ShopState(int state) {
		super(state);
	}
	
	@Override
	public void firstTimeEnter() throws SlickException {
		super.firstTimeEnter();
		input = container.getInput();
		buy = new Button(container.getWidth() * 3 / 4 - 100, container.getHeight() * 3 / 4 + 3, 100, 30, "BUY", Color.green, Color.white);
		back = new Button(10, 10, ResourceManager.getImage("BackButton.png").getWidth(), ResourceManager.getImage("BackButton.png").getHeight(), ResourceManager.getImage("BackButton.png"), 1);
		shieldLevel = 1;
		upgrades.clear();
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		//How mouch money do you have
		money += ((LevelBasedGameState) game.getState(Launch.LEVELGAMESTATE)).getScore();
		
		int buttonAmount;
		for(Ware w : wares)
			if(w.getName().equals("Shield"))
				shieldLevel = w.getLevel();
		
		//Clearing lists
		buttons.clear();
		wares.clear();
		supplements.clear();
		
		//Adding wares to the list
		wares.add(new Shield(((LevelBasedGameState) game.getState(Launch.LEVELGAMESTATE)).getPlanet(), shieldLevel, ResourceManager.getImage("Shield.png"), (LevelBasedGameState) game.getState(Launch.LEVELGAMESTATE)));
		wares.add(new Probe(container.getWidth() / 2, container.getHeight() / 2, (LevelBasedGameState) game.getState(Launch.LEVELGAMESTATE), 5000));
		wares.add(new Nuke(((LevelBasedGameState) game.getState(Launch.LEVELGAMESTATE)).getPlanet(), container, (LevelBasedGameState) game.getState(Launch.LEVELGAMESTATE)));
		
		//Adding atleast 10 buttons
		if(wares.size() < 10)
			buttonAmount = 10;
		else
			buttonAmount = wares.size();
		for(int i = 0; i < buttonAmount; i++) {
			if(i >= wares.size())	//Empty buttons
				buttons.add(new Button(container.getWidth() / 4, container.getHeight() / 4 + i * container.getHeight() / 20, container.getWidth() / 4, container.getHeight() / 20, "-", new Color(10, 10, 10), Color.white));
			else	//Buttons with wares
				buttons.add(new Button(container.getWidth() / 4, container.getHeight() / 4 + i * container.getHeight() / 20, container.getWidth() / 4, container.getHeight() / 20, wares.get(i).getName(), new Color(10, 10, 10), Color.white));
		}
	}
	
	

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//Changing color of the button upon hoover, and changing Ware information
		for(int i = 0; i < buttons.size(); i++) {
			if(buttons.get(i).isSelected()) {
				buttons.get(i).setColor(new Color(50, 50, 50));
				for(int j = 0; j < wares.size(); j++) {
					if(buttons.get(i).getText().equalsIgnoreCase(wares.get(j).getName())) {
						Util.drawLines(g, font16, wares.get(j).getDescription(font16, container.getWidth() / 4 - 40), container.getWidth() / 2 + 20, container.getHeight() / 4 + 20);
						Util.drawLines(g, font16, "Price: " + wares.get(j).getPrice(), container.getWidth() * 3 / 4 -  font16.getWidth("Price: " + wares.get(j).getPrice()) - 10, container.getHeight() * 3 / 4 - font16.getHeight() - 10);
					}
				}
			}
			buttons.get(i).render(g, font16);
		}
		g.drawRect(container.getWidth() / 4, container.getHeight() / 4, container.getWidth() / 2, container.getHeight() / 2);
		font16.drawString(container.getWidth() / 4, container.getHeight() / 4 - font16.getHeight() - 10,"Money: " + money);
		buy.render(g, font16);
		back.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		handleButtons();
		
		if(wares.size() > 0 && selectedBtnIndex < wares.size() && money >= wares.get(selectedBtnIndex).getPrice()) {
			for(Ware w : upgrades) {
				if(wares.get(selectedBtnIndex).isUpgradeable() && w.getClass().equals(wares.get(selectedBtnIndex).getClass())) {
					buy.setText("UPGRADE");
					break;
				} else if(upgrades.indexOf(w) + 1 == upgrades.size()) {
					buy.setText("BUY");
				}
			}
			
			
			if(buy.clicked(container))
				buy(selectedBtnIndex);
		} else {
			buy.setColor(new Color(10, 10, 10));
		}
		
		if(back.hoverOver(input.getMouseX(), input.getMouseY())) {
			back.changeImage(ResourceManager.getImage("BackButtonHover.png"));
			if(back.clicked(container))
				game.enterState(Launch.LEVELSELECTSTATE);
		} else {
			back.changeImage(ResourceManager.getImage("BackButton.png"));
		}
	}
	
	/**
	 * Handles updating of ware buttons
	 */
	private void handleButtons() {
		for(int i = 0; i < buttons.size(); i++) {
			Button btn = buttons.get(i);
			
			if(btn.hoverOver(input.getMouseX(), input.getMouseY())) {
				//Makes button light grey
				btn.setColor(new Color(1f, 1f, 1f, 0.5f));
				
				if(btn.clicked(container)) {
					//Sets all buttons unselected
					for(int j = 0; j < buttons.size(); j++) {
						buttons.get(j).setSelected(false);
					}
					//Makes clicked button selected
					btn.setSelected(true);
				}
			} else {//if(btn.getColor().r == 1f) {
				//Makes button grey
				btn.setColor(new Color(10, 10, 10));
			}
			
			if(btn.isSelected())
				selectedBtnIndex = buttons.indexOf(btn);
		}
	}
	
	/**
	 * Used to buy wares
	 * @param index - The index of the ware to buy
	 */
	private void buy(int index) {
		//Variables
		Ware ware = wares.get(index);
		
		//Checks if ware should be bought or upgraded
		if(ware.isUpgradeable() && upgrades.contains(ware)) {
			upgrades.get(upgrades.indexOf(ware)).upgrade();
		} else {
			//Removes button
			buttons.remove(index);
			
			//Moves and sets all buttons to not selected
			for(Button b : buttons) {
				b.setSelected(false);
				if(buttons.indexOf(b) >= index)
					b.setY(b.getY() - b.getHeight());
			}
			
			//Adding button
			if(wares.size() < 10)
				buttons.add(buttons.size(), new Button(container.getWidth() / 4, container.getHeight() / 4 + 9 * container.getHeight() / 20, container.getWidth() / 4, container.getHeight() / 20, "-", new Color(10, 10, 10), Color.white));
			else
				buttons.add(new Button(container.getWidth() / 4, container.getHeight() / 4 + (wares.size() - 1) * container.getHeight() / 20, container.getWidth() / 4, container.getHeight() / 20, "-", new Color(10, 10, 10), Color.white));
			
			//Decrease money amount
			money -= ware.getPrice();
			
			//Add a ware to bought or upgrades list
			if(ware.isUpgradeable()) {
				upgrades.add(ware);
			} else {
				supplements.add(ware);
			}
			
			//Removing the ware from the wares list
			wares.remove(ware);
			ware = null;
			
			//Sets first ware to selected
			buttons.get(0).setSelected(true);
		}
	}
	
	public List<Ware> getSuplementWares() {
		return supplements;
	}
	
	public List<Ware> getUpgrades() {
		return upgrades;
	}
}
