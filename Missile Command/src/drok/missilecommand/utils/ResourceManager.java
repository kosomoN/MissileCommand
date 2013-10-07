package drok.missilecommand.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ResourceManager {
	private static Map<String, Image> imgs = new HashMap<String, Image>();
	
	public static void loadResources() {
		try {
			loadFiles(new File("res"));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private static void loadFiles(File file) throws SlickException {
		for(File f : file.listFiles()) {
			if(f.isDirectory())
				loadFiles(f);
			else {
				if(f.getName().substring(f.getName().lastIndexOf(".")).equals(".png")) {
					if(!imgs.containsKey(f.getName())) {
						String path = f.getPath().replaceAll("\\\\", "/");
						Image img = new Image(path);
						img.setFilter(Image.FILTER_NEAREST);
						imgs.put(f.getName(), img);
					} else {
						throw new RuntimeException("Duplicate image names: " + f.getName());
					}
				}
			}
		}
		
	}

	public static Image getImage(String string) {
		Image img = imgs.get(string);
		if(img == null) {
			throw new RuntimeException("No image with name: " + string);
		}
		return img;
	}
}
