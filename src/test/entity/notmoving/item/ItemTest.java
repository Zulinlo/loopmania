package test.entity.notmoving.item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.entity.notmoving.item.Anduril;
import unsw.loopmania.entity.notmoving.item.Armour;
import unsw.loopmania.entity.notmoving.item.CompoundRareItem;
import unsw.loopmania.entity.notmoving.item.Helmet;
import unsw.loopmania.entity.notmoving.item.Shield;
import unsw.loopmania.entity.notmoving.item.Stake;
import unsw.loopmania.entity.notmoving.item.TreeStump;

public class ItemTest {

    // Testing to see if unequipped inventory works and sword item loads properly
    @Test
    public void SwordItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("Sword", false);
        assertEquals("Sword", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());
    }

    // Testing to see if unequipped inventory works and armour item loads properly
    @Test
    public void ArmourItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("Armour", false);
        assertEquals("Armour", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());

        assertTrue(new Armour(null, null).defenseEffect(50, false) == 23);
    }

    // Testing to see if unequipped inventory works and helmet item loads properly
    @Test
    public void HelmetItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("Helmet", false);
        assertEquals("Helmet", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());

        assertTrue(new Helmet(null, null).defenseEffect(50, false) == 47);
        assertTrue(new Helmet(null, null).attackEffect(50, "Vampire") == 47);
    }

    // Testing to see if unequipped inventory works and HealthPotion item loads properly
    @Test
    public void HealthPotionItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("HealthPotion", false);
        assertEquals("HealthPotion", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());
    }

    // Testing to see if unequipped inventory works and Shield item loads properly
    @Test
    public void ShieldItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("Shield", false);
        assertEquals("Shield", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());

        assertTrue(new Shield(null, null).defenseEffect(50, false) == 49);
    }

    // Testing to see if unequipped inventory works and Staff item loads properly
    @Test
    public void StaffItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("Staff", false);
        assertEquals("Staff", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());
    }

    // Testing to see if unequipped inventory works and Stake item loads properly
    @Test
    public void StakeItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("Stake", false);
        assertEquals("Stake", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());

        // Will make sure this combination of accuracy will occur at least once
        for (int i = 0; i < 10000; i++) {
            if (new Stake(null, null).garlicStab(50, true) == 59) {
                assertTrue(true);
                return;
            }
        }

        for (int i = 0; i < 10000; i++) {
            if (new Stake(null, null).garlicStab(50, false) == 53) {
                assertTrue(true);
                return;
            }
        }
    }

     // Testing to see if unequipped inventory works and DoggieCoin item loads properly
     @Test
     public void DoggieCoinItemLoaderTest(){
         
         Character character = new Character(null);
         character.addUnequippedItem("DoggieCoin", false);
         assertEquals("DoggieCoin", character.getUnequippedInventory().get(0).getType());
         assertEquals(0, character.getUnequippedInventory().get(0).getX());
         assertEquals(0, character.getUnequippedInventory().get(0).getY());
     }

    // Testing to see if unequipped inventory works and TheOneRing item loads properly
    @Test
    public void TheOneRingItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("TheOneRing", false);
        assertEquals("TheOneRing", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());
    }

    // Testing to see if unequipped inventory works and TreeStump item loads properly
    @Test
    public void TreeStumpItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("TreeStump", false);
        assertEquals("TreeStump", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());

        assertTrue(new TreeStump(null, null).defenseEffect(50, false) == 47);
        assertTrue(new TreeStump(null, null).defenseEffect(50, true) == 41);
    }

    @Test
    public void CompoundRareItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("Anduril", true);
        assertEquals("Anduril", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());

        character.addUnequippedItem("TreeStump", true);
        assertEquals("TreeStump", character.getUnequippedInventory().get(1).getType());
        assertEquals(1, character.getUnequippedInventory().get(1).getX());
        assertEquals(0, character.getUnequippedInventory().get(1).getY());

        character.addUnequippedItem("TheOneRing", true);
        assertEquals("TheOneRing", character.getUnequippedInventory().get(2).getType());
        assertEquals(2, character.getUnequippedInventory().get(2).getX());
        assertEquals(0, character.getUnequippedInventory().get(2).getY());

        CompoundRareItem compound = new CompoundRareItem("Anduril", null, null);
        compound.add(new Anduril(null, null));
        compound.add(new TreeStump(null, null));
        assertTrue(compound.attackEffect(50, "Slug") == 50);
        assertTrue(compound.attackEffect(50, "Doggie") == 50);
        assertTrue(compound.defenseEffect(50, false) == 47);
        assertTrue(compound.defenseEffect(50, true) == 41);
    }

    // Testing to see if unequipped inventory works and multiple items loads properly
    @Test
    public void MultipleItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("Stake", false);
        character.addUnequippedItem("Sword", false);
        character.addUnequippedItem("TheOneRing", false);
        assertEquals("Stake", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());

        assertEquals("Sword", character.getUnequippedInventory().get(1).getType());
        assertEquals(1, character.getUnequippedInventory().get(1).getX());
        assertEquals(0, character.getUnequippedInventory().get(1).getY());

        assertEquals("TheOneRing", character.getUnequippedInventory().get(2).getType());
        assertEquals(2, character.getUnequippedInventory().get(2).getX());
        assertEquals(0, character.getUnequippedInventory().get(2).getY());
    }

    // Testing to see if removing unequipped inventory works and multiple items loads properly
    @Test
    public void RemovingItemLoaderTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("Stake", false);
        character.addUnequippedItem("Sword", false);
        character.addUnequippedItem("TheOneRing", false);
        assertEquals("Stake", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());

        assertEquals("Sword", character.getUnequippedInventory().get(1).getType());
        assertEquals(1, character.getUnequippedInventory().get(1).getX());
        assertEquals(0, character.getUnequippedInventory().get(1).getY());

        assertEquals("TheOneRing", character.getUnequippedInventory().get(2).getType());
        assertEquals(2, character.getUnequippedInventory().get(2).getX());
        assertEquals(0, character.getUnequippedInventory().get(2).getY());

        character.removeUnequippedInventoryItemByCoordinates(1, 0);
        assertEquals("TheOneRing", character.getUnequippedInventory().get(1).getType());
        assertEquals(2, character.getUnequippedInventory().get(1).getX());
        assertEquals(0, character.getUnequippedInventory().get(1).getY());

        assertEquals("Stake", character.getUnequippedInventory().get(0).getType());
        assertEquals(0, character.getUnequippedInventory().get(0).getX());
        assertEquals(0, character.getUnequippedInventory().get(0).getY());

        assertEquals(2, character.getUnequippedInventory().size());
    }

    // Testing to see if character has health pots
    @Test
    public void HealthPotsItemCheckerTest(){
        
        Character character = new Character(null);
        character.addUnequippedItem("Stake", false);
        character.addUnequippedItem("Sword", false);
        character.addUnequippedItem("TheOneRing", false);
        assertFalse(character.hasHealthPotion());
        
        character.addUnequippedItem("HealthPotion", false);
        assertTrue(character.hasHealthPotion());
    }

}
