package ca.billweb;

import ca.billweb.quizzes.QuizRunner;
import discord4j.core.event.domain.message.ReactionAddEvent;

import java.util.ArrayList;
import java.util.Date;

public class ReactionProcessor {

    public static ArrayList<QuizRunner> activeQuizzes = new ArrayList<>();

    static void onReactionRecieved(ReactionAddEvent x){
        if(x.getUser().block().isBot()) return;
        activeQuizzes.removeIf(e -> (!e.active || Math.abs(e.q.startTime - new Date().getTime()) > 15 * 1000));
        for (QuizRunner qr : activeQuizzes) {
            if(qr.q.messageID == x.getMessageId().asLong() && qr.p.id == x.getUserId().asLong()){
                qr.guess(x.getEmoji());
            }
        }
    }
}
