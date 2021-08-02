package unsw.loopmania.entity.notmoving.card;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a tower card in the backend game world
 */
public class TowerCard extends Card {
    public TowerCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setType("Tower");
    }    
}
