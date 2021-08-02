package unsw.loopmania.entity.moving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;

import unsw.loopmania.entity.moving.enemy.Enemy;
import unsw.loopmania.entity.notmoving.item.*;

/**
 * The main Character Object of the game world, taking input from Human Player
 */
public class Character extends MovingEntity {

    public static final int unequippedInventoryWidth = 4;
    public static final int unequippedInventoryHeight = 4;

    private SimpleIntegerProperty curHp;
    private SimpleIntegerProperty maxHp;
    private SimpleIntegerProperty level;
    private int baseAtk;
    
    private boolean isInBattle;
    private SimpleIntegerProperty exp;
    private SimpleIntegerProperty gold;

    private List<Item> unequippedInventory;
    private Map<String, Item> equippedItems;

    private List<Integer> alliedSoldiers;

    /**
     * Constructor for Character
     */
    public Character(PathPosition position) {
        super(position);

        this.isInBattle = false;
        this.exp = new SimpleIntegerProperty(0);
        this.gold = new SimpleIntegerProperty(0);
        this.curHp = new SimpleIntegerProperty(100);
        this.maxHp = new SimpleIntegerProperty(100);
        this.level = new SimpleIntegerProperty(1);
        this.baseAtk = 3;
        
        this.unequippedInventory = new ArrayList<Item>();
        this.equippedItems = new HashMap<String, Item>();
        equippedItems.put("Helmet", null);
        equippedItems.put("Armour", null);
        equippedItems.put("Shield", null);
        equippedItems.put("Weapon", null);
        equippedItems.put("TheOneRing", null);

        this.alliedSoldiers = new ArrayList<Integer>();
    }

    public int getExp() {
        return exp.get();
    }

    public int getLevel() {
        return level.get();
    }

    public int getCurHp() {
        return curHp.get();
    }


    /**
     * gets alliedsolider with most hp's index
     * @return index
     * @pre none
     * @post returns allied soldier's index who has highest hp, if there are no allied soldiers then return -1
     */
    public int getHighestHpAlliedSoldierIndex() {
        if (alliedSoldiers.size() == 0) {
            return -1;
        }

        int maxHpIndex = 0;
        for (int i = 0; i < alliedSoldiers.size(); i++) {
            if (alliedSoldiers.get(maxHpIndex) <= alliedSoldiers.get(i)) {
                maxHpIndex = i;
            }
        }

        return maxHpIndex;
    }

    /**
     * gets alliedsoldier's hp according to index input
     * @param index of allied soldier
     * @return int hp
     * @pre index has needed allied soldier
     * @post returns the health
     */
    public int getAlliedSoldierHpByIndex(int index) {
        return alliedSoldiers.get(index).intValue();
    }

    /**
     * reduces an allied soldier's hp by index input
     * @param index
     * @return true if allied soldier still alive, false if dead
     * @pre damage taken > 0 and index corresponds to alive allied soldier
     * @post reduces allied soldier's hp, returns true if still alive and false if dead
     */
    public boolean reduceAlliedSoldierHpByIndex(int index, int damage) {
        int healthRemaining = alliedSoldiers.get(index).intValue() - damage;
        if (healthRemaining <= 0) {
            alliedSoldiers.remove(index);
            return false;
        } else {
            alliedSoldiers.set(index, Integer.valueOf(healthRemaining));
            return true;
        }
    }

    /**
     * Adds a new allied soldier, replaces any with lowest hp
     */
    public void addAlliedSoldier() {
        if (alliedSoldiers.size() < 3) {
            alliedSoldiers.add(Integer.valueOf(40));
        } else {
            int indexDelete = 0;
            for (int i = 0; i < alliedSoldiers.size(); ++i) {
                if (alliedSoldiers.get(i) < alliedSoldiers.get(indexDelete)) {
                    indexDelete = i;
                }
            }

            alliedSoldiers.remove(indexDelete);
            alliedSoldiers.add(Integer.valueOf(40));
        }
    }

