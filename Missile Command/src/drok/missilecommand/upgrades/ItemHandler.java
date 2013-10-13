package drok.missilecommand.upgrades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ItemHandler {
	//Fields
	private List<Ware> items = new ArrayList<Ware>();
	
	public ItemHandler() {
		items.clear();
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
