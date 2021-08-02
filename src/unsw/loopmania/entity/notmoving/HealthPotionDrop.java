package unsw.loopmania.entity.notmoving;

import javafx.beans.property.SimpleIntegerProperty;

public class HealthPotionDrop extends StaticEntity {
    public HealthPotionDrop(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public void destroyHealthPotionDrop() {
        this.destroy();
    }
}