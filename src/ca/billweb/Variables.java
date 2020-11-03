package ca.billweb;

import ca.billweb.attacks.Attack;
import ca.billweb.attacks.Charm;
import ca.billweb.attacks.Cripple;
import ca.billweb.attacks.Steal;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Variables {

    static final int statBarCount = 15;

    static final long DAILY_COOLDOWN = 24 * 60 * 60 * 1000;
    static final long WORK_COOLDOWN = 1 * 60 * 1000;
    static final long CRIME_COOLDOWN = 3 * 60 * 1000;
    static final long ATTACK_COOLDOWN = 10 * 60 * 1000;
    static final long SWAP_COOLDOWN = 2 * 60 * 60 * 1000;
    static final long QUIZ_COOLDOWN = 3 * 60 * 1000;

    static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss.SSSSSS");

    static HashMap<String, JSONObject> helpData;
    static HashMap<String, ArrayList<JSONObject>> categoryHelp;

    static Attack[] attacks = {new Charm(), new Steal(), new Cripple()};

    static long[] ADMINS = {506696814490288128L};

    public static HashMap<String, Long> GLOBAL_COOLDOWNS = new HashMap<>(){
        {
            put("crime", CRIME_COOLDOWN);
            put("work", WORK_COOLDOWN);
            put("daily", DAILY_COOLDOWN);
            put("attack", ATTACK_COOLDOWN);
            put("swap", SWAP_COOLDOWN);
            put("quiz", QUIZ_COOLDOWN);
        }
    };

    public static Random random = new Random();

    static String BOT_TOKEN;
    static String PREFIX;

    static File IMG_PATH;
    static HashMap<String, File> IMG_DIRS;

    static File aniDataPath;
    public static AniCharacter[] characters = new AniCharacter[200000];
    public static int maxCharID;

    static File guesspicJsonPath;
    public static String guesspicBucket = "https://weeabot-guesspic.s3.us-west-2.amazonaws.com/";
    public static ArrayList<JSONObject> guesspicObj = new ArrayList<>();

    static File saveDir;

}
