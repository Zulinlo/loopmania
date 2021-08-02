package unsw.loopmania.entity.notmoving.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped helmet in the backend world
 */
public class Helmet extends Item {
    public Helmet(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Helmet", 450, 150, x, y);
    }    

    @Override
    public int attackEffect(int dealDamage, String enemyType) {
        return dealDamage - 3;
    }

    @Override
    public int defenseEffect(int damageTaken, boolean vsBoss) {
        return damageTaken - 3;
    }
}
