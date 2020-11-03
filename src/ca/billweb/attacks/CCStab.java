package ca.billweb.attacks;

import ca.billweb.Player;

public class CCStab extends Attack {

    public CCStab(){
        name = "Close-range Stab";
        desc = "The attacker uses a dull knife to stab the enemy at close range";
    }

    @Override
    public boolean attack(Player attack, Player defend) {
        return false;
    }

    @Override
    public boolean canAttack(Player attack, Player defend) {
        return false;
    }
}
