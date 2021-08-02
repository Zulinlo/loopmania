package unsw.loopmania.entity.notmoving.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped shield in the backend world
 */
public class TreeStump extends Item {
    public TreeStump(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("TreeStump", -1, 400, x, y);
    }

    @Override
    public int defenseEffect(int damageTaken, boolean vsBoss) {
        if (vsBoss)
            return damageTaken - 9;
        
        return damageTaken - 3;
    }
}
