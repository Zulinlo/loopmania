package unsw.loopmania.entity.notmoving.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped Sword in the backend world
 */
public class Sword extends Item {
    public Sword(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Sword", 150, 50, x, y);
    }

    /**
     * Slash action 
     * @param prior damage total
     * @return new damage total
     */
    public int slash(int dealDamage) {
        return dealDamage + 5;
    }

    /**
     * Strike action 
     * @param prior damage total
     * @return new damage total
     */
    public int strike(int dealDamage) {
        if (new Random().nextInt(100) < 70) {
            return dealDamage + 8;
        }
        return 0;
    }

    /**
     * Heavy slash action 
     * @param prior damage total
     * @return new damage total
     */
    public int heavySlash(int dealDamage) {
        if (new Random().nextInt(100) < 50) {
            return dealDamage + 7;
        }
        return 0;
    }

    /**
     * TEMPLATE PATTERN for PPs
     */
    public List<Object> PP1() {
        List<Object> pp1 = new ArrayList<Object>();

        pp1.add("Slash");
        pp1.add(Integer.MAX_VALUE);

        return pp1;
    }

    public List<Object> PP2() {
        List<Object> pp2 = new ArrayList<Object>();

        pp2.add("Strike");
        pp2.add(Integer.valueOf(5));

        return pp2;
    }

    public List<Object> PP3() {
        List<Object> pp3 = new ArrayList<Object>();

        pp3.add("Heavy Slash");
        pp3.add(Integer.valueOf(3));

        return pp3;
    }
}
