package ca.billweb.attacks;

import ca.billweb.*;

public abstract class Attack {

    public String name;
    String desc;
    protected String attackResult, failMessage;

    public abstract boolean attack(Player attack, Player defend);

    public abstract boolean canAttack(Player attack, Player defend);

    public String getAttackResult(){
        return attackResult;
    }

    public String getFailMessage(){
        return failMessage;
    }
}
