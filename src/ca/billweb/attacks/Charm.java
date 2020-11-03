package ca.billweb.attacks;

import ca.billweb.AniCharacter;
import ca.billweb.Player;
import ca.billweb.Variables;

public class Charm extends Attack{


    public Charm(){
        name = "Charm";
        desc = "This attack attempts to use your character's charm to sway another person's character onto your side";
    }

    @Override
    public boolean attack(Player attack, Player defend) {
        int baseChance = 100;
        baseChance += ((attack.active.stats >> 16) & 0b11111111) / 16;

        int defLoyalty = (int) ((defend.active.stats >> 48) & 0b11111111);
        double finalChance = (1 - (defLoyalty) / 255.0) * baseChance;

        int x = Variables.random.nextInt(255);
        if(x <= finalChance){
            AniCharacter ac = defend.active;
            attackResult = ac.id + ": **" + ac.name + "** was charmed and switched allegiance!";

            attack.addChar(ac.id);
            defend.removeChar(ac.id);
            defend.active = defend.charInv.get(0);
            return true;
        }
        else{
            attackResult = "Target's active character was too loyal to be charmed!";
            return false;
        }
    }

    @Override
    public boolean canAttack(Player attack, Player defend) {
        if(attack.active == null){
            failMessage = "Attacking player has no active character!";
            return false;
        }
        else if(defend.active == null || defend.charInv.size() <= 1){
            failMessage = "The target's active character is their only character, so you may not charm it.";
            return false;
        }
        return true;
    }



}
