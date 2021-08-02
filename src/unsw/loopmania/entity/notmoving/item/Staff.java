package unsw.loopmania.entity.notmoving.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped Staff in the backend world
 */
public class Staff extends Item {
    public Staff(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Staff", 150, 50, x, y);
    }

    /**
     * Jab action 
     * @param prior damage total
     * @return new damage total
     */
    public int jab(int dealDamage) {
        return dealDamage + 1;
    }

    /**
     * Zap action 
     * @param prior damage total
     * @return new damage total
     */
    public int zap(int dealDamage) {
        if (new Random().nextInt(100) < 70) {
            return dealDamage + 6;
        }
        return 0;
    }

    /**
     * Trance action 
     * @return if succeeded or not
     */
    public boolean trance() {
        return new Random().nextInt(100) < 30;
    }

    /**
     * TEMPLATE PATTERN for PPs
     */
    public List<Object> PP1() {
        List<Object> pp1 = new ArrayList<Object>();

        pp1.add("Jab");
        pp1.add(Integer.MAX_VALUE);

        return pp1;
    }

    public List<Object> PP2() {
        List<Object> pp2 = new ArrayList<Object>();

        pp2.add("Zap");
        pp2.add(Integer.valueOf(8));

        return pp2;
    }

    public List<Object> PP3() {
        List<Object> pp3 = new ArrayList<Object>();

        pp3.add("Trance");
        pp3.add(Integer.valueOf(5));

        return pp3;
    }
}
