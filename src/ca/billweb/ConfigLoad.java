package ca.billweb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;

public class ConfigLoad {
    static void load() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("config.json")));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = in.readLine()) != null){
            sb.append(s);
        }

        JSONObject obj = new JSONObject(sb.toString());
        Variables.BOT_TOKEN = obj.getString("token");
        Variables.PREFIX = obj.getString("prefix");

        Variables.IMG_PATH = new File(obj.getString("imgpath"));

        Variables.IMG_DIRS = new HashMap<>();
        for (File f : Variables.IMG_PATH.listFiles()) {
            Variables.IMG_DIRS.put(f.getName(), f.getAbsoluteFile());
        }

        Variables.aniDataPath = new File(obj.getString("anidata"));
        loadAniData(Variables.aniDataPath);

        Variables.saveDir = new File(obj.getString("savedir"));
        if(!Variables.saveDir.isDirectory()) Variables.saveDir.mkdir();

        Variables.guesspicJsonPath = new File(obj.getString("guesspicpath"));
        loadGuesspic(Variables.guesspicJsonPath);

    }

    static void loadAniData(File path) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = in.readLine()) != null){
            sb.append(s);
        }

        Variables.characters = new AniCharacter[200000];
        int maxID = Integer.MIN_VALUE;
        JSONObject obj = new JSONObject(sb.toString());
        for (String key : obj.keySet()) {
            JSONObject charData = obj.getJSONObject(key);
            Variables.characters[Integer.parseInt(key)] = new AniCharacter(charData);
            maxID = Math.max(maxID, charData.getInt("id"));
        }
        Variables.maxCharID = maxID;
    }

    static void loadGuesspic(File path) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = in.readLine()) != null){
            sb.append(s);
        }

        JSONArray arr = new JSONArray(sb.toString());
        for (Object o : arr) {
            JSONObject obj = (JSONObject) o;
            Variables.guesspicObj.add(obj);
        }
    }
}
