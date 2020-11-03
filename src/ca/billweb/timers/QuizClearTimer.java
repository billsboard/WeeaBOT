package ca.billweb.timers;

import ca.billweb.ReactionProcessor;
import ca.billweb.quizzes.QuizRunner;

import java.util.Date;
import java.util.Iterator;
import java.util.TimerTask;

public class QuizClearTimer extends TimerTask {
    @Override
    public void run() {
        Iterator<QuizRunner> it = ReactionProcessor.activeQuizzes.iterator();
        while (it.hasNext()){
            QuizRunner q = it.next();
            if(!q.active || new Date().getTime() - q.q.startTime > 15 * 1000){
                it.remove();
            }
        }
    }
}
