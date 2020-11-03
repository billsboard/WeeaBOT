package ca.billweb.attacks;

import ca.billweb.Player;
import ca.billweb.Variables;

public class Cripple extends Attack {

    public Cripple(){
        name = "Cripple";
        desc = "The attacker hits the defender repeatadly on their hands and legs, removing their ability to do work";
    }

    @Override
    public boolean attack(Player attack, Player defend) {
        int atkAttack = (int) ((attack.active.stats >> 8) & 0b11111111);
        int baseChance = 60;
        int defDefense = (int) (defend.active.stats & 0b11111111);

        baseChance += atkAttack / 6;
        baseChance -= defDefense / 8;

        int x = Variables.random.nextInt(255);
        if(x <= baseChance){
            attackResult = "<@" + defend.id + "> was crippled and needs time to recover!";
            attack.addCooldown("steal", 15 * 60 * 1000);
            attack.addCooldown("work", 15 * 60 * 1000);
            return true;
        }
        else {
            attackResult = "<@" + attack.id + ">'s character was too weak to do damage!";
        }
        return false;
    }

    @Override
    public boolean canAttack(Player attack, Player defend) {
        if(attack.active == null){
            failMessage = "Attacking player has no active character!";
            return false;
        }
        else if(defend.active == null){
            failMessage = "Defending player has no active character!";
            return false;
        }
        return true;
    }
}
