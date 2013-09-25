package drok.missilecommand;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import drok.missilecommand.debris.Debris;
import drok.missilecommand.states.game.GameState;

public class Level {

	private List<Spawnable> initalSpawnables = new ArrayList<Spawnable>();
	private List<Spawnable> spawnables = new ArrayList<Spawnable>();
	private boolean hasWon;
	private int timeSinceLastDebris;
	private Random random;

	private int spawnTime;
	private String planetName, name;
	private int missileCount;
	
	public Level(File levelFile) throws Exception {
		random = new Random();
		load(levelFile);
		spawnables.addAll(initalSpawnables);
		for(Spawnable sp : spawnables) { // Testing for corrupt level-file
			sp.getDebris(0, 0, 0, 0, null);
			sp.spawnCount++;
		}
	}
	
	public void restart() {
		spawnables.clear();
		spawnables.addAll(initalSpawnables);
		for(Spawnable s : spawnables)
			s.reset();
		hasWon = false;
	}
	
	public void update(int delta, GameState gs) {
		timeSinceLastDebris += delta;
		if(timeSinceLastDebris >= spawnTime && !gs.getPlanet().isHit()) {
			if(!spawnables.isEmpty()) {
				double rand = Math.random();
				float x, y;
				if(rand > 0.5) {
					y = (float) (Math.random() * gs.getScreenHeight());
					if(rand > 0.75)
						x = -16;
					else
						x = gs.getScreenWidth() + 16;
				} else {
					x = (float) (Math.random() * gs.getScreenWidth());
					if(rand > 0.25)
						y = -16;
					else
						y = gs.getScreenHeight() + 16;
				}
				int randInt = random.nextInt(spawnables.size());
				Spawnable sp = spawnables.get(randInt);
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
		FileReader fr = null;
		try {
			JSONParser jsonParser = new JSONParser();
			fr = new FileReader(file);
			JSONObject obj = (JSONObject) jsonParser.parse(fr);
			JSONObject spw = (JSONObject) obj.get("spawnables");
			initalSpawnables.clear();
			for(Entry<?, ?>  s : (Set<Map.Entry<?, ?>>) spw.entrySet()) {
				initalSpawnables.add(new Spawnable(((Long) s.getValue()).intValue(), Class.forName("drok.missilecommand.debris." + String.valueOf(s.getKey()))));
			}
			spawnTime = ((Long) obj.get("spawnTime")).intValue();
			planetName = (String) obj.get("planetName");
			name = (String) obj.get("name");
			missileCount = ((Long) obj.get("missiles")).intValue();
		} finally {
			if(fr != null)
				fr.close();
		}
	}
	
	public boolean hasWon() {
		return hasWon;
	}
	
	public int getSpawnTime() {
		return spawnTime;
	}
	
	public static class Spawnable {
		private int spawnCount, initialSpawnCount;
		private Class<?> spawnType;
		private String spawnTypeName;
		
		public Spawnable(int spawnCount, Class<?> spawnType) {
			this.spawnCount = spawnCount;
			this.initialSpawnCount = spawnCount;
			this.spawnType = spawnType;
			//Insert space in front of capital letters
			spawnTypeName = spawnType.getName().substring(spawnType.getName().lastIndexOf(".") + 1).replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
		}

		public void reset() {
			spawnCount = initialSpawnCount;
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
		
		public String getSpawnTypeName() {
			return spawnTypeName;
		}
		
		@Override
		public String toString() {
			return "Spawnable [spawnCount=" + spawnCount + ", spawnType="
					+ spawnType + "]";
		}

		public int getAmount() {
			return spawnCount;
		}
	}
	
	public String getPlanetName() {
		return planetName;
	}
	
	public String getName() {
		return name;
	}

	public List<Spawnable> getSpawnables() {
		return spawnables;
	}

	public int getMissileCount() {
		return missileCount;
	}
}
