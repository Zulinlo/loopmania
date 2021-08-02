package unsw.loopmania.entity.notmoving.item;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * Composite class for confusing mode
 * A rare item which has another rare item's feature
 */
public class CompoundRareItem extends Item {
    private List<Item> children = new ArrayList<Item>();
    
    public CompoundRareItem(String type, SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(type, -1, 400, x, y);
    }

    @Override
    public int attackEffect(int dealDamage, String enemyType) {
        if (children.size() == 0)
            return -1;

        int res = dealDamage;

        for (Item child : children) {
            if (child.attackEffect(res, enemyType) != -1) {
                res = child.attackEffect(res, enemyType);
            }
        }

        return res;
    }

    @Override
    public int defenseEffect(int damageTaken, boolean vsBoss) {
        if (children.size() == 0)
            return -1;

        int res = damageTaken;

        for (Item child : children) {
            if (child.defenseEffect(res, vsBoss) != -1) {
                res = child.defenseEffect(res, vsBoss);
            }
        }

        if (res < 1) {
            return 1;
        } else {
            return res;
        }
    }

    /**
     * Add an item to this composite object
     * @param item
     */
    public void add(Item item) {
        children.add(item);
    }

    public List<Item> getItems() {
        return children;
    }
}
