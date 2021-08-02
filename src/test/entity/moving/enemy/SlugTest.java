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

public class SlugTest {
    // Helper function to create an ordered path
    // The path is the tip / edges of a 4 x 4 world, path starting from 0, 0
    private List<Pair<Integer, Integer>> createOrderedPath() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        for (int x = 1; x < 5; x++) { // Go right 4 tiles (1 to 4)
            Pair<Integer, Integer> route = new Pair<>(x, 1);
            orderedPath.add(route);
        }
        for (int y = 1; y < 5; y++) { // Go down 4 tiles (1 to 4)
            Pair<Integer, Integer> route = new Pair<>(4, y);
            orderedPath.add(route);
        }
        for (int x = 4; x > 0; x--) { // Go left 4 tiles (4 to 1)
            Pair<Integer, Integer> route = new Pair<>(x, 4);
            orderedPath.add(route);
        }
        for (int y = 4; y > 0; y--) { // Go up 4 tiles (4 to 1)
            Pair<Integer, Integer> route = new Pair<>(1, y);
            orderedPath.add(route);
        } 

        return orderedPath;
    }

    // Test the constructor of Slug
    // Its stats should be the same as the predetermined stats for slugs in assumptions.md
    @Test
    public void testSlugConstructor() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Slug originSlug = new Slug(curPos);

        // Compare the stats of the initial new constructed slug
        // with the desription of a slug in the assumptions (e.g. hp, atk, etc.)
        assertEquals(originSlug.getEnemyMaxHp(), 12);
        assertEquals(originSlug.getEnemyCurHp(), 12);
        assertEquals(originSlug.getEnemyAtk(), 6);
        assertEquals(originSlug.getBattleRadius(), 2);
        assertEquals(originSlug.getSupportRadius(), 2);
        assertEquals(originSlug.getAttackOrder(), 4);
        assertEquals(originSlug.getGoldDrop(), 5);
        assertEquals(originSlug.getXpDrop(), 1);
        assertEquals(originSlug.getEnemyType(), "Slug");
    }

    // Tries to set curHp over maxHp, should not cause error
    // Sets cur hp to value of max hp instead
    @Test
    public void testSetHpOverMaxHP() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Slug originSlug = new Slug(curPos);

        originSlug.setEnemyCurHp(9999);
        assertEquals(originSlug.getEnemyCurHp(), originSlug.getEnemyMaxHp());
    }

    // Simple test to see if enemies can take dmg and hp gets reduced
    @Test
    public void testTakeDamage() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Slug originSlug = new Slug(curPos);

        originSlug.takeDamage(4);
        assertEquals(originSlug.getEnemyCurHp(), 8);
        assertEquals(originSlug.getEnemyMaxHp(), 12);
        originSlug.takeDamage(8);
        assertEquals(originSlug.getEnemyCurHp(), 0);
        assertEquals(originSlug.getEnemyMaxHp(), 12);
    }

    // Test the battleRadius and supportRadius of slugs if they work as it should
    @Test
    public void testBattleSupportRadiiSlugs() {
        List<Pair<Integer, Integer>> orderedPath = createOrderedPath();
        // Create multiple slugs with the following positions
        // Slug A is located in (2, 1)
        PathPosition posA = new PathPosition(1, orderedPath);
        Slug SlugA = new Slug(posA);
        // Slug B is located in (2, 2)
        PathPosition posB = new PathPosition(2, orderedPath);
        Slug SlugB = new Slug(posB);
        // Slug C is located in (4, 2)
        PathPosition posC = new PathPosition(4, orderedPath);
        Slug SlugC = new Slug(posC);

        // Checks
        // Case A: considering character is in (1,1)
        assertTrue(SlugA.isInEnemyBattleRange(1, 1)); // (1,1) in range of (2, 1) as 1 tile diff
        assertTrue(SlugA.isInEnemySupportRange(1, 1));
        assertFalse(SlugB.isInEnemyBattleRange(1, 1)); // (1,1) not in range of (3, 1) as on edge of radius (not inside)
        assertFalse(SlugB.isInEnemySupportRange(1, 1));
        assertFalse(SlugC.isInEnemyBattleRange(1, 1)); // (1,1) not in range of (4, 2)
        assertFalse(SlugC.isInEnemySupportRange(1, 1));
        
        // Case B: considering character is in  (4, 1)
        assertFalse(SlugA.isInEnemyBattleRange(4, 1)); // (4,1) not in range of (2, 1) as on edge of radius
        assertFalse(SlugA.isInEnemySupportRange(4, 1));
        assertTrue(SlugB.isInEnemyBattleRange(4, 1)); // (4,1) in range of (3, 1)
        assertTrue(SlugB.isInEnemySupportRange(4, 1));
        assertTrue(SlugC.isInEnemyBattleRange(4, 1)); // (4,1) in range of (4, 2)
        assertTrue(SlugC.isInEnemySupportRange(4, 1));
    }

    // Test item drops of slugs
    // The actual action of enemies dropping loot is done by LoopManiaWorldController.reactToEnemyDefeat
    // HOWEVER, that is a private function and cannot be called for testing.
    // In exchange this test only checks if the type of items that can drop are correct
    @Test
    public void testLootDrops() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Slug originSlug = new Slug(curPos);

        // Check the content of the lootDrop for the slug
        // The items that can be dropped should be the same with our assumptions 
        Map<String, Integer> lootDrops = originSlug.getLootDrops();
        assertEquals(lootDrops.size(), 7); // Can only drop 7 common items
        lootDrops.containsKey("Sword");
        lootDrops.containsKey("Staff");
        lootDrops.containsKey("Stake");
        lootDrops.containsKey("Armour");
        lootDrops.containsKey("Helmet");
        lootDrops.containsKey("Shield");
        lootDrops.containsKey("HealthPotion");
    }

    // total of three scenarios
    // Slug does not move, slug moves up the path and slug moves down the path
    @Test
    public void testMove() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Slug originSlug = new Slug(curPos);

        // Case 1: slug does not move
        for (int i = 0; i < 100; i++) {
            originSlug.move(null, null);
            if (originSlug.getX() == 0 && originSlug.getY() == 1) {
                assertEquals(originSlug.getX(), 0);
                assertEquals(originSlug.getY(), 1);
                break;
            }
        }

        // Case 2: slug move down the path
        for (int i = 0; i < 100; i++) {
            originSlug.move(null, null);
            if (originSlug.getX() == 0 && originSlug.getY() == 2) {
                assertEquals(originSlug.getX(), 0);
                assertEquals(originSlug.getY(), 2);
                break;
            }
        }

        // Case 3: slug moves up the path
        for (int i = 0; i < 100; i++) {
            originSlug.move(null, null);
            if (originSlug.getX() == 0 && originSlug.getY() == 1) {
                assertEquals(originSlug.getX(), 0);
                assertEquals(originSlug.getY(), 1);
                break;
            }
        }
    }

    // [Integration Testing]
    // Test the battleRadius and supportRadius of slugs correctly iniating battles
    // in a given LoopManiaWorld using runBattle
    /*
    @Test
    public void testRunBattleSlug() {
        // 4 x 4 path around a 5 x 5 world, start from (1,1)
        List<Pair<Integer, Integer>> orderedPath = createOrderedPath();

        // creates a 5 x 5 world with the 4 x 4 ordered path around it
        LoopManiaWorld newWorld = new LoopManiaWorld(5, 5, orderedPath);

        // adds slugs into the world
        // Slug A is located in (2, 1)
        PathPosition posA = new PathPosition(1, orderedPath);
        Slug SlugA = new Slug(posA);
        // Slug B is located in (2, 2)
        PathPosition posB = new PathPosition(2, orderedPath);
        Slug SlugB = new Slug(posB);
        /** 
         * Uncomment this after addEnemy is done in LoopManiaWorld
         * newWorld.addEnemy(SlugA);
         * newWorld.addEnemy(SlugB);
        

        // Character starts at (1,1) and should only battle slugA initially
        // runBattles
        List<Enemy> deadEnemies = newWorld.runBattles();
        // SlugA should die leaving only slug B in the world
        // Check if SlugA is added to dead enemies
        assertEquals(deadEnemies.size(), 1);
        assertEquals(deadEnemies.get(0).getX(), 2);
        assertEquals(deadEnemies.get(0).getY(), 1);
    }
    */
}
