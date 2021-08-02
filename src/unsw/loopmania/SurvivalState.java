package unsw.loopmania;

import unsw.loopmania.entity.notmoving.building.HerosCastle;
import unsw.loopmania.entity.notmoving.item.Item;

public class SurvivalState implements GameModeState {

    public void buy(Item i, HerosCastle shop) {
        if (i.getType().equals("HealthPotion")) shop.removeItem(i);
    }
    
    public String getMode() {
        return "Survival";
    }
}
