package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.entity.notmoving.building.HerosCastle;
import unsw.loopmania.entity.notmoving.item.Item;

public class BerserkerState implements GameModeState {

    public void buy(Item i, HerosCastle shop) {
        if (i.getType() == "Armour" || i.getType() == "Helmet" || i.getType() == "Shield") {
            List<Item> remove = new ArrayList<>();
            for (Item item: shop.getShop()) {
                if (item.getType() == "Armour" || item.getType() == "Helmet" || item.getType() == "Shield")
                    remove.add(item);
            }

            for (Item e: remove) {
                shop.removeItem(e);
            }
        }
    }

    public String getMode() {
        return "Berserker";
    }
}
