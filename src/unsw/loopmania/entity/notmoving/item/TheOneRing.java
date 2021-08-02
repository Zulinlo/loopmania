package unsw.loopmania.entity.notmoving.item;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped TheOneRing in the backend world
 */
public class TheOneRing extends Item {
    public TheOneRing(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("TheOneRing", -1, 400, x, y);
    }    
}
