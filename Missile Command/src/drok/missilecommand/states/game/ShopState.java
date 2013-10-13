package drok.missilecommand.states.game;

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
	//TODO
	/* 1. Unupgradeable wares can be stacked
	*/
	
	//Fields
	private List<Ware> wares = new LinkedList<Ware>();
	private List<Button> buttons = new LinkedList<Button>();
	
	private Input input;
	private Button buy, back;
	private int selectedBtnIndex;
	private int money = 50;
	
	public enum Wares {
		SHIELD, PROBE, NUKE;
	}
	
	public ShopState(int state) {
		super(state);
	}
	
	@Override
	public void firstTimeEnter() throws SlickException {
		super.firstTimeEnter();
		input = container.getInput();
		buy = new Button(container.getWidth() * 3 / 4 - 100, container.getHeight() * 3 / 4 + 23, 100, 30, "BUY", Color.green, Color.white);
		back = new Button(10, 10, ResourceManager.getImage("BackButton.png").getWidth(), ResourceManager.getImage("BackButton.png").getHeight(), ResourceManager.getImage("BackButton.png"), 1);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		getItemHandler().clear();
		getItemHandler().addItems(getCurrentSave().getItemsAsWares());
		
		
		//How mouch money do you have
		money += ((LevelBasedGameState) game.getState(Launch.LEVELGAMESTATE)).getScore();
		
		int buttonAmount;
		
		//Clearing lists
		buttons.clear();
		wares.clear();
		
		//Adding wares to the list
		wares.add(new Shield(ResourceManager.getImage("Shield.png")));
		wares.add(new Probe());
		wares.add(new Nuke());
		
		for(Ware w : getItemHandler().getItems()) {
			for(int i = 0; i < wares.size(); i++) {
				if(w.getName().equals(wares.get(i).getName())) {
					wares.add(i, w);
					wares.remove(i + 1);
					System.out.println("changed to: " + w.getName());
					break;
				} else if(w instanceof Shield) {
					if(w.getClass().equals(wares.get(i).getClass())) {
						wares.add(i, w);
						wares.remove(i + 1);
						break;
					}
				}
			}
		}
		
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
		
		buttons.get(0).setSelected(true);
	}
	
	

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.white);
		//Changing color of the button upon hoover, and changing Ware information
		for(int i = 0; i < buttons.size(); i++) {
			if(buttons.get(i).isSelected()) {
				buttons.get(i).setColor(new Color(50, 50, 50));
				for(int j = 0; j < wares.size(); j++) {
					if(buttons.get(i).getText().equalsIgnoreCase(wares.get(j).getName())) {
						Util.drawLines(g, font16, wares.get(j).getDescription(font16, container.getWidth() / 4 - 40), container.getWidth() / 2 + 20, container.getHeight() / 4 + 20);
						Util.drawLines(g, font16, "Price: " + wares.get(j).getPrice(), container.getWidth() * 3 / 4 -  font16.getWidth("Price: " + wares.get(j).getPrice()) - 10, container.getHeight() * 3 / 4 - font16.getHeight() - 10);
						Util.drawLines(g, font16, "Owned: " + getWareAmount(getItemHandler().getItems(), wares.get(selectedBtnIndex)), container.getWidth() * 3 / 4 -  font16.getWidth("Owned: " + getWareAmount(getItemHandler().getItems(), wares.get(selectedBtnIndex))) - 10, container.getHeight() * 3 / 4 - 2 * font16.getHeight() - 15);
					}
				}
			}
			buttons.get(i).render(g, font16);
		}
		
		//Drawing outline
		g.drawRoundRect(container.getWidth() / 4 - 10, container.getHeight() / 4 - 10, container.getWidth() / 2 + 20, container.getHeight() / 2 + 20, 20);
		g.drawRect(container.getWidth() / 4, container.getHeight() / 4, container.getWidth() / 2, container.getHeight() / 2);
		
		//Draws money amount
		font16.drawString(container.getWidth() / 4, container.getHeight() / 4 - font16.getHeight() - 10,"Money: " + money);
		buy.render(g, font16);
		back.render(g);
		
		//Draws pop-up for buy button
		if(buy.hoverOver(input.getMouseX(), input.getMouseY())) {
			if(wares.get(selectedBtnIndex).isMaxUpgraded()) {
				g.setColor(Color.gray);
				g.fillRect(input.getMouseX(), input.getMouseY() - font12.getHeight(), font12.getWidth("Fully Upgraded") + 2, font12.getHeight());
				font12.drawString(input.getMouseX() + 1, input.getMouseY() - font12.getHeight(), "Fully Upgraded");
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		handleButtons();
		
		//Handling buy button
		buy.setColor(new Color(10, 10, 10));
		buy.setText("BUY");
		
		if(wares.size() > 0 && selectedBtnIndex < wares.size()) {
			if(money >= wares.get(selectedBtnIndex).getPrice()) {
				buy.setColor(Color.green);
			} else {
				buy.setColor(new Color(10, 10, 10));
			}
			
			if(getItemHandler().getItem(wares.get(selectedBtnIndex)).isUpgradeable()) {
				buy.setText("UPGRADE");
				if(getItemHandler().getItem(wares.get(selectedBtnIndex)).isMaxUpgraded()) {
					buy.setColor(new Color(10, 10, 10));
				}
			}
			
			if(buy.clicked(container)) {
				buy(selectedBtnIndex);
			}
		}
		
		//Updating back button
		if(back.hoverOver(input.getMouseX(), input.getMouseY())) {
			back.changeImage(ResourceManager.getImage("BackButtonHover.png"));
			if(back.clicked(container))
				game.enterState(Launch.LEVELSELECTSTATE);
		} else {
			back.changeImage(ResourceManager.getImage("BackButton.png"));
		}
	}
	
	private int getWareAmount(List<Ware> list, Ware ware) {
		int amount = 0;
		
		for(Ware w : list) {
			if(w.equals(ware)) {
				amount++;
			}
		}
		
		return amount;
	}
	
	/**
	 * Handles updating of ware buttons
	 */
	private void handleButtons() {
		for(int i = 0; i < buttons.size(); i++) {
			Button btn = buttons.get(i);
			if(i < wares.size())
				btn.setText(wares.get(i).getName());
			
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
		
		if(money >= ware.getPrice()) {
			//Decrease money amount
			money -= ware.getPrice();
			
			//Checks if ware should be bought or upgraded
			if(ware.isUpgradeable() && !ware.isMaxUpgraded()) {
				if(getItemHandler().isOwned(ware)) {
					getItemHandler().getItem(ware).upgrade();
					wares.add(selectedBtnIndex, getItemHandler().getItem(ware));
					wares.remove(selectedBtnIndex + 1);
				} else {
					getItemHandler().addItem(ware);
				}
			} else if(!ware.isUpgradeable()) {				
				/*//Moves and sets all buttons to not selected
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
				*/
				
				//Add a ware to owned items
				getItemHandler().addItem(ware);
				
				ware = null;
			}
		}
	}
	
	public List<Ware> getWares() {
		return wares;
	}
}
