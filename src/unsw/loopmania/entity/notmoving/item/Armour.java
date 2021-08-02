package unsw.loopmania.entity.notmoving.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped Armour in the backend world
 */
public class Armour extends Item {
    public Armour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Armour", 600, 200, x, y);
    }

    @Override
    public int defenseEffect(int damageTaken, boolean vsBoss) {
        return (damageTaken / 2) - 2;
    }
}
