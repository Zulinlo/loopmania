package unsw.loopmania.entity.notmoving.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a HealthPotion in the backend world
 */
public class HealthPotion extends Item {
    public HealthPotion(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("HealthPotion", 30, 10, x, y);
    }
}
