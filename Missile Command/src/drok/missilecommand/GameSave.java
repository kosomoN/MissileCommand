package drok.missilecommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.util.Log;

import drok.missilecommand.upgrades.Ware;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class GameSave {
	private static final boolean OBFUSCATE_FILE = false;
	
	private Map<String, Long> highscores = new HashMap<String, Long>(); //JSON uses long
	private boolean firstTimePlaying = false;
	private String name;
	private List<Ware> items = new ArrayList<Ware>();
	
	@SuppressWarnings("unchecked") //Let it throw the error in case of failure
	public GameSave(String name) throws Exception {
		this.name = name;
		File file = new File("res/data/saves/" + name + ".sav");
		if(file.exists()) {
			BufferedReader br = null; 
			try {
				JSONParser jsonParser = new JSONParser();
				br = new BufferedReader(new FileReader(file));
				StringBuffer sb = new StringBuffer();
				String line = null;
				while((line = br.readLine()) != null){
					sb.append(line).append("\n");
				}
				JSONObject obj;
				try {
					if(OBFUSCATE_FILE)
						obj = (JSONObject) jsonParser.parse(new String(new BASE64Decoder().decodeBuffer(sb.toString())));
					else
						obj = (JSONObject) jsonParser.parse(sb.toString());
				} catch (ParseException e) {
					System.err.println("File not in specified format, trying the other one");
					e.printStackTrace();
					if(!OBFUSCATE_FILE)
						obj = (JSONObject) jsonParser.parse(new String(new BASE64Decoder().decodeBuffer(sb.toString())));
					else
						obj = (JSONObject) jsonParser.parse(sb.toString());
				}
				highscores = (Map<String, Long>) obj.get("Highscores");
				
				Log.info("Save file loaded");
			} finally {
				if(br != null)
					br.close();
			}
		} else {
			firstTimePlaying = true;
			save();
		}
	}

	@SuppressWarnings("unchecked") //Let it throw the error in case of failure
	public void save() {
		FileOutputStream fos = null;
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("Highscores", highscores);
			File file = new File("res/data/saves/");//" + name + ".sav");
			if(!file.exists())
				file.mkdirs();
			
			file = new File("res/data/saves/" + name + ".sav");
			fos = new FileOutputStream(file);
			if(OBFUSCATE_FILE)
				fos.write(new BASE64Encoder().encode(jsonObj.toJSONString().getBytes()).getBytes());
			else
				fos.write(jsonObj.toJSONString().getBytes());
			fos.flush();
			fos.close();
		} catch (IOException e) {
			System.err.println("Failed to save game!");
			e.printStackTrace();
		} finally {
			try {
				if(fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isFirstTimePlaying() {
		return firstTimePlaying;
	}
	
	public boolean hasWonLevel(Level level) {
		return highscores.containsKey(level.getName());
	}

	public int getHighscore(Level level) {
		return (int) (highscores.containsKey(level.getName()) ? highscores.get(level.getName()) : -1);
	}
	
	/**
	 * 
	 * @param name Name of the level
	 * @param score The score to submit
	 * @return True if the highscore was higher then the last
	 */
	public boolean setHighscore(String name, int score) {
		if(highscores.containsKey(name)) {
			if(highscores.get(name) < score) {
				highscores.put(name, (long) score);
				return true;
			}
		} else
			highscores.put(name, (long) score);
		
		return false;
	}
}