    public SimpleIntegerProperty getExpProperty() {
        return exp;
    }

    public SimpleIntegerProperty getGoldProperty() {
        return gold;
    }

    public SimpleIntegerProperty getHealthProperty() {
        return curHp;
    }

    public SimpleIntegerProperty getMaxHealthProperty() {
        return maxHp;
    }

    public SimpleIntegerProperty getLevelProperty() {
        return level;
    }

    public void addHealth(int amount) {
        curHp.set(curHp.getValue() + amount);

        // if add health to more than max, then keep it as max
        if (curHp.get() > maxHp.get()) {
            curHp.set(maxHp.get());
        }
    }

    public void setLevel(int amount) {
        level.set(amount);
    }

    public void setMaxHealth(int amount) {
       maxHp.set(amount);
    }

    public void setBaseAtk(int newAtk) {
        this.baseAtk = newAtk;
    }
    
    public void setGold(int amount) {
        gold = new SimpleIntegerProperty(amount);
    }

    public void setExp(int amount) {
        exp = new SimpleIntegerProperty(amount);
    }

    public void takeDamage(int amount) {
        curHp.set(curHp.getValue() - amount);
    }

    /**
     * reset characters attributes to original state
     */
    public void reset() {
        for (Item i: unequippedInventory) {
            i.destroy();
        }
        unequippedInventory.clear();

        for (Item u : equippedItems.values()) {
            if (u != null)
                u.destroy();
        }

        equippedItems.put("Helmet", null);
        equippedItems.put("Armour", null);
        equippedItems.put("Shield", null);
        equippedItems.put("Weapon", null);
        equippedItems.put("TheOneRing", null);

        alliedSoldiers.clear();

        exp.set(0);
        gold.set(0);
        level.set(0);
        curHp.set(100);
        maxHp.set(100);
    }

    /**
     * IsInBattle getter
     */
    public boolean getIsInBattle() {
        return this.isInBattle;
    }
    
    /**
     * give character experience points
     * @param exp amount
     */
    public void addExp(int amount) {
        exp.set(exp.getValue() + amount);
    }

    /**
     * give gold to character
     * @param gold amount to give
     */
    public void addGold(int amount) {
        gold.set(gold.getValue() + amount);
    }

     /**
     * get gold from character
     */
    public int getGold() {
        return this.gold.intValue();
    }

    /**
     * takes gold from character
     * @param gold amount to be taken
     */
    public void removeGold(int gold) {
        this.gold.set(this.gold.getValue() - gold);
    }

    /**
     * isInBattle setter
     */
    public void setIsInBattle(boolean bool) {
        this.isInBattle = bool;
    }

    /**
     * equipped items getter
     * @return Map of equippedItems
     */
    public Map<String, Item> getEquippedItems() {
        return this.equippedItems;
    }

    /**
     * unequipped items getter
     * @return Map of equippedItems
     */
    public List<Item> getUnequippedItems() {
        return this.unequippedInventory;
    }

    /**
     * is character full hp
     * @return boolean
     */
    public boolean isMaxHp() {
        return curHp.get() == maxHp.get();
    }

