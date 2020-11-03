package ca.billweb.quizzes;

import ca.billweb.Methods;
import ca.billweb.Player;
import ca.billweb.Variables;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.ReactionEmoji;

public class CharacterQuizRunner extends QuizRunner{
    public CharacterQuizRunner(Player p, MessageChannel c){
        this.p = p;
        this.c = c;

        moneyPerKill = Variables.random.nextInt(20) + 10;
        Methods.sendMessage(c, "Quiz starting, you will answer up to 10 questions, and each question will be worth $" + moneyPerKill);

        q = new CharacterQuiz(c);
    }

    @Override
    public void guess(ReactionEmoji e) {
        Integer x = reactionMap.get(e.asUnicodeEmoji().get().getRaw());
        CharacterQuiz quiz = (CharacterQuiz) q;
        if(x == null) return;
        if(quiz.arr[x] == quiz.selectedChar){
            correct++;
            if(correct >= maxQ) {
                active = false;
                p.money += correct * moneyPerKill;
                Methods.sendMessage(c, "You got " + correct + " correct and thus earned $" + (correct * moneyPerKill));
            }
            else {
                q = new CharacterQuiz(c);
            }
        }
        else {
            p.money += correct * moneyPerKill;
            Methods.sendMessage(c, "**Incorrect!**\nYou got " + correct + " correct and thus earned $" + (correct * moneyPerKill));
            active = false;
        }
    }
}
