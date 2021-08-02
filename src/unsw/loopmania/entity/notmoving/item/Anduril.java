package unsw.loopmania.entity.notmoving.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped Stake in the backend world
 */
public class Anduril extends Item {
    public Anduril(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Anduril", -1, 400, x, y);
    }

    /**
     * Swing action 
     * @param prior damage total
     * @return new damage total
     */
    public int swing(int dealDamage) {
        return dealDamage + 10;
    }

    /**
     * Bad Boy action 
     * @param prior damage total
     * @return new damage total
     */
    public int badBoy(int dealDamage, boolean isDoggie) {
        if (new Random().nextInt(100) < 80) {
            return isDoggie ? dealDamage + 30 : dealDamage + 11;
        }
        
        return 0;
    }

    /**
     * Space Ex action 
     * @param prior damage total
     * @return new damage total
     */
    public int spaceEx(int dealDamage, boolean isElanMuske) {
        if (new Random().nextInt(100) < 80) {
            return isElanMuske ? dealDamage + 30 : dealDamage + 11;
        }

        return 0;
    }

    /**
     * TEMPLATE PATTERN for PPs
     */
    public List<Object> PP1() {
        List<Object> pp1 = new ArrayList<Object>();

        pp1.add("Swing");
        pp1.add(Integer.MAX_VALUE);

        return pp1;
    }

    public List<Object> PP2() {
        List<Object> pp2 = new ArrayList<Object>();

        pp2.add("Bad Boy");
        pp2.add(Integer.valueOf(10));

        return pp2;
    }

    public List<Object> PP3() {
        List<Object> pp3 = new ArrayList<Object>();

        pp3.add("Space Ex");
        pp3.add(Integer.valueOf(10));

        return pp3;
    }
}
