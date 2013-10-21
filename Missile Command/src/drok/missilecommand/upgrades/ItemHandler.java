package drok.missilecommand.upgrades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import drok.missilecommand.GameSave;

public class ItemHandler {
	//Fields
	private List<Ware> items = new ArrayList<Ware>();
	
	public ItemHandler() {
		items.clear();
	}
	
	public void save(GameSave save) {
		List<String> tempList = new ArrayList<String>();
		save.clearItems();
		for(Ware ware : getItems()) {
			tempList.add(Integer.toString(ware.getLevel()));
			tempList.add(Integer.toString(ware.getPrice()));
			tempList.add(Boolean.toString(ware.isUpgradeable()));
			tempList.add(Boolean.toString(ware.isMaxUpgraded()));
			
			save.setItem(ware.getName(), tempList);
			tempList.clear();
		}
	}
	
	public void addItem(Ware ware) {
		items.add(ware);
	}
	
	public void addItems(Collection<Ware> wares) {
		items.addAll(wares);
	}
	
	public void removeItem(Ware ware) {
		items.remove(ware);
	}
	
	public void clear() {
		items.clear();
	}
	
	public Ware getItem(Ware ware) {
		if(isOwned(ware)) {
			return ware;
		} else {
			System.err.println("No such ware");
			return null;
		}
	}
	
	public List<Ware> getItems() {
		return items;
	}
	
	public boolean isOwned(Ware ware) {
		for(Ware w : items) {
			if(w.getClass().equals(ware.getClass())) {
				return true;
			}
		}
		return false;
	}
}
