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
					String path = f.getPath().replaceAll("\\\\", "/");
					imgs.put(path, new Image(path));
					imgs.get(path).setFilter(Image.FILTER_NEAREST);
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
