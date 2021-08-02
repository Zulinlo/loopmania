package test.entity.moving;

import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.entity.notmoving.item.*;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class CharacterTest {
    /**
     * Unit Testing (One Behaviour no component interaction)
     */
    @Test
    public void characterTestEquippableItemsNotInBattle() {
        /**
         * Excepts Character to be able to equip Helmet, Shield, Armour, Weapon, Ring and new equips swap
         * When not in battle
         */
        Character character = new Character(null);


        Item helmet = character.equipItemByType("Helmet", false);
        Item shield = character.equipItemByType("Shield", false);
        Item armour = character.equipItemByType("Armour", false);
        character.equipItemByType("Sword", false);
        Item stake = character.equipItemByType("Stake", false);
        Item ring = character.equipItemByType("TheOneRing", false);

        assertEquals(character.getEquippedItems().get("Helmet"), helmet);
        assertEquals(character.getEquippedItems().get("Shield"), shield);
        assertEquals(character.getEquippedItems().get("Armour"), armour);
        assertEquals(character.getEquippedItems().get("Weapon"), stake);
        assertEquals(character.getEquippedItems().get("TheOneRing"), ring);

        Item rare1 = character.equipItemByType("Anduril", true);
        assertEquals(character.getEquippedItems().get("Weapon"), rare1);

        Item rare2 = character.equipItemByType("TreeStump", true);
        assertEquals(character.getEquippedItems().get("Shield"), rare2);

        Item rare3 = character.equipItemByType("TheOneRing", true);
        assertEquals(character.getEquippedItems().get("TheOneRing"), rare3);

        Item rare4 = character.equipItemByType("TreeStump", false);
        assertEquals(character.getEquippedItems().get("Shield"), rare4);
    }

    @Test
    public void characterTestDestroyItemWhenFull() {
        /**
         * Tests upon unequip when inventory is full that oldest item is destroyed
         */
        Character character = new Character(null);
        
        character.addUnequippedItem("TheOneRing", false);

        for (int i = 0; i < 16; ++i) 
            character.addUnequippedItem("Sword", false);
        
        // Checks the character gets 8 gold from item destruction
        assertEquals(character.getGold(), 8);
        assertEquals(character.getExp(), 3);

        // Checks the character's oldest item (shield) is destroyed by seeing if inventory only consists of swords and one ringt
        for (int i = 0; i < 16; ++i)
            assertEquals(character.getUnequippedItems().get(i).getType(), "Sword");
    }
/* this is frontend so unneeded
    @Test
    public void characterTestCanSwapWhenFull() {
         * Tests when inventory is full, can swap item from equipped to inventory
        Character character = new Character(null);
        

        character.equipItemByType("Stake");

        for (int i = 0; i < 16; ++i) 
            character.addUnequippedItem("Sword");

        character.equipItemByType("Staff");
        character.addUnequippedItem("Stake");
        
        // Checks the character gets 0 gold as no item destroyed
        assertEquals(character.getGold(), 0);
        assertEquals(character.getUnequippedItems().get(0).getType(), "Sword");

        assertEquals(character.getUnequippedItems().get(0).getType(), "Stake");
        
        character.equipItemByType("Sword");
        character.addUnequippedItem("Staff");
        assertEquals(character.getUnequippedItems().get(1).getType(), "Staff");
    }
*/ 

/* also done in frontend so unneeded
    @Test
    public void characterTestCantEquipInBattle() {
        
         * Tests when Character is in battle then cannot equip
         
        Character character = new Character(null);
        
        character.addUnequippedItem("Stake");

        character.setIsInBattle(true);
        character.equip(stake);

        assertEquals(character.getInventory().getItems().size(), 1);
        assertEquals(character.getEquipped().get("Weapon"), null);
    }
*/

    @Test
    public void useHPPot() {
        /**
         * Tests using a health potion to regain 20hp
         */
        Character character = new Character(null);
        character.useAHealthPotion();

        character.takeDamage(40);
        character.useAHealthPotion();
        assertEquals(60, character.getCurHp());

        character.addUnequippedItem("HealthPotion", false);
        character.addUnequippedItem("HealthPotion", false);
        character.addUnequippedItem("HealthPotion", false);
        character.useAHealthPotion();
        assertEquals(93, character.getCurHp());

        character.useAHealthPotion();
        assertEquals(100, character.getCurHp());

        character.useAHealthPotion();
        assertEquals(100, character.getCurHp());
    }

    @Test
    public void equip() {
        /**
         * is he equipping right and the right items
         */

        Character character = new Character(null);
        assertTrue(character.isMaxHp());
        character.takeDamage(1);

        assertFalse(character.isMaxHp());

        character.addUnequippedItem("Sword", false);
        character.equipItemByType("Sword", false);

        character.addUnequippedItem("TheOneRing", false);
        character.equipItemByType("TheOneRing", false);

        character.addUnequippedItem("Shield", false);
        character.equipItemByType("Shield", false);

        character.addUnequippedItem("Helmet", false);
        character.equipItemByType("Helmet", false);

        character.addUnequippedItem("Armour", false);
        character.equipItemByType("Armour", false);

        assertEquals("Weapon", character.getEquippedItemSlotByCoordinates(0, 1));
        assertEquals("TheOneRing", character.getEquippedItemSlotByCoordinates(0, 0));
        assertEquals("Helmet", character.getEquippedItemSlotByCoordinates(1, 0));
        assertEquals("Armour", character.getEquippedItemSlotByCoordinates(1, 1));
        assertEquals("Shield", character.getEquippedItemSlotByCoordinates(2, 1));

        assertEquals("Sword", character.getEquippedItemByCoordinates(0, 1).getType());
        assertEquals("TheOneRing", character.getEquippedItemByCoordinates(0, 0).getType());
        assertEquals("Helmet", character.getEquippedItemByCoordinates(1, 0).getType());
        assertEquals("Armour", character.getEquippedItemByCoordinates(1, 1).getType());
        assertEquals("Shield", character.getEquippedItemByCoordinates(2, 1).getType());
    }

    @Test
    public void useTheOneRing() {

        Character character = new Character(null);

        assertTrue(character.useTheOneRing() == null);

        Item ring = character.equipItemByType("TheOneRing", false);
        assertTrue(character.useTheOneRing().equals(ring));

        Item ring1 = character.equipItemByType("TheOneRing", true);
        assertTrue(character.useTheOneRing().equals(ring1));

        // Make a fake ring guarnteed to have theonering as bonus attribute at least once
        for (int i = 0; i < 1000; i++) {
            Item fakeRing = character.equipItemByType("Anduril", true);
            if (((CompoundRareItem) fakeRing).getItems().get(1).getType().equals("TheOneRing")) {
                assertTrue(character.useTheOneRing().equals(fakeRing));
                return;
            }
        }
    }

    @Test
    public void damageTaken() {
        Character character = new Character(null);

        assertTrue(character.damageTaken(100, false) == 100);

        character.equipItemByType("Helmet", false);
        assertTrue(character.damageTaken(100, false) == 97);

        character.equipItemByType("Armour", false);
        assertTrue(character.damageTaken(100, false) == 45);
        
        assertTrue(character.damageTaken(2, false) == 1);

    }

    // reset test
    @Test
    public void testReset() {

        Character character = new Character(null);
        assertTrue(character.isMaxHp());
        character.takeDamage(1);

        assertFalse(character.isMaxHp());
        character.addUnequippedItem("Sword", false);
        character.equipItemByType("Sword", false);
        character.addUnequippedItem("TheOneRing", false);
        character.equipItemByType("TheOneRing", false);
        character.addUnequippedItem("Shield", false);
        character.equipItemByType("Shield", false);
        character.addUnequippedItem("Helmet", false);
        character.equipItemByType("Helmet", false);
        character.addUnequippedItem("Armour", false);
        character.equipItemByType("Armour", false);

        character.reset();
        assertTrue(character.isMaxHp());
        assertTrue(character.getUnequippedInventory().size() == 0);
        assertEquals(character.getEquippedItems().size(), 5);
        assertEquals(character.getGold(), 0);
        assertEquals(character.getExp(), 0);
        assertEquals(character.getLevel(), 0);
        assertEquals(character.getCurHp(), 100);
    }
}
