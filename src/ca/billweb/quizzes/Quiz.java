package ca.billweb.quizzes;

import ca.billweb.AniCharacter;
import discord4j.core.object.entity.channel.MessageChannel;

abstract public class Quiz {

    MessageChannel c;
    public long messageID;
    public long startTime = Long.MAX_VALUE;
    public boolean completed = false;

    Quiz(MessageChannel c){
        this.c = c;
    }

    Quiz(){}

}
