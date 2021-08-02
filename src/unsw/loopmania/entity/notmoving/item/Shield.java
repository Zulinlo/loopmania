package unsw.loopmania.entity.notmoving.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped shield in the backend world
 */
public class Shield extends Item {
    public Shield(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Shield", 450, 150, x, y);
    }

    @Override
    public int defenseEffect(int damageTaken, boolean vsBoss) {
        return damageTaken - 1;
    }
}
