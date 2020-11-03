package ca.billweb;

import discord4j.common.util.Snowflake;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Player {

    boolean started = false;

    public long id;
    Snowflake flake;

    String username;

    public AniCharacter active;
    public ArrayList<AniCharacter> charInv;

    public int money = 0;
    long lastAccessTime;

    double health;

    HashMap<String, Long> cooldowns;

    Player(Snowflake flake){
        this.flake = flake;
        id = flake.asLong();
        username = Main.gateway.getUserById(flake).block().getUsername();

        active = null;
        charInv = new ArrayList<>();

        cooldowns = new HashMap<>();
        cooldowns.put("crime", 0L);
        cooldowns.put("work", 0L);
        cooldowns.put("daily", 0L);
        cooldowns.put("attack", 0L);
        cooldowns.put("quiz", 0L);
        cooldowns.put("swap", 0L);
    }

    void setActiveChar(int id){
        boolean found = false;
        AniCharacter c = null;
        for (AniCharacter a : charInv) {
            if(a.id == id){
                found = true;
                c = a;
            }
        }

        if(found){
            AniCharacter ac = charInv.get(0);
            charInv.set(charInv.indexOf(c), ac);
            charInv.set(0, c);
            active = c;
        }
    }

    public void addChar(int id){
        if(!hasCharacter(id)){
            charInv.add(Variables.characters[id]);
        }
    }

    boolean hasCharacter(int id){
        boolean found = false;
        for (AniCharacter a : charInv) {
            if (a.id == id) {
                found = true;
            }
        }
        return found;
    }
    
    public void removeChar(int id){
        AniCharacter charToRemove = null;
        if(hasCharacter(id)){
            for (AniCharacter a : charInv) {
                if(a.id == id){
                    charToRemove = a;
                    break;
                }
            }
            charInv.remove(charToRemove);
        }
    }

    boolean hasCharacter(String name){
        boolean found = false;
        for (AniCharacter a : charInv) {
            if (a.name.toLowerCase().startsWith(name) || a.name.toLowerCase().endsWith(name)) {
                found = true;
            }
        }
        return found;
    }

    void setCooldown(String cooldown){
        cooldowns.put(cooldown, new Date().getTime());
    }

    void setCooldown(String cooldown, long time){
        cooldowns.put(cooldown, time);
    }

    public void addCooldown(String cooldown, long time){
        cooldowns.put(cooldown, cooldowns.get(cooldown) + time);
    }

    boolean canCooldownAction(String cooldown){
        return new Date().getTime() - cooldowns.get(cooldown) >= Variables.GLOBAL_COOLDOWNS.get(cooldown);
    }

}
