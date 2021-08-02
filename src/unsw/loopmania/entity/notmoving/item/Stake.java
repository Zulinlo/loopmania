package unsw.loopmania.entity.notmoving.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped Stake in the backend world
 */
public class Stake extends Item {
    public Stake(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super("Stake", 180, 60, x, y);
    }

    /**
     * Stab action 
     * @param prior damage total
     * @return new damage total
     */
    public int stab(int dealDamage) {
        return dealDamage + 2;
    }

    /**
     * Garlic stab action 
     * @param prior damage total
     * @return new damage total
     */
    public int garlicStab(int dealDamage, boolean isVampire) {
        if (new Random().nextInt(100) < 80) {
            return isVampire ? dealDamage + 9 : dealDamage + 3;
        }

        return 0;
    }

    /**
     * TEMPLATE PATTERN for PPs
     */
    public List<Object> PP1() {
        List<Object> pp1 = new ArrayList<Object>();

        pp1.add("Stab");
        pp1.add(Integer.MAX_VALUE);

        return pp1;
    }

    public List<Object> PP2() {
        List<Object> pp2 = new ArrayList<Object>();

        pp2.add("Garlic Stab");
        pp2.add(Integer.valueOf(7));

        return pp2;
    }
}
