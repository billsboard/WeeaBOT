package ca.billweb;

import discord4j.common.util.Snowflake;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class PlayerCache {

    public static ArrayList<Player> loaded = new ArrayList<>();

    static Player loadPlayer(long id) throws IOException {
        File pFile = new File(Variables.saveDir, id + ".json");
        if(!pFile.exists()){
            Player p = new Player(Snowflake.of(id));
            loaded.add(p);
            return p;
        }
        else {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(pFile)));
            String s; StringBuilder sb = new StringBuilder();
            while ((s = in.readLine()) != null){
                sb.append(s);
            }

            JSONObject obj = new JSONObject(sb.toString());

            Player p = new Player(Snowflake.of(id));
            p.money = obj.getInt("money");
            p.active = Variables.characters[obj.getInt("active")];

            JSONArray charArr = obj.getJSONArray("characters");
            for (int i = 0; i < charArr.length(); i++) {
                int charID = charArr.getInt(i);
                p.addChar(charID);
            }

            JSONObject cooldownArr = obj.getJSONObject("cooldowns");
            p.cooldowns.put("crime", cooldownArr.getLong("crime"));
            p.cooldowns.put("daily", cooldownArr.getLong("daily"));
            p.cooldowns.put("work", cooldownArr.getLong("work"));
            p.cooldowns.put("attack", cooldownArr.optLong("attack"));
            p.cooldowns.put("quiz", cooldownArr.optLong("quiz"));
            p.cooldowns.put("swap", cooldownArr.optLong("swap"));

            p.started = obj.getBoolean("start");

            loaded.add(p);
            return p;
        }
    }

    static Player getLoadedPlayer(long id){
        for (Player p : loaded) {
            if(p.id == id) return p;
        }
        return null;
    }

    static boolean isPlayerLoaded(long id){
        for (Player p : loaded) {
            if(p.id == id) return true;
        }
        return false;
    }

    static Player getPlayer(long id) throws IOException {
        Player p = getLoadedPlayer(id);
        if(p == null) return loadPlayer(id);
        return p;
    }

    public static void savePlayer(long id) throws IOException {
        Player p = getLoadedPlayer(id);
        if(p == null) return;

        File f = new File(Variables.saveDir, id + ".json");
        BufferedWriter out = new BufferedWriter(new FileWriter(f));

        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("money", p.money);
        obj.put("active", p.active == null ? 0 : p.active.id);

        JSONArray arr = new JSONArray();
        for (AniCharacter ac : p.charInv){
            if(ac != null){
                arr.put(ac.id);
            }
        }

        JSONObject cooldowns = new JSONObject(p.cooldowns);
        obj.put("cooldowns", cooldowns);
        obj.put("start", p.started);

        obj.put("characters", arr);
        out.write(obj.toString());
        out.close();
    }

}
