package test.entity.moving.enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.javatuples.Pair;
import java.util.Map;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.entity.moving.*;
import unsw.loopmania.entity.moving.enemy.*;
import unsw.loopmania.entity.notmoving.building.*;

public class VampireTest {
    // Helper function to create an ordered path
    // The path is the tip / edges of a 6 x 6 world, path starting from 0, 0
    private List<Pair<Integer, Integer>> createOrderedPath() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        for (int x = 0; x <= 5; x++) { // Go right 5 tiles (0 to 5)
            Pair<Integer, Integer> route = new Pair<>(x, 0);
            orderedPath.add(route);
        }
        for (int y = 0; y <= 5; y++) { // Go down 5 tiles (0 to 5)
            Pair<Integer, Integer> route = new Pair<>(5, y);
            orderedPath.add(route);
        }
        for (int x = 5; x >= 0; x--) { // Go left 5 tiles (5 to 0)
            Pair<Integer, Integer> route = new Pair<>(x, 5);
            orderedPath.add(route);
        }
        for (int y = 5; y >= 0; y--) { // Go up 5 tiles (5 to 0)
            Pair<Integer, Integer> route = new Pair<>(0, y);
            orderedPath.add(route);
        } 

        return orderedPath;
    }

    // Test the constructor of Vampire
    // Its stats should be the same as the predetermined stats for Vampire in assumptions.md
    @Test
    public void testVampireConstructor() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Vampire originVamp = new Vampire(curPos);

        // Compare the stats of the initial new constructed vampire
        // with the desription of a vampire in the assumptions (e.g. hp, atk, etc.)
        assertEquals(originVamp.getEnemyMaxHp(), 100);
        assertEquals(originVamp.getEnemyCurHp(), 100);
        assertEquals(originVamp.getEnemyAtk(), 18);
        assertEquals(originVamp.getBattleRadius(), 5);
        assertEquals(originVamp.getSupportRadius(), 7);
        assertEquals(originVamp.getAttackOrder(), 5);
        assertTrue(originVamp.getGoldDrop() >= 30 && originVamp.getGoldDrop() <= 50);
        assertEquals(originVamp.getXpDrop(), 10);
        assertEquals(originVamp.getEnemyType(), "Vampire");
    }

    // Test the battleRadius and supportRadius of vampires if they work as it should
    @Test
    public void testBattleSupportRadiiVampires() {
        List<Pair<Integer, Integer>> orderedPath = createOrderedPath();
        // Create multiple Vampires with the following positions
        // Vampire A is located in (0, 0)
        PathPosition posA = new PathPosition(0, orderedPath);
        Vampire vampA = new Vampire(posA);
        // Vampire B is located in (5, 0)
        PathPosition posB = new PathPosition(6, orderedPath);
        Vampire vampB = new Vampire(posB);
        // Vampire C is located in (5, 5)
        PathPosition posC = new PathPosition(11, orderedPath);
        Vampire vampC = new Vampire(posC);

        // Checks
        // Case A: considering character is in (0,0)
        assertTrue(vampA.isInEnemyBattleRange(0, 0)); // (0,0) in range of (0, 0) as tile is literally the same
        assertTrue(vampA.isInEnemySupportRange(0, 0));
        assertFalse(vampB.isInEnemyBattleRange(0, 0)); // (0,0) not in range of (5, 0) as on edge of radius (not inside)
        assertTrue(vampB.isInEnemySupportRange(0, 0)); // Should be in the support range tho
        assertFalse(vampC.isInEnemyBattleRange(0, 0));
        assertFalse(vampC.isInEnemySupportRange(0, 0)); // (5,5) out of 7 tiles support radius of VampC
        
        // Case B: considering character is in  (5, 2)
        assertFalse(vampA.isInEnemyBattleRange(5, 2)); // (5,2) not in range of (0, 0) as out battle radius
        assertTrue(vampA.isInEnemySupportRange(5, 2));
        assertTrue(vampB.isInEnemyBattleRange(5, 2)); // (5,2) in range of (5, 0) [2 tiles diff]
        assertTrue(vampB.isInEnemySupportRange(5, 2));
        assertTrue(vampC.isInEnemyBattleRange(5, 2)); // (5,2) in range of (5, 5) [3 tiles diff]
        assertTrue(vampC.isInEnemySupportRange(5, 2));
    }

    // Test item drops of Vampires
    // The actual action of enemies dropping loot is done by LoopManiaWorldController.reactToEnemyDefeat
    // HOWEVER, that is a private function and cannot be called for testing.
    // In exchange this test only checks if the type of items that can drop are correct
    @Test
    public void testLootDrops() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Vampire testVamp = new Vampire(curPos);

        // Check the content of the lootDrop for the zombie
        // The items that can be dropped should be the same with our assumptions 
        Map<String, Integer> lootDrops = testVamp.getLootDrops();
        assertEquals(lootDrops.size(), 7); // Can only drop 7 common items
        lootDrops.containsKey("Sword");
        lootDrops.containsKey("Staff");
        lootDrops.containsKey("Stake");
        lootDrops.containsKey("Armour");
        lootDrops.containsKey("Helmet");
        lootDrops.containsKey("Shield");
        lootDrops.containsKey("HealthPotion");
    }


    // Test the critical attacks of vampires
    // Vampires should be able to do additional damage on crit
    @Test
    public void testCriticalAttackVampire() {
        List<Pair<Integer, Integer>> orderedPath = createOrderedPath();   
        PathPosition curPos = new PathPosition(0, orderedPath);
        Vampire testVamp = new Vampire(curPos);

        for (int i = 0; i < 100; i++) {
            // non crit attack without shield
            double nmlDmg = testVamp.getPossiblyCritAtk(false);
            if (nmlDmg == testVamp.getEnemyAtk()) {
                assertEquals(nmlDmg, testVamp.getEnemyAtk());
            }
        }

        for (int i = 0; i < 100; i++) {
            // non crit attack with shield
            double nmlDmg = testVamp.getPossiblyCritAtk(true);
            if (nmlDmg == testVamp.getEnemyAtk()) {
                assertEquals(nmlDmg, testVamp.getEnemyAtk());
            }
        }

        for (int i = 0; i < 100; i++) {
            // crit attack with shield
            double nmlDmg = testVamp.getPossiblyCritAtk(true);
            if (nmlDmg > testVamp.getEnemyAtk()) {
                assertTrue(nmlDmg > testVamp.getEnemyAtk());
            }
        }

        for (int i = 0; i < 100; i++) {
            // crit attack without shield
            double nmlDmg = testVamp.getPossiblyCritAtk(false);
            if (nmlDmg > testVamp.getEnemyAtk()) {
                assertTrue(nmlDmg > testVamp.getEnemyAtk());
            }
        }
    }

    /**
     * Many cases involving campfires especially
     */
    @Test
    public void testMove() {
        List<Pair<Integer, Integer>> orderedPath = Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2));
        PathPosition curPos = new PathPosition(0, orderedPath);
        Vampire testVamp = new Vampire(curPos);
        

        CampfireBuilding campA = new CampfireBuilding(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        CampfireBuilding campB = new CampfireBuilding(new SimpleIntegerProperty(4), new SimpleIntegerProperty(2));
        // CampA is closer than campB
        List<Building> buildings =  new ArrayList<>();
        buildings.add(campA);
        buildings.add(campB);

        // Case 1: no campfires, vamp can move anywhere
        for (int i = 0; i < 100; i++) {
            testVamp.move(null, null);
            if (testVamp.getX() == 0 && testVamp.getY() == 2) {
                assertEquals(testVamp.getX(), 0);
                assertEquals(testVamp.getY(), 2);
            } else if (testVamp.getX() == 0 && testVamp.getY() == 1) {
                assertEquals(testVamp.getX(), 0);
                assertEquals(testVamp.getY(), 1);
            }
        }

        // Case 2: campfires running away from them
        for (int i = 0; i < 100; i++) {
            testVamp.move(buildings, orderedPath);
            if (testVamp.getX() == 0 && testVamp.getY() == 2) {
                assertEquals(testVamp.getX(), 0);
                assertEquals(testVamp.getY(), 2);
            } else if (testVamp.getX() == 0 && testVamp.getY() == 1) {
                assertEquals(testVamp.getX(), 0);
                assertEquals(testVamp.getY(), 1);
            }
        }

    }
}
