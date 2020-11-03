package ca.billweb.timers;

import ca.billweb.Player;
import ca.billweb.PlayerCache;

import java.io.IOException;
import java.util.TimerTask;

public class PlayerSaveTimer extends TimerTask {

    @Override
    public void run() {
        System.out.println("Executing player save...");
        for (Player p : PlayerCache.loaded) {
            try {
                PlayerCache.savePlayer(p.id);
            } catch (IOException ignored) {}
        }
    }
}
