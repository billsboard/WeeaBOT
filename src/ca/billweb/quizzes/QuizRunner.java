package ca.billweb.quizzes;

import ca.billweb.Player;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.ReactionEmoji;

import java.util.HashMap;

abstract public class QuizRunner {

    public MessageChannel c;
    int correct;
    int maxQ = 10;
    int moneyPerKill;

    public boolean active = true;

    public Player p;
    public Quiz q;

    HashMap<String, Integer> reactionMap = new HashMap<>(){
        {
            put("1️⃣", 1);
            put("2️⃣", 2);
            put("3️⃣", 3);
            put("4️⃣", 4);
        }
    };

    public abstract void guess(ReactionEmoji e);


}
