package unsw.loopmania.entity.notmoving.building;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.entity.notmoving.item.*;

/**
 * a basic form of building in the world
 */
public class HerosCastle extends Building {

    private List<Item> shop;

    public HerosCastle(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);      
        shop = new ArrayList<>();
        this.setType("HerosCastle");
    }

    /**
     * Randomises a list of objects for the user to buy
     * @return list of objects in shop
     */
    public List<Item> shop() {
        List<Item> list = new ArrayList<>();
        Random rand = new Random();
        int rate = rand.nextInt(100);
        int x = 0;
        int y = 2;
        
        Item item = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(2));
        list.add(item);

        x += 2;
        list.add(new HealthPotion(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y)));
        x += 2;
        

        if (rate < 60) {
            list.add(new Stake(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y)));
            x += 2;
        }

        rate = rand.nextInt(100);
        if (rate < 60) {
            list.add(new Staff(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y)));
            x+=2;
        }

        rate = rand.nextInt(100);
        if (rate < 50) {
            if (x > 7) {
                x=0;
                y+=2;
            }
            list.add(new Armour(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y)));
            x+=2;
        }

        rate = rand.nextInt(100);
        if (rate < 50) {
            if (x > 7) {
                x=0;
                y+=2;
            }
            list.add(new Shield(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y)));
            x+=2;
        }

        rate = rand.nextInt(100);
        if (rate < 50) {
            if (x > 7) {
                x=0;
                y+=2;
            }
            list.add(new Helmet(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y)));
            x+=2;
        }
        shop = list;
        return list;
    }

    public void removeItem(Item i) {
        shop.remove(i);
    }

    public List<Item> getShop() {
        return this.shop;
    }
}
