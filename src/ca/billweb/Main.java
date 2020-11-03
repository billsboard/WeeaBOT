package ca.billweb;

import ca.billweb.timers.PlayerSaveTimer;
import ca.billweb.timers.QuizClearTimer;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    static DiscordClient client;
    static GatewayDiscordClient gateway;

    public static void main(String[] args) throws IOException {
        ConfigLoad.load();
        HelpLoad.load();

        client = DiscordClient.create(Variables.BOT_TOKEN);
        gateway = client.login().block();

        gateway.on(ReadyEvent.class)
                .subscribe(ready -> {System.out.println("Logged in as " + ready.getSelf().getUsername());
                System.out.println("Servicing " + ready.getGuilds().size() + " guilds");});

        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                try{
                    FileUtils.copyDirectory(Variables.saveDir, new File(Variables.saveDir, ".bak"), false);
                    System.out.println("Saving players...");
                    for (Player p : PlayerCache.loaded) {
                        PlayerCache.savePlayer(p.id);
                    }
                } catch (Exception ignored){}
                System.out.println("Exiting");
            }
        });

        System.out.println("ki!");

        /* Load the timers */
        Timer timer = new Timer();

        TimerTask quizTask = new QuizClearTimer();
        timer.scheduleAtFixedRate(quizTask, 0, 1000 * 5);

        TimerTask playerTask = new PlayerSaveTimer();
        timer.scheduleAtFixedRate(playerTask, 0, 1000 * 60 * 5);



        gateway.on(MessageCreateEvent.class).subscribe(x -> {
            try{
                MessageProcessor.onMessageRecieved(x);
            }
            catch (Exception e){
                if(e.getClass().getSimpleName().equalsIgnoreCase("PatternSyntaxException")) return;
                e.printStackTrace();
            }
        });

        gateway.on(ReactionAddEvent.class).subscribe(x -> {
            try{
                ReactionProcessor.onReactionRecieved(x);
            }
            catch (Exception e){
                if(e.getClass().getSimpleName().equalsIgnoreCase("PatternSyntaxException")) return;
                e.printStackTrace();
            }
        });

        gateway.onDisconnect().block();
    }
}
