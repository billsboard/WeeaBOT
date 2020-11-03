package ca.billweb;

import org.json.JSONObject;

public class AniCharacter {

    public String name;
    String media;
    public long stats;
    public int id;

    int rarity;
    int cost;

    public String imgURL;

    /* Stats:
    64 bit number: 00000000 loyalty intelligence luck speed charm attack defense
     */

    AniCharacter(String name, String media, long statMask, int id, String imgURL){
        this.name = name;
        this.media = media;
        this.stats = statMask;
        this.id = id;
        this.imgURL = "https://s4.anilist.co/file/anilistcdn/character/large/" + imgURL;
    }

    AniCharacter(String json){
        JSONObject obj = new JSONObject(json);
        this.name = obj.getString("name");
        this.media = obj.getString("media");
        this.stats = obj.getLong("stats");
        this.id = obj.getInt("id");
        this.imgURL = "https://s4.anilist.co/file/anilistcdn/character/large/" + obj.getString("img");
        this.rarity = obj.getInt("rarity");
        this.cost = obj.getInt("cost");
    }

    AniCharacter(JSONObject json){
        this.name = json.getString("name");
        this.media = json.getString("media");
        this.stats = json.getLong("stats");
        this.id = json.getInt("id");
        this.imgURL = "https://s4.anilist.co/file/anilistcdn/character/large/" + json.getString("img");
        this.rarity = json.getInt("rarity");
        this.cost = json.getInt("cost");
    }
}
