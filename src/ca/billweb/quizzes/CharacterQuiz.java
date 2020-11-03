package ca.billweb.quizzes;

import ca.billweb.AniCharacter;
import ca.billweb.Methods;
import ca.billweb.Variables;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;

public class CharacterQuiz extends Quiz{

    MessageChannel c;
    AniCharacter selectedChar;

    int guesses = 1;
    int choices = 4;

    AniCharacter[] arr = new AniCharacter[choices + 1];

    public CharacterQuiz(MessageChannel c){
        this.c = c;

        int correct = Variables.random.nextInt(4) + 1;
        selectedChar = Variables.characters[Variables.random.nextInt(Variables.maxCharID - 1) + 1];
        arr[correct] = selectedChar;

        for (int i = 1; i <= 4; i++) {
            if(i == correct) continue;

            AniCharacter ac = Variables.characters[Variables.random.nextInt(Variables.maxCharID - 1) + 1];
            if(ac.id == selectedChar.id){
                int x = Variables.random.nextInt(30);
                ac = Variables.characters[ac.id + x >= Variables.characters.length ? ac.id + x : ac.id - x];
            }
            arr[i] = ac;
        }

        Consumer<EmbedCreateSpec> spec = x -> {
            x.setTitle("Who is this character?");
            x.setDescription("React to this message! You have 15 seconds");
            x.addField("1️⃣", arr[1].name, true);
            x.addField("2️⃣", arr[2].name, true);
            x.addField("3️⃣", arr[3].name, true);
            x.addField("4️⃣", arr[4].name, true);
            x.setImage(selectedChar.imgURL);
        };

        Message m = Methods.sendEmbed(c, spec);
        messageID = m.getId().asLong();

        Methods.unicodeReaction(c, messageID, "1️⃣");
        Methods.unicodeReaction(c, messageID, "2️⃣");
        Methods.unicodeReaction(c, messageID, "3️⃣");
        Methods.unicodeReaction(c, messageID, "4️⃣");
        startTime = new Date().getTime();
    }

}
