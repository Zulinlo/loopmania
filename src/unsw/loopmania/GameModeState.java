package unsw.loopmania;

import unsw.loopmania.entity.notmoving.building.HerosCastle;
import unsw.loopmania.entity.notmoving.item.Item;

public interface GameModeState {
	public void buy(Item i, HerosCastle shop);

	public String getMode();
}
