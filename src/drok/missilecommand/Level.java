package drok.missilecommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import drok.missilecommand.debris.Debris;
import drok.missilecommand.states.game.GameState;

public class Level {

	private GameState gs;
	private List<Spawnable> spawnables = new ArrayList<Spawnable>();
	private boolean hasWon;
	private int timeSinceLastDebris;
	private Random random;
	private int spawnTime;
	
	public Level(GameState gs, File levelFile) throws Exception {
		this.gs = gs;
		random = new Random();
		load(levelFile);
	}
	
	public void update(int delta, GameState gs) {
		timeSinceLastDebris += delta;
		if(timeSinceLastDebris >= spawnTime && !gs.getPlanet().isHit()) {
			if(!spawnables.isEmpty()) {
				int rand = random.nextInt(4);
				float x, y;
				if(rand >= 1) {
					y = (float) (Math.random() * gs.getScreenHeight());
					if(rand == 0)
						x = -16;
					else
						x = gs.getScreenWidth() + 16;
				} else {
					x = (float) (Math.random() * gs.getScreenWidth());
					if(rand == 2)
						y = -16;
					else
						y = gs.getScreenHeight() + 16;
				}
				rand = random.nextInt(spawnables.size());
				Spawnable sp = spawnables.get(rand);
				gs.addEntity(sp.getDebris(x, y, 0.02f, (float) (Math.random() * 360), gs.getPlanet()));
				if(sp.isDoneSpawning())
					spawnables.remove(sp);
			} else if(gs.getDebris().isEmpty()){
				hasWon = true;
			}
			timeSinceLastDebris = 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void load(File file) throws Exception {
		JSONParser jsonParser = new JSONParser();
		JSONObject obj = (JSONObject) jsonParser.parse(new FileReader(new File("res/levels/test.misscommlvl")));
		System.out.println(obj.get("spawnables"));
		JSONObject spw = (JSONObject) obj.get("spawnables");
		for(Entry<?, ?>  s : (Set<Map.Entry<?, ?>>) spw.entrySet()) {
			spawnables.add(new Spawnable(((Long) s.getValue()).intValue(), Class.forName("drok.missilecommand.debris." + String.valueOf(s.getKey()))));
		}
		spawnTime = ((Long) obj.get("spawnTime")).intValue();
	}
	
	public boolean hasWon() {
		return hasWon;
	}
	
	public static class Spawnable {
		private int spawnCount;
		private Class<?> spawnType;
		
		public Spawnable(int spawnCount, Class<?> spawnType) {
			this.spawnCount = spawnCount;
			this.spawnType = spawnType;
		}

		public boolean isDoneSpawning() {
			return spawnCount <= 0;
		}

		public Debris getDebris(float x, float y, float speed, float direction, Planet planet) {
			try {
				spawnCount--;
				return (Debris) spawnType.getConstructors()[0].newInstance(x, y, speed, direction, planet);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public String toString() {
			return "Spawnable [spawnCount=" + spawnCount + ", spawnType="
					+ spawnType + "]";
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException {
		JSONObject jsonObj = new JSONObject();
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("Asteroid", 20);
		jsonObj.put("spawnables", map);
		jsonObj.put("spawnTime", new Integer(500));
		FileOutputStream fos = new FileOutputStream("res/levels/test.misscommlvl");
		fos.write(jsonObj.toJSONString().getBytes());
		fos.flush();
		fos.close();
	}
}
