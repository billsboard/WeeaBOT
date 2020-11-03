package ca.billweb.quizzes;

import ca.billweb.Methods;
import ca.billweb.Variables;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.function.Consumer;

public class GuessPicQuiz extends Quiz{
    MessageChannel c;
    boolean completed = false;

    int id = 0;
    int correctNum = -1;
    String name = "";
    String url = "";

    String[] names = new String[5];

    GuessPicQuiz(MessageChannel c){
        this.c = c;
        JSONObject obj = Variables.guesspicObj.get(Variables.random.nextInt(Variables.guesspicObj.size()));
        name = obj.getString("title");

        JSONArray arr = obj.getJSONArray("urls");
        String t = arr.getString(Variables.random.nextInt(arr.length()));
        correctNum = Variables.random.nextInt(4) + 1;

        id = Integer.parseInt(t.replaceAll("[^\\d]", ""));
        url = Variables.guesspicBucket + "guesspic" + id + ".png";

        names[correctNum] = name;

        for (int i = 1; i <= 4; i++){
            if(i == correctNum) continue;
            int z = Variables.random.nextInt(Variables.guesspicObj.size());
            JSONObject choice = Variables.guesspicObj.get(z);
            if(choice.getString("title").equals(name)){
                int x = Variables.random.nextInt(3);
                choice = Variables.guesspicObj.get(x + z >= Variables.guesspicObj.size() ? z - x : z + x);
            }

            names[i] = (String) choice.get("title");
        }

        Consumer<EmbedCreateSpec> spec = x -> {
            x.setTitle("Where is this from?");
            x.setDescription("React to this message! You have 15 seconds");
            x.addField("1️⃣", names[1], true);
            x.addField("2️⃣", names[2], true);
            x.addField("3️⃣", names[3], true);
            x.addField("4️⃣", names[4], true);
            x.setImage(url);
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
