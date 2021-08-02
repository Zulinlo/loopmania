package unsw.loopmania.entity.notmoving.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * represents an unarmed and its actions
 */
public class Unarmed {
    public Unarmed() {}

    /**
     * Slap action 
     * @param prior damage total
     * @return new damage total
     */
    public int slap(int dealDamage) {
        return dealDamage + 1;
    }

    /**
     * Punch action 
     * @param prior damage total
     * @return new damage total
     */
    public int punch(int dealDamage) {
        if (new Random().nextInt(100) < 70) {
            return dealDamage + 3;
        }
        return 0;
    }

    /**
     * Get PP of this item
     * @return Map<String, Integer> of pp
     */
    public Map<String, Integer> getPP() {
        Map<String, Integer> pp = new HashMap<String, Integer>();
        pp.put("Slap", Integer.MAX_VALUE);
        pp.put("Punch", Integer.MAX_VALUE);

        return pp;
    }
}
