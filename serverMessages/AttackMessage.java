package serverMessages;

import obj.Room;

import java.io.Serializable;

public class AttackMessage implements Serializable {
    private String turnName;
    private int attackIndex;

    public AttackMessage(String turnName, int attackIndex) {
        this.turnName = turnName;
        this.attackIndex = attackIndex;
    }

    public String getTurnName() {
        return turnName;
    }

    public int getAttackIndex() {
        return attackIndex;
    }
}
