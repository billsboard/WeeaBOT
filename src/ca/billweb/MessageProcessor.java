package ca.billweb;

import ca.billweb.attacks.Attack;
import ca.billweb.quizzes.CharacterQuiz;
import ca.billweb.quizzes.CharacterQuizRunner;
import ca.billweb.quizzes.GuessPicRunner;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Color;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.function.Consumer;

public class MessageProcessor {
    static void onMessageRecieved(MessageCreateEvent e) throws IOException {
        Guild g = e.getGuild().block();
        Message m = e.getMessage();
        MessageChannel c = m.getChannel().block();
        User u = m.getAuthor().get();

        String body = m.getContent();
        String[] args = body.split(" ");
        String[] lowerArgs = body.toLowerCase().split(" ");

        String cmd = lowerArgs[0].replaceFirst(Variables.PREFIX, "");
        String noCmd = body.replaceFirst(Variables.PREFIX + cmd + " ", "");

        if(u.isBot()) return;
        else if(!body.startsWith(Variables.PREFIX)) return;

        switch (cmd){
            case "neko":{
                File[] f = Variables.IMG_DIRS.get("neko").listFiles();
                File ch = f[Variables.random.nextInt(f.length)];
                Consumer<MessageCreateSpec> spec = x -> {
                    try {
                        x.addFile(ch.getName(), new FileInputStream(ch));
                    } catch (FileNotFoundException ignored) {}
                };
                Methods.sendMessage(c, spec);
                break;
            }
            case "kitsune":{
                File[] f = Variables.IMG_DIRS.get("kitsune").listFiles();
                File ch = f[Variables.random.nextInt(f.length)];
                Consumer<MessageCreateSpec> spec = x -> {
                    try {
                        x.addFile(ch.getName(), new FileInputStream(ch));
                    } catch (FileNotFoundException ignored) {}
                };
                Methods.sendMessage(c, spec);
                break;
            }
            case "ping":{
                Methods.sendMessage(c, "Pong! Bot is online");
                break;
            }
            case "help":{
                if(lowerArgs.length < 2){
                    String s = Methods.getCommandUsage("help");
                    Methods.sendMessage(c, s);
                }
                else {
                    String cat = noCmd.toLowerCase();
                    if(!Variables.categoryHelp.containsKey(cat) && !Variables.helpData.containsKey(cat)){
                        StringBuilder sb = new StringBuilder();
                        for (String s : Variables.categoryHelp.keySet()) {
                            sb.append(" - ").append(Methods.toTitleCase(s)).append("\n");
                        }

                        Consumer<EmbedCreateSpec> spec = x -> {
                            x.setDescription("Invalid category!");
                            x.addField("Available categories", sb.toString(), true);
                        };

                        Methods.sendEmbed(c, spec);
                    }
                    else if(Variables.helpData.containsKey(cat)){
                        JSONObject obj = Variables.helpData.get(cat);

                        String s = "`" + obj.getString("name") + " ";
                        for (Object o : obj.getJSONArray("args")) {
                            s += "[" + o + "] ";
                        }
                        s = s.trim() + "`";

                        String finalS = s;
                        Consumer<EmbedCreateSpec> spec = x -> {
                            x.setTitle("Help for " + Methods.toTitleCase(obj.getString("name")) + " command");
                            x.addField("Name", Methods.toTitleCase(obj.getString("name")), true);
                            x.addField("Category", Methods.toTitleCase(obj.getString("category")), true);
                            x.addField("Usage", finalS, false);
                            x.addField("Description", obj.getString("longdesc"), false);
                        };
                        Methods.sendEmbed(c, spec);
                    }
                    else {
                        Consumer<EmbedCreateSpec> spec = x -> {
                            x.setTitle(Methods.toTitleCase(cat) + " commands");
                            for (JSONObject obj : Variables.categoryHelp.get(cat) ){
                                String s = "`" + obj.getString("name") + " ";
                                for (Object o : obj.getJSONArray("args")) {
                                    s += "[" + o + "] ";
                                }
                                s = s.trim() + "`";
                                x.addField(s, obj.getString("desc"), false);
                            }
                        };
                        Methods.sendEmbed(c, spec);
                    }
                }
                break;
            }
        } // Switch case for non-rpg commands
        switch (cmd){
            case "char": case "character": case "cinfo":{
                AniCharacter character = null;

                if(lowerArgs.length < 2){
                    Methods.sendArgumentsError(c, cmd, "name or ID");
                    break;
                }
                else if(Methods.isDigits(lowerArgs[1])){
                    int id = Integer.parseInt(lowerArgs[1]);
                    if(id > Variables.characters.length || Variables.characters[id] == null){
                        Methods.sendMessage(c, "The character with specified ID does not exist!");
                        break;
                    }
                    character = Variables.characters[id];
                }
                else{
                    String x = noCmd.toLowerCase();
                    for (AniCharacter a : Variables.characters) {
                        if(a == null) continue;
                        if(a.name.toLowerCase().startsWith(x) || a.name.toLowerCase().endsWith(x)){
                            character = a;
                            break;
                        }
                    }
                    if(character == null){
                        Methods.sendMessage(c, "There is no character with that name");
                        break;
                    }
                }

                AniCharacter finalCharacter = character;
                Consumer<EmbedCreateSpec> spec = x -> {
                    x.setTitle(finalCharacter.name)
                            .setDescription("From \"" + finalCharacter.media + "\"")
                            .setThumbnail(finalCharacter.imgURL);

                    StringBuilder statBuilder = new StringBuilder();
                    long mask = finalCharacter.stats;
                    int def = (int) mask & 0b11111111;
                    mask = mask >> 8;
                    statBuilder.append("Attack: ").append((int) mask & 0b11111111).append("\n");
                    mask = mask >> 8;
                    statBuilder.append("Defense: ").append(def & 0b11111111).append("\n");
                    statBuilder.append("Charm: ").append(mask & 0b11111111).append("\n");
                    mask = mask >> 8;
                    statBuilder.append("Speed: ").append(mask & 0b11111111).append("\n");
                    mask = mask >> 8;
                    statBuilder.append("Luck: ").append(mask & 0b11111111).append("\n");
                    mask = mask >> 8;
                    statBuilder.append("Intelligence: ").append(mask & 0b11111111).append("\n");
                    mask = mask >> 8;
                    statBuilder.append("Loyalty: ").append(mask & 0b11111111).append("\n");
                    x.addField("Stats", statBuilder.toString(), false);

                    StringBuilder otherBuilder = new StringBuilder();
                    otherBuilder.append("ID: ").append(finalCharacter.id).append("\n");
                    otherBuilder.append("Rarity: ").append(finalCharacter.rarity).append("\n");
                    otherBuilder.append("Cost: $").append(finalCharacter.cost).append("\n");
                    x.addField("Other", otherBuilder.toString(), false);
                };

                Methods.sendEmbed(c, spec);
                break;
            }
            case "work":{
                Player p = PlayerCache.getPlayer(u.getId().asLong());
                if(!p.started){
                    Methods.sendMessage(c, "You have not started the game... Please use `w!start` to start");
                    break;
                }

                if(p.canCooldownAction("work")){
                    int mod = p.active == null ? 0 : (int) ((p.active.stats & (0b11111111L << 32)) >> 32);
                    mod = mod / 8 - 5;
                    int r = Variables.random.nextInt(100);
                    Consumer<EmbedCreateSpec> spec;
                    if(r <= 65 + mod){
                        int money = Variables.random.nextInt(60) + 15;
                        p.money += money;
                        spec = x -> {
                            x.setDescription("[Uninteresting Text] You made $" + money);
                            x.setColor(Color.GREEN);
                        };
                    }
                    else {
                        int money = Variables.random.nextInt(30) + 3;
                        p.money -= money;
                        spec = x -> {
                            x.setDescription("Your character was too degenerate and lost $" + money);
                            x.setColor(Color.RED);
                        };

                    }

                    p.setCooldown("work");
                    Methods.sendEmbed(c, spec);
                }
                else{
                    Methods.ratelimitMsg(c, "work", p);
                }
                break;
            }
            case "crime":{
                Player p = PlayerCache.getPlayer(u.getId().asLong());
                if(!p.started){
                    Methods.sendMessage(c, "You have not started the game... Please use `w!start` to start");
                    break;
                }

                if(p.canCooldownAction("crime")){
                    int mod = p.active == null ? 0 : (int) ((p.active.stats & (0b11111111L << 32)) >> 32);
                    mod = mod / 4 - 7;
                    int r = Variables.random.nextInt(100);
                    Consumer<EmbedCreateSpec> spec;
                    if(r <= 45 + mod){
                        int money = Variables.random.nextInt(110) + 35;
                        p.money += money;
                        spec = x -> {
                            x.setDescription("[Uninteresting Text] You stole $" + money);
                            x.setColor(Color.GREEN);
                        };
                    }
                    else {
                        int money = Variables.random.nextInt(50) + 16;
                        p.money -= money;
                        spec = x -> {
                            x.setDescription("Your character was caught and fined $" + money);
                            x.setColor(Color.RED);
                        };

                    }

                    p.setCooldown("crime");
                    Methods.sendEmbed(c, spec);
                }
                else{
                    Methods.ratelimitMsg(c, "crime", p);
                }
                break;
            }
            case "daily":{
                Player p = PlayerCache.getPlayer(u.getId().asLong());
                if(!p.started){
                    Methods.sendMessage(c, "You have not started the game... Please use `w!start` to start");
                    break;
                }

                if(p.canCooldownAction("daily")){
                    int mod = p.active == null ? 0 : (int) ((p.active.stats & (0b11111111L << 32)) >> 32);
                    mod = mod / 4;

                    int money = Variables.random.nextInt(500 + mod) + 250;
                    p.money += money;

                    Consumer<EmbedCreateSpec> spec = x -> {
                        x.setDescription("You claimed your daily reward of $" + money);
                        x.setColor(Color.GREEN);
                    };

                    p.setCooldown("daily");
                    Methods.sendEmbed(c, spec);
                }
                else{
                    Methods.ratelimitMsg(c, "daily", p);
                }
                break;
            }
            case "buy": case "purchase":{
                if(lowerArgs.length < 2 || !Methods.isDigits(lowerArgs[1])){
                    Methods.sendArgumentsError(c, cmd, "character id");
                }
                else {
                    Player p = PlayerCache.getPlayer(u.getId().asLong());
                    if(!p.started){
                        Methods.notStarted(c);
                        break;
                    }
                    else{
                        AniCharacter ac = Variables.characters[Integer.parseInt(lowerArgs[1])];
                        if(ac.cost > p.money) Methods.sendMessage(c, "You do not have enough money!");
                        else{
                            p.addChar(ac.id);
                            p.money -= ac.cost;
                            if(p.active == null) p.setActiveChar(ac.id);
                            Methods.sendMessage(c, "Sucessfully bought " + ac.id + ": **" + ac.name + "**!");
                        }
                    }
                }
                break;
            }
            case "profile": case "p":{
                Player p = PlayerCache.getPlayer(u.getId().asLong());
                if(!p.started){
                    Methods.sendMessage(c, "You have not started the game... Please use `w!start` to start");
                    break;
                }

                Consumer<EmbedCreateSpec> spec = x -> {
                    x.setTitle(u.getUsername() + "'s profile");
                    x.addField("Money", p.money + "", true);
                    x.addField("Characters", p.charInv.size() + "", true);

                    String s;
                    if(p.active == null) s = "None";
                    else {
                        s = p.active.name + "\nID:" + p.active.id;
                    }

                    x.addField("Active character:", s, false);

                };
                Methods.sendEmbed(c, spec);
                break;

            }
            case "ki":{
                Methods.sendMessage(c, "Ki!");
                break;
            }
            case "start":{
                Player p = PlayerCache.getPlayer(u.getId().asLong());
                if(p.started){
                    Methods.sendMessage(c, "You have already started the game!");
                }
                else{
                    AniCharacter ac;
                    while ((ac = Variables.characters[Variables.random.nextInt(Variables.characters.length)]) == null){}
                    p.addChar(ac.id);
                    p.setActiveChar(ac.id);
                    p.money = 100;

                    Methods.sendMessage(c, "You have started the game. Your balance is 100, and your starting character was " +
                            ac.id + ": **" + ac.name + "**!");
                    p.started = true;
                }
                break;
            }
            case "attack":{
                Player p = PlayerCache.getPlayer(u.getId().asLong());
                long targetID;

                if(!p.started) Methods.notStarted(c);
                else if(lowerArgs.length < 2){
                    Methods.sendArgumentsError(c, cmd, "mentioned user");
                }
                else if(lowerArgs.length == 2){
                    targetID = Methods.getLongID(lowerArgs[1]);
                    if(!PlayerCache.getPlayer(targetID).started){
                        Methods.sendMessage(c, "Target player has not participated in WeeaBOT yet. Get them to do `w!start`!");
                    }
                    else if(!p.canCooldownAction("attack")){
                        Methods.ratelimitMsg(c, "attack", p);
                    }
                    else{
                        Player t = PlayerCache.getPlayer(targetID);
                        ArrayList<Attack> attacks = new ArrayList<>();
                        Collections.addAll(attacks, Variables.attacks);
                        Attack attack;
                        while (!(attack = attacks.get(Variables.random.nextInt(attacks.size()))).canAttack(p, t)){
                            attacks.remove(attack);
                            if(attacks.size() <= 0){
                                Consumer<EmbedCreateSpec> spec = x -> {
                                    x.setDescription("There are no available attacks to use on the target!\n(Try specifying an attack with `w!attack [user] [attack]` to see the requirements");
                                    x.setColor(Color.RED);
                                };
                                Methods.sendEmbed(c, spec);
                                break;
                            }
                        }

                        Consumer<EmbedCreateSpec> spec;
                        final Attack atk = attack;
                        if(!atk.canAttack(p, t)){
                            spec = x -> {
                                x.setTitle("Attack log");
                                x.setDescription(u.getUsername() + " is the attacker");

                                x.addField("Event", u.getUsername() + " attempted to use a " + atk.name + ", but it failed!", false);
                                x.addField("Reason", atk.getFailMessage(), true);
                                x.setColor(Color.RED);
                            };
                        }
                        else {
                            p.setCooldown("attack");
                            boolean result = atk.attack(p, t);
                            if(result){
                                spec = x -> {
                                    x.setTitle("Attack log");
                                    x.setDescription(u.getUsername() + " is the attacker");
                                    x.addField("Attack", atk.name, true);
                                    x.addField("Attacking Character", p.active.id + ": " + p.active.name, true);
                                    x.addField("Defending Character", t.active.id + ": " + t.active.name, true);
                                    x.addField("Result", atk.getAttackResult(), true);
                                    x.setColor(Color.GREEN);
                                };
                            }
                            else {
                                spec = x -> {
                                    x.setTitle("Attack log");
                                    x.setDescription(u.getUsername() + " is the attacker");
                                    x.addField("Attack", atk.name, true);
                                    x.addField("Attacking Character", p.active.id + ": " + p.active.name, true);
                                    x.addField("Defending Character", t.active.id + ": " + t.active.name, true);
                                    x.addField("Result", atk.getAttackResult(), true);
                                    x.setColor(Color.RED);
                                };
                            }
                        }

                        Methods.sendEmbed(c, spec);
                    }
                }
                break;
            }
            case "characters": case "charinv": case "chars":{
                Player p = PlayerCache.getPlayer(u.getId().asLong());
                if(!p.started){
                    Methods.notStarted(c);
                    break;
                }

                Consumer<EmbedCreateSpec> spec = x -> {
                    x.setTitle("Characters for " + u.getUsername());

                    ArrayList<AniCharacter> charInv = p.charInv;
                    for (int i = 0; i < charInv.size(); i++) {
                        AniCharacter a = charInv.get(i);
                        x.addField(i + ") " + a.name, "ID: " + a.id + "\nCost: $" + a.cost + "\nRarity: " + a.rarity + "\nStat Total: " + Methods.calculateStatTotal(a.stats), true);
                    }
                };

                Methods.sendEmbed(c, spec);
                break;
            }
            case "quiz": {
                if(lowerArgs.length < 2){
                    Methods.sendMessage(c, Methods.getCommandUsage("quiz"));
                }
                else{
                    Player p = PlayerCache.getPlayer(u.getId().asLong());
                    if(!p.canCooldownAction("quiz")){
                        Methods.ratelimitMsg(c, "quiz", p);
                        break;
                    }
                    switch (lowerArgs[1]){
                        case "character": case "char":{
                            p.setCooldown("quiz");
                            ReactionProcessor.activeQuizzes.add(new CharacterQuizRunner(p, c));
                            break;
                        }
                        case "image":{
                            p.setCooldown("quiz");
                            ReactionProcessor.activeQuizzes.add(new GuessPicRunner(p, c));
                            break;
                        }
                        default:
                            Methods.sendMessage(c, "Category not found!");
                    }
                }
            }
            case "select": case "swap":{
                Player p = PlayerCache.getPlayer(u.getId().asLong());
                if(!p.started){
                    Methods.notStarted(c);
                }
                else if(!p.canCooldownAction("swap")){
                    Methods.ratelimitMsg(c, "swap", p.cooldowns.get("swap"));
                }
                else if(lowerArgs.length < 2 || !Methods.isDigits(lowerArgs[1])){
                    Methods.sendMessage(c, Methods.getCommandUsage("select"));
                }
                else {
                    int x = Integer.parseInt(lowerArgs[1]);
                    if(x >= p.charInv.size() || p.charInv.get(x) == null){
                        Methods.sendMessage(c, "ID Specified is not a valid inventory slot. Do `w!characters` to get the slot numbers of your characters");
                    }
                    else{
                        p.active = p.charInv.get(x);
                        p.charInv.set(x, p.charInv.get(0));
                        p.charInv.set(0, p.active);
                        p.setCooldown("swap");
                        Methods.sendMessage(c, "Active character was set to " + p.active.id + ": " + p.active.name);
                    }
                }


                break;
            }
        }


        if(Methods.isAdmin(u.getId().asLong())) {
            switch (cmd) {
                case "addcharacter": {
                    if (lowerArgs.length == 2) {
                        Player p = PlayerCache.getPlayer(u.getId().asLong());
                        p.addChar(Integer.parseInt(lowerArgs[2]));
                        Methods.sendMessage(c, "Added character with ID " + lowerArgs[2]);
                    } else if (lowerArgs.length == 3) {
                        Player p = PlayerCache.getPlayer(Methods.getLongID(lowerArgs[1]));
                        p.addChar(Integer.parseInt(lowerArgs[2]));
                        Methods.sendMessage(c, "Added character with ID " + lowerArgs[2]);
                    }
                    break;
                }
                case "test": {
                    System.out.println(g.getMembers().collectList().block());
                    break;
                }
            }
        }
    }
}
