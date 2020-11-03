package ca.billweb.attacks;

import ca.billweb.Player;
import ca.billweb.Variables;

public class Steal extends Attack {

    public Steal(){
        name = "Steal";
        desc = "Steal some money from your friends!";
    }

    @Override
    public boolean attack(Player attack, Player defend) {
        int attackSpeed = (int) ((attack.active.stats >> 24) & 0b11111111);
        int defIntelligence = (int) ((defend.active.stats >> 40) & 0b11111111);

        int baseChance = 50;
        baseChance += attackSpeed / 6;
        baseChance -= defIntelligence / 6;

        int x = Variables.random.nextInt(255);
        if(x <= baseChance){
            int money = (Variables.random.nextInt((int) Math.min(defend.money - 500, defend.money * 0.1)));
            attackResult = "$" + money + " was successfully stolen!";
            attack.money += money;
            defend.money -= money;
            return true;
        }
        else {
            attackResult = "<@" + defend.id + "> character anticipated the robbery!";
            return false;
        }
    }

    @Override
    public boolean canAttack(Player attack, Player defend) {
        if(defend.money <= 500){
            failMessage = "$500 is protected from stealing";
            return false;
        }
        else if(attack.active == null){
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
