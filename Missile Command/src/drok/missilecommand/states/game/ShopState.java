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

import drok.missilecommand.states.State;
import drok.missilecommand.upgrades.ShieldMK1;
import drok.missilecommand.upgrades.Upgrade;
import drok.missilecommand.utils.Button;
import drok.missilecommand.utils.Util;
import drok.missilecommand.weapons.Probe;

public class ShopState extends State {
	//Fields
	private List<Upgrade> upgrades = new ArrayList<Upgrade>();
	private List<Upgrade> bought = new ArrayList<Upgrade>();
	private Upgrade ware;
	private List<Button> buttons = new LinkedList<Button>();
	private Input input;
	private Button buy;
	private int money;
	
	public ShopState(int state) {
		super(state);
		
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		super.enter(container, game);
		//How mouch money do you have
		money = 10;
		
		int buttonAmount;
		input = container.getInput();
		buy = new Button(container.getWidth() * 3 / 4 - 100, container.getHeight() * 3 / 4 + 3, 100, 30, "BUY", Color.green, Color.white);
		
		//Adding upgrades to the list
		upgrades.add(new ShieldMK1(3));
		upgrades.add(new Probe());
		
		//Adding atleast 10 buttons
		if(upgrades.size() < 10)
			buttonAmount = 10;
		else
			buttonAmount = upgrades.size();
		for(int i = 0; i < buttonAmount; i++) {
			if(i >= upgrades.size())	//Empty buttons
				buttons.add(new Button(container.getWidth() / 4, container.getHeight() / 4 + i * container.getHeight() / 20, container.getWidth() / 4, container.getHeight() / 20, "-", new Color(10, 10, 10), Color.white));
			else	//Buttons with upgrades
				buttons.add(new Button(container.getWidth() / 4, container.getHeight() / 4 + i * container.getHeight() / 20, container.getWidth() / 4, container.getHeight() / 20, upgrades.get(i).getName(), new Color(10, 10, 10), Color.white));
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		//Changing color of the button upon hoover, and changing upgrade information
		for(int i = 0; i < buttons.size(); i++) {
			if(buttons.get(i).isSelected()) {
				buttons.get(i).setColor(new Color(50, 50, 50));
				for(int j = 0; j < upgrades.size(); j++) {
					if(buttons.get(i).getText().equalsIgnoreCase(upgrades.get(j).getName())) {
						Util.drawLines(g, font16, upgrades.get(j).getDescription(), container.getWidth() / 2 + 20, container.getHeight() / 2);
						Util.drawLines(g, font16, "Price: " + upgrades.get(j).getPrice(), container.getWidth() * 3 / 4 -  font16.getWidth("Price: " + upgrades.get(j).getPrice()) - 10, container.getHeight() * 3 / 4 - font16.getHeight() - 10);
					}
				}
			}
			buttons.get(i).render(g, font16);
		}
		g.drawRect(container.getWidth() / 4, container.getHeight() / 4, container.getWidth() / 2, container.getHeight() / 2);
		font16.drawString(container.getWidth() / 4, container.getHeight() / 4 - font16.getHeight() - 10,"Money: " + money);
		buy.render(g, font16);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		for(int i = 0; i < buttons.size(); i++) {
			Button btn = buttons.get(i);
			btn.setColor(new Color(10, 10, 10));
			if(btn.hoverOver(input.getMouseX(), input.getMouseY())) {
				btn.setColor(new Color(1f, 1f, 1f, 0.5f));
				if(btn.clicked(input.getMouseX(), input.getMouseY(), container)) {
					for(int j = 0; j < buttons.size(); j++) {
						buttons.get(j).setSelected(false);
					}
					btn.setSelected(true);
				}
			}
		}
		
		for(int i = 0; i < buttons.size(); i++) {
			if(buttons.get(i).isSelected()) {
				if(upgrades.size() > 0 && i < upgrades.size() && money >= upgrades.get(i).getPrice()) {
					//buy.setColor(Color.white);
					//Buying
					if(buy.clicked(input.getMouseX(), input.getMouseY(), container)) {
						if(buy()) {
							System.out.println(money);
							int buttonIndex = upgrades.indexOf(ware);
							buttons.remove(upgrades.indexOf(ware));
							for(int j = 0; j < buttons.size(); j++) {
								if(j >= buttonIndex)
									buttons.get(j).setY(buttons.get(j).getY() - buttons.get(j).getHeight());
								buttons.get(j).setSelected(false);
								
							}
							
							if(upgrades.size() < 10)
								buttons.add(buttons.size(), new Button(container.getWidth() / 4, container.getHeight() / 4 + 9 * container.getHeight() / 20, container.getWidth() / 4, container.getHeight() / 20, "-", new Color(10, 10, 10), Color.white));
							else
								buttons.add(new Button(container.getWidth() / 4, container.getHeight() / 4 + (upgrades.size() - 1) * container.getHeight() / 20, container.getWidth() / 4, container.getHeight() / 20, "-", new Color(10, 10, 10), Color.white));
								
							upgrades.remove(ware);
							bought.add(ware);
							ware = null;
							buttons.get(0).setSelected(true);
						}
					}
				} else {
					buy.setColor(new Color(10, 10, 10));
					break;
				}
			}
		}
	}
	
	public boolean buy() {
		for(int i = 0; i < upgrades.size(); i++) {
			if(buttons.get(i).isSelected() && upgrades.get(i).getPrice() <= money) {
				buttons.get(i).setSelected(false);
				ware = upgrades.get(i);
				money -= ware.getPrice();
				return true;
			}
		}
		return false;
	}
	
	public List<Upgrade> getBoughtUpgrades() {
		return bought;
	}
}
