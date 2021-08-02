package unsw.loopmania.entity.notmoving.card;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a campfire card in the backend game world
 */
public class CampfireCard extends Card {
    public CampfireCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setType("Campfire");
    }    
}
