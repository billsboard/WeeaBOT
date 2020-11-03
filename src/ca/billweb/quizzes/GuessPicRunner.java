package ca.billweb.quizzes;

import ca.billweb.Methods;
import ca.billweb.Player;
import ca.billweb.Variables;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.ReactionEmoji;

public class GuessPicRunner extends QuizRunner {


    public GuessPicRunner(Player p, MessageChannel c){
        this.p = p;
        this.c = c;

        moneyPerKill = Variables.random.nextInt(12) + 5;
        Methods.sendMessage(c, "Quiz starting, you will answer up to 10 questions, and each question will be worth $" + moneyPerKill);

        q = new GuessPicQuiz(c);
    }

    @Override
    public void guess(ReactionEmoji e) {
        Integer x = reactionMap.get(e.asUnicodeEmoji().get().getRaw());
        GuessPicQuiz quiz = (GuessPicQuiz) q;
        if(x == null) return;
        if(quiz.correctNum == x){
            correct++;
            if(correct >= maxQ) {
                active = false;
                p.money += correct * moneyPerKill;
                Methods.sendMessage(c, "You got " + correct + " correct and thus earned $" + (correct * moneyPerKill));
            }
            else {
                q = new GuessPicQuiz(c);
            }
        }
        else {
            p.money += correct * moneyPerKill;
            Methods.sendMessage(c, "**Incorrect!**\nYou got " + correct + " correct and thus earned $" + (correct * moneyPerKill));
            active = false;
        }

    }
}