    /**
     * spawn an item in the world (in character's unequippedInventory) and return the item entity
     * @return an item to be spawned in the controller as a JavaFX node
     * @pre valid item type
     * @post will create the respective item and add to unequppedInventory
     */
    public Item addUnequippedItem(String itemType, boolean isConfusingMode){
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        // If no slot is empty then delete oldest unequipped item, drop its loot and replace it
        if (firstAvailableSlot == null){
            removeItemByPositionInUnequippedInventoryItems(0);
            firstAvailableSlot = getFirstAvailableSlotForItem();
            destroyedItemDrop();
        }
        
        // now we insert the new sword, as we know we always pick up new items and destroy the oldest
        Item item = null;

        switch (itemType) {
            case "HealthPotion":
                item = new HealthPotion(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                break;
            case "Shield":
                item = new Shield(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                break;
            case "Armour":
                item = new Armour(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                break;
            case "Helmet":
                item = new Helmet(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                break;
            case "Sword":
                item = new Sword(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                break;
            case "Stake":
                item = new Stake(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                break;
            case "Staff":
                item = new Staff(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                break;
            case "DoggieCoin":
                item = new DoggieCoin(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                break;

            case "TheOneRing":
                if (isConfusingMode) {
                    item = new CompoundRareItem("TheOneRing", new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                    ((CompoundRareItem) item).add(new TheOneRing(null, null));
                    Item randomRare = new Random().nextInt(2) == 0 ? new Anduril(null, null) : new TreeStump(null, null);
                    ((CompoundRareItem) item).add(randomRare);
                } else {
                    item = new TheOneRing(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                }
                break;
            case "Anduril":
                if (isConfusingMode) {
                    item = new CompoundRareItem("Anduril", new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                    ((CompoundRareItem) item).add(new Anduril(null, null));
                    Item randomRare = new Random().nextInt(2) == 0 ? new TheOneRing(null, null) : new TreeStump(null, null);
                    ((CompoundRareItem) item).add(randomRare);
                } else {
                    item = new Anduril(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                }
                break;
            case "TreeStump":
                if (isConfusingMode) {
                    item = new CompoundRareItem("TreeStump", new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                    ((CompoundRareItem) item).add(new TreeStump(null, null));
                    Item randomRare = new Random().nextInt(2) == 0 ? new Anduril(null, null) : new TheOneRing(null, null);
                    ((CompoundRareItem) item).add(randomRare);
                } else {
                    item = new TreeStump(new SimpleIntegerProperty(firstAvailableSlot.getValue0()), new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
                }
        }

        unequippedInventory.add((Item) item);
        return item;
    }

    /**
     * equip item by type of item
     * @param itemType the item type to equip 
     * @return equippedItem to be rendered in Controller
     * @pre only equippable inventory items
     * @post character equips item
     */
    public Item equipItemByType(String itemType, boolean isConfusingMode) {
        Item newEquippedItem = null;
        switch (itemType) {
            case "Sword":
                newEquippedItem = new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
                equippedItems.put("Weapon", newEquippedItem);
                break;
            case "Stake":
                newEquippedItem = new Stake(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
                equippedItems.put("Weapon", newEquippedItem);
                break;
            case "Staff":
                newEquippedItem = new Staff(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
                equippedItems.put("Weapon", newEquippedItem);
                break;
            case "Helmet":
                newEquippedItem = new Helmet(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0));
                equippedItems.put("Helmet", newEquippedItem);
                break;
            case "Armour":
                newEquippedItem = new Armour(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
                equippedItems.put("Armour", newEquippedItem);
                break;
            case "Shield":
                newEquippedItem = new Shield(new SimpleIntegerProperty(2), new SimpleIntegerProperty(1));
                equippedItems.put("Shield", newEquippedItem);
                break;
            case "TheOneRing":
                if (isConfusingMode) {
                    newEquippedItem = new CompoundRareItem("TheOneRing", new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
                    ((CompoundRareItem) newEquippedItem).add(new TheOneRing(null, null));
                    Item randomRare = new Random().nextInt(2) == 0 ? new Anduril(null, null) : new TreeStump(null, null);
                    ((CompoundRareItem) newEquippedItem).add(randomRare);
                } else {
                    newEquippedItem = new TheOneRing(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
                }
                equippedItems.put("TheOneRing", newEquippedItem);
                break;
            case "Anduril":
                if (isConfusingMode) {
                    newEquippedItem = new CompoundRareItem("Anduril", new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
                    ((CompoundRareItem) newEquippedItem).add(new Anduril(null, null));
                    Item randomRare = new Random().nextInt(2) == 0 ? new TheOneRing(null, null) : new TreeStump(null, null);
                    ((CompoundRareItem) newEquippedItem).add(randomRare);
                } else {
                    newEquippedItem = new Anduril(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
                }
                equippedItems.put("Weapon", newEquippedItem);
                break;
            case "TreeStump":
                if (isConfusingMode) {
                    newEquippedItem = new CompoundRareItem("TreeStump", new SimpleIntegerProperty(2), new SimpleIntegerProperty(2));
                    ((CompoundRareItem) newEquippedItem).add(new TreeStump(null, null));
                    Item randomRare = new Random().nextInt(2) == 0 ? new TheOneRing(null, null) : new Anduril(null, null);
                    ((CompoundRareItem) newEquippedItem).add(randomRare);
                } else {
                    newEquippedItem = new TreeStump(new SimpleIntegerProperty(2), new SimpleIntegerProperty(2));
                }
                equippedItems.put("Shield", newEquippedItem);
                break;
        }

        return newEquippedItem;
    }

    /**
     * unequip item from equipped slot based on coordinates and add to first available slot
     * @param item to be equipped
     * @pre item is an equipped item which has to be (Helmet, Shield, Armour, Weapon, Ring)
     * @post character unequips item
     */
    public void unequipItem(Item item) {

        // now we add an empty slot to equippedItems
        String itemType = item.getType();

        if (itemType.equals("Sword") || itemType.equals("Stake") || itemType.equals("Staff") || itemType.equals("Anduril")) {
            equippedItems.put("Weapon", null);
        } else if (itemType.equals("TreeStump")) {
            equippedItems.put("Shield", null);
        } else {
            equippedItems.put(itemType, null);
        }

        item.destroy();
    }

    /**
     * remove an item by x,y coordinates
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public String removeUnequippedInventoryItemByCoordinates(int x, int y){
        Item item = getUnequippedInventoryItemByCoordinates(x, y);
        String res = item.getType();
        removeUnequippedInventoryItem(item);

        return res;
    }

    /**
     * remove an item from the unequipped inventory
     * @param item item to be removed
     */
    private void removeUnequippedInventoryItem(Item item){
        item.destroy();
        unequippedInventory.remove(item);
    }

    /**
     * remove item at a particular index in the unequipped inventory items list (this is ordered based on age in the starter code)
     * @param index index from 0 to length-1
     */
    private void removeItemByPositionInUnequippedInventoryItems(int index){
        Item item = unequippedInventory.get(index);
        item.destroy();
        unequippedInventory.remove(index);
    }

    /**
     * return an unequipped inventory item by x and y coordinates
     * assumes that no 2 unequipped inventory items share x and y coordinates
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    public Item getUnequippedInventoryItemByCoordinates(int x, int y){
        for (Item e: unequippedInventory){
            if ((e.getX() == x) && (e.getY() == y)){
                return e;
            }
        }
        return null;
    }

    /**
     * return an equipped item slot depending on x and y
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return equipped item slot by its type at the input position
     */
    public String getEquippedItemSlotByCoordinates(int x, int y){
        if (y == 0 && x == 0) {
            return "TheOneRing";
        } else if (y == 0 && x == 1) {
            return "Helmet";
        } else if (y == 1 && x == 0) {
            return "Weapon";
        } else if (y == 1 && x == 1) {
            return "Armour";
        } else if (y == 1 && x == 2) {
            return "Shield";
        }

        return null;
    }

    /**
     * return an equipped item depending on x and y
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return equipped item slot by its type at the input position
     */
    public Item getEquippedItemByCoordinates(int x, int y){
        if (y == 0 && x == 0) {
            return equippedItems.get("TheOneRing");
        } else if (y == 0 && x == 1) {
            return equippedItems.get("Helmet");
        } else if (y == 1 && x == 0) {
            return equippedItems.get("Weapon");
        } else if (y == 1 && x == 1) {
            return equippedItems.get("Armour");
        } else if (y == 1 && x == 2) {
            return equippedItems.get("Shield");
        }

        return null;
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the unequipped inventory
     * @return x,y coordinate pair as an optional (to check if null)
     */
    private Pair<Integer, Integer> getFirstAvailableSlotForItem(){
        // gets first available slot for an item
        // IMPORTANT - have to check by y then x, since trying to find first available slot defined by looking row by row
        for (int y = 0; y < unequippedInventoryHeight; y++){
            for (int x = 0; x < unequippedInventoryWidth; x++){
                if (getUnequippedInventoryItemByCoordinates(x, y) == null){
                    return new Pair<Integer, Integer>(x, y);
                }
            }
        }
        return null;
    }

    /**
     * the destroyed oldest item will give the character some loot
     */
    private void destroyedItemDrop() {
        addGold(8);
        addExp(3);
    }

    public List<Item> getUnequippedInventory() {
        return unequippedInventory;
    }
    
    /**
     * checks if there is a HealthPotion in inventory
     */
    public boolean hasHealthPotion() {
        for (int y = 0; y < unequippedInventoryHeight; y++){
            for (int x = 0; x < unequippedInventoryWidth; x++){
                Item item = getUnequippedInventoryItemByCoordinates(x, y);
                if (item != null && item.getType().equals("HealthPotion")){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * use first health potion
     */
    public void useAHealthPotion() {
        for (int y = 0; y < unequippedInventoryHeight; y++){
            for (int x = 0; x < unequippedInventoryWidth; x++){
                Item item = getUnequippedInventoryItemByCoordinates(x, y);
                if (item != null && item.getType().equals("HealthPotion")){
                    removeUnequippedInventoryItemByCoordinates(x, y);
                    addHealth(maxHp.get() / 3);
                    return;
                }
            }
        }
    }

    /**
     * Use theOneRing if character has one equipped else return null
     * @return TheOneRing item
     */
    public Item useTheOneRing() {
        Item ring = equippedItems.get("TheOneRing");
        if (ring == null) {
            boolean hasRing = false;
            // if confusing mode, check for compound rare items containing one ring
            for (Item item : equippedItems.values()) {
                if (item == null)
                    continue;
                
                if (item instanceof CompoundRareItem) {
                    List<Item> children = ((CompoundRareItem) item).getItems();
                    for (int i = 0; i < children.size(); ++i) {
                        if (children.get(i).getType().equals("TheOneRing")) {
                            ring = item;
                            hasRing = true;
                        }
                    }
                }
            }

            if (!hasRing)
                return null;
        }
        
        unequipItem(ring);
        return ring;
    }

    public List<Integer> getAlliedSoldiers() {
        return alliedSoldiers;
    }

    /**
     * Calculate character's damage on next attack
     * @param enemy type that the character is attacking
     * @return int value of attack damage
     */
    public int dealDamage(Enemy targetEnemy) {
        int res = baseAtk;
        String enemyType = targetEnemy.getEnemyType();

        // Check what items the character has equipped and use their attackEffect
        if (equippedItems.get("Helmet") != null) {
            res = equippedItems.get("Helmet").attackEffect(res, enemyType);
        }

        // if confusing mode, check for compound rare items containing one ring
        for (Item item : equippedItems.values()) {
            if (item == null)
                continue;
            
            if (item instanceof CompoundRareItem) {
                List<Item> children = ((CompoundRareItem) item).getItems();
                if (children.get(1).getType().equals("Anduril")) {
                    res += 10;
                }
            }
        }

        return res;
    }

    /**
     * Calculates how much damage the character receives based on defense and equipped items
     * @param amount of damage to negate
     */
    public int damageTaken(int damage, boolean vsBoss) {
        int res = damage;

        if (equippedItems.get("Armour") != null) {
            res = equippedItems.get("Armour").defenseEffect(res, vsBoss);
        }

        for (Item item : equippedItems.values()) {
            if (item == null || item.getType().equals("Armour"))
                continue;

            if (item.defenseEffect(res, vsBoss) != -1) {
                res = item.defenseEffect(res, vsBoss);
            }
        }

        // Character cannot take less than 1 damage
        if (res < 1) {
            takeDamage(1);
            return 1;
        } else {
            takeDamage(res);
            return res;
        }
    }
}
