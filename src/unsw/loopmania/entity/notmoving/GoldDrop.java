package unsw.loopmania.entity.notmoving;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

public class GoldDrop extends StaticEntity {
    private int goldAmount;

    public GoldDrop(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        Random random = new Random();
        this.goldAmount = random.nextInt(5) + 5;
    }

    // destroys this gold drop
    public void destroyGoldDrop() {
        this.destroy();
    }

    // gets amount of gold in this drop
    public int getGoldAmount() {
        return this.goldAmount;
    }
}