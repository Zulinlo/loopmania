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

import unsw.loopmania.entity.moving.*;
import unsw.loopmania.entity.moving.enemy.*;

public class ZombieTest {
    // Helper function to create an ordered path
    // The path is the tip / edges of a 4 x 4 world, path starting from 0, 0
    private List<Pair<Integer, Integer>> createOrderedPath() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        for (int x = 0; x < 4; x++) { // Go right 4 tiles (0 to 3)
            Pair<Integer, Integer> route = new Pair<>(x, 0);
            orderedPath.add(route);
        }
        for (int y = 0; y < 4; y++) { // Go down 4 tiles (0 to 3)
            Pair<Integer, Integer> route = new Pair<>(3, y);
            orderedPath.add(route);
        }
        for (int x = 3; x >= 0; x--) { // Go left 4 tiles (3 to 0)
            Pair<Integer, Integer> route = new Pair<>(x, 3);
            orderedPath.add(route);
        }
        for (int y = 3; y >= 0; y--) { // Go up 4 tiles (3 to 0)
            Pair<Integer, Integer> route = new Pair<>(0, y);
            orderedPath.add(route);
        } 

        return orderedPath;
    }

    // Tests the constructor of Zombie
    // Its stats should be the same as the predetermined stats for Zombie in assumptions.md
    @Test
    public void testZombieConstructor() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Zombie originZombie = new Zombie(curPos);

        // Compare the stats of the initial new constructed zombie
        // with the desription of a zobmie in the assumptions (e.g. hp, atk, etc.)
        assertEquals(originZombie.getEnemyMaxHp(), 24);
        assertEquals(originZombie.getEnemyCurHp(), 24);
        assertEquals(originZombie.getEnemyAtk(), 10);
        assertEquals(originZombie.getBattleRadius(), 3);
        assertEquals(originZombie.getSupportRadius(), 2);
        assertEquals(originZombie.getAttackOrder(), 3);
        assertTrue(originZombie.getGoldDrop() >= 8 && originZombie.getGoldDrop() <= 10);
        assertEquals(originZombie.getXpDrop(), 3);
        assertEquals(originZombie.getEnemyType(), "Zombie");
    }

    // Test the battleRadius and supportRadius of zombies if they work as it should
    @Test
    public void testBattleSupportRadiiZombies() {
        List<Pair<Integer, Integer>> orderedPath = createOrderedPath();
        // Create multiple Vampires with the following positions
        // ZombieA is located in (1, 0)
        PathPosition posA = new PathPosition(1, orderedPath);
        Zombie zombA = new Zombie(posA);
        // ZombieB is located in (3, 0)
        PathPosition posB = new PathPosition(4, orderedPath);
        Zombie zombB = new Zombie(posB);
        // Zombie C is located in (2, 3)
        PathPosition posC = new PathPosition(10, orderedPath);
        Zombie zombC = new Zombie(posC);

        // Checks
        // Case A: considering character is in (0,0)
        assertTrue(zombA.isInEnemyBattleRange(0, 0)); // (0,0) in range of (0, 0) as tile is literally the same
        assertTrue(zombA.isInEnemySupportRange(0, 0));
        assertFalse(zombB.isInEnemyBattleRange(0, 0)); // (0,0) not in range of (3, 0) as on edge of radius (not inside)
        assertFalse(zombB.isInEnemySupportRange(0, 0));
        assertFalse(zombC.isInEnemyBattleRange(0, 0));
        assertFalse(zombC.isInEnemySupportRange(0, 0)); // (2,3) so far from (0,0)
        
        // Case B: considering character is in  (3, 2)
        assertTrue(zombA.isInEnemyBattleRange(3, 2)); // (3,2) not in range of (1, 0) as out of battle radius
        assertFalse(zombA.isInEnemySupportRange(3, 2));
        assertTrue(zombB.isInEnemyBattleRange(3, 2)); // (3,2) in range of (3, 0) [2 tiles diff]
        assertFalse(zombB.isInEnemySupportRange(3, 2));
        assertTrue(zombC.isInEnemyBattleRange(3, 2)); // (3,2) in range of (2, 3) [1 tiles diff]
        assertFalse(zombC.isInEnemySupportRange(3, 2));
    }

    // Test item drops of Zombies
    // The actual action of enemies dropping loot is done by LoopManiaWorldController.reactToEnemyDefeat
    // HOWEVER, that is a private function and cannot be called for testing.
    // In exchange this test only checks if the type of items that can drop are correct.
    @Test
    public void testLootDrops() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Zombie testZomb = new Zombie(curPos);

        // Check the content of the lootDrop for the zombie
        // The items that can be dropped should be the same with our assumptions 
        Map<String, Integer> lootDrops = testZomb.getLootDrops();
        assertEquals(lootDrops.size(), 7); // Can only drop 7 common items
        lootDrops.containsKey("Sword");
        lootDrops.containsKey("Staff");
        lootDrops.containsKey("Stake");
        lootDrops.containsKey("Armour");
        lootDrops.containsKey("Helmet");
        lootDrops.containsKey("Shield");
        lootDrops.containsKey("HealthPotion");
    }

    // Test the critical strike of zombie
    @Test
    public void testZombieCrit() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Zombie testZomb = new Zombie(curPos);

        // Success
        for (int i = 0; i < 100; i++) {
            boolean ret = testZomb.isCrit();
            if (ret) {
                assertTrue(ret);
                break;
            }
        }

        // Fail crit
        for (int i = 0; i < 100; i++) {
            boolean ret = !(testZomb.isCrit());
            if (ret) {
                assertTrue(ret);
                break;
            }
        }
    }

    // Test zombie's movement
    @Test
    public void testMove() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Zombie testZomb = new Zombie(curPos);

        // Case 1: zomb does not move
        for (int i = 0; i < 100; i++) {
            testZomb.move(null, null);
            if (testZomb.getX() == 0 && testZomb.getY() == 1) {
                assertEquals(testZomb.getX(), 0);
                assertEquals(testZomb.getY(), 1);
                break;
            }
        }

        // Case 2: zomb move down the path
        for (int i = 0; i < 100; i++) {
            testZomb.move(null, null);
            if (testZomb.getX() == 0 && testZomb.getY() == 2) {
                assertEquals(testZomb.getX(), 0);
                assertEquals(testZomb.getY(), 2);
                break;
            }
        }

        // Case 3: zomb moves up the path
        for (int i = 0; i < 100; i++) {
            testZomb.move(null, null);
            if (testZomb.getX() == 0 && testZomb.getY() == 1) {
                assertEquals(testZomb.getX(), 0);
                assertEquals(testZomb.getY(), 1);
                break;
            }
        }
    }
}
