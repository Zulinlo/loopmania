package unsw.loopmania.entity.notmoving.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.entity.notmoving.StaticEntity;

/**
 * An Item in the world representing what the Character can obtain
 * Only exists in inventory
 */
public abstract class Item extends StaticEntity {
    private String type;
    private int buyPrice;
    private int sellPrice;

    public Item(String item, int buyPrice, int sellPrice, SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.type = item;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    /**
     * Item type getter
     * @return string representing item type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Buy price getter
     * @return price as int
     */
    public int getBuyPrice() {
        return this.buyPrice;
    }

    /**
     * Sell price getter
     * @return price as int
     */
    public int getSellPrice() {
        return this.sellPrice;
    }

    /**
     * Applies the item's effect on attack
     * @param current character's attack amount
     * @return altered character's attack amount
     */
    public int attackEffect(int dealDamage, String enemyType) {
        return -1;
    }

    /**
     * Applies the item's effect on defense
     * @param current character's damage taken amount
     * @param vsBoss if character is currently vsing a boss mob
     * @return altered character's damage taken amount
     */
    public int defenseEffect(int currentAttack, boolean vsBoss) {
        return -1;
    }

    public void setSellPrice(int price) {
        this.sellPrice = price;
    }

    /**
     * TEMPLATE PATTERN for each skill's PP
     */
    public List<Object> PP1() {
        return null;
    }

    public List<Object> PP2() {
        return null;
    }

    public List<Object> PP3() {
        return null;
    }

    /**
     * Get PPs of each skill
     * @return Map<String, Integer> representing all PPs
     */
    public final Map<String, Integer> getPP() {
        Map<String, Integer> allPPs = new HashMap<String, Integer>();

        List<Object> PP1 = PP1();
        List<Object> PP2 = PP2();
        List<Object> PP3 = PP3();

        if (PP1 != null) {
            allPPs.put((String) PP1.get(0), (Integer) PP1.get(1));
        }

        if (PP2 != null) {
            allPPs.put((String) PP2.get(0), (Integer) PP2.get(1));
        }

        if(PP3 != null) {
            allPPs.put((String) PP3.get(0), (Integer) PP3.get(1));
        }

        return allPPs;
    }
}
