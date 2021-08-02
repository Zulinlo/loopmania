package unsw.loopmania.entity.notmoving.item;

import javafx.beans.property.SimpleIntegerProperty;

public class DoggieCoin extends Item {
    public DoggieCoin(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("DoggieCoin", 0, 50, x, y);
    }
}
