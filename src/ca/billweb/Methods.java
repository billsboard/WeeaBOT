package ca.billweb;

import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.ReactionEmoji;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.LongStream;

public class Methods {

    public static Message sendMessage(MessageChannel c, String s){
        if(s.length() > 1024) return c.createMessage("```Resultant message greater than 1024 messages```").block();
        return c.createMessage(s).block();
    }

    static void notStarted(MessageChannel c){
        c.createMessage("You have not started the game... Use `" + Variables.PREFIX + "start` to start the game!");
    }

    static void ratelimitMsg(MessageChannel c, long millis){
        sendMessage(c, "\uD83D\uDD59 Please wait " + toMinsSec(millis));
    }

    static void ratelimitMsg(MessageChannel c, String cooldown, long lastTime){
        ratelimitMsg(c, Variables.GLOBAL_COOLDOWNS.get(cooldown) - new Date().getTime() + lastTime);
    }

    static void ratelimitMsg(MessageChannel c, String cooldown, Player p){
        ratelimitMsg(c, Variables.GLOBAL_COOLDOWNS.get(cooldown) - new Date().getTime() + p.cooldowns.get(cooldown));
    }

    static Message sendMessage(MessageChannel c, Consumer<MessageCreateSpec> messageSpec){
        return c.createMessage(messageSpec).block();
    }

    static Message sendArgumentsError(MessageChannel c, String cmd, String... args){
        StringBuilder out = new StringBuilder("Invalid usage. Syntax is: `" + cmd + " ");
        for (int i = 0; i < args.length; i++) {
            out.append("[").append(args[i]).append("] ");
        }
        String msg = out.toString().trim() + "`";
        return c.createMessage(msg).block();
    }

    static boolean isDigits(String s){
        return s.chars().allMatch(Character::isDigit);
    }

    public static Message sendEmbed(MessageChannel c, Consumer<EmbedCreateSpec> embedSpec){
        return c.createEmbed(embedSpec).block();
    }

    static String generateStatBar(int stat){
        double remain = stat;
        double perBlock = 255.0 / Variables.statBarCount;

        int blockcount = 0;

        StringBuilder sb = new StringBuilder();
        while (remain > perBlock){
            sb.append("\u2588");
            remain -= perBlock;
            blockcount++;
        }

        for (int i = 0; i < Variables.statBarCount - blockcount; i++) {
            //sb.append("\u2592");
            sb.append("▁");
        }

        /*
        remain /= perBlock; // Convert to fraction

        if(0.0625 <= remain && remain < 0.1875) sb.append("\u258F");
        else if(0.1875 <= remain && remain < 0.3125) sb.append("\u258E");
        else if(0.3125 <= remain && remain < 0.4375) sb.append("\u258D");
        else if(0.4375 <= remain && remain < 0.5625) sb.append("\u258C");
        else if(0.5625 <= remain && remain < 0.6875) sb.append("\u258B");
        else if(0.6875 <= remain && remain < 0.8125) sb.append("\u258A");
        else if(0.8125 <= remain && remain < 0.9375) sb.append("\u2589");
        else sb.append("\u2588"); */

        return sb.append(" \u200b ").append(stat).append("/255").toString();
    }

    static long getLongID(String mention){
        return Long.parseLong(mention.replaceAll("[^\\d]", ""));
    }

    static boolean isAdmin(long id){
        return LongStream.of(Variables.ADMINS).anyMatch(x -> x == id);
    }

    static int calculateStatTotal(long statMask){
        int out = 0;
        while (statMask > 0){
            out += (int) ((statMask >> 8) & 0b11111111);
            statMask = statMask >> 8;
        }

        return out;
    }

    static String getCommandUsage(String command){
        JSONObject obj = Variables.helpData.get(command);
        String s = "Usage: `" + obj.getString("name");
        for (Object o : obj.getJSONArray("args")) {
            s += " [" + o + "]";
        }

        return s.trim() + "`";
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static void unicodeReaction(MessageChannel c, long messageID, String reaction){
        Message m = c.getMessageById(Snowflake.of(messageID)).block();
        ReactionEmoji re = ReactionEmoji.unicode(reaction);
        m.addReaction(re).block();
    }

    public static String toMinsSec(long millis){
        millis = Math.abs(millis);
        return String.format("%d:%d:%d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }
}
