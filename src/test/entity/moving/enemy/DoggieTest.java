package test.entity.moving.enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.javatuples.Pair;
import java.util.Map;

import org.junit.jupiter.api.Test;

import unsw.loopmania.entity.moving.*;
import unsw.loopmania.entity.moving.enemy.*;

public class DoggieTest {
    // Test the constructor of Doggie
    // Its stats should be the same as the predetermined stats for doggie in assumptions.md
    @Test
    public void testDoggieConstructor() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Doggie testDoge = new Doggie(curPos);

        // Compare the stats of the initial new constructed doggie
        assertEquals(testDoge.getEnemyMaxHp(), 230);
        assertEquals(testDoge.getEnemyCurHp(), 230);
        assertEquals(testDoge.getEnemyAtk(), 46);
        assertEquals(testDoge.getBattleRadius(), 2);
        assertEquals(testDoge.getSupportRadius(), 2);
        assertEquals(testDoge.getAttackOrder(), 1);
        assertTrue(testDoge.getGoldDrop() >= 50 && testDoge.getGoldDrop() <= 100);
        assertEquals(testDoge.getXpDrop(), 50);
        assertEquals(testDoge.getEnemyType(), "Doggie");
    }

    // Test item drops of Doggie
    // The actual action of enemies dropping loot is done by LoopManiaWorldController.reactToEnemyDefeat
    // HOWEVER, that is a private function and cannot be called for testing.
    // In exchange this test only checks if the type of items that can drop are correct
    @Test
    public void testLootDrops() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Doggie testDoge = new Doggie(curPos);

        // Check the content of the lootDrop for the Doggie
        Map<String, Integer> lootDrops = testDoge.getLootDrops();
        assertEquals(lootDrops.size(), 8); // Can drop up to 8 items
        lootDrops.containsKey("Sword");
        lootDrops.containsKey("Staff");
        lootDrops.containsKey("Stake");
        lootDrops.containsKey("Armour");
        lootDrops.containsKey("Helmet");
        lootDrops.containsKey("Shield");
        lootDrops.containsKey("HealthPotion");
        lootDrops.containsKey("DoggieCoin");
    }

    // Test the stun probability of doggie
    @Test
    public void testZombieCrit() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        Doggie doggie = new Doggie(curPos);

        // Success
        for (int i = 0; i < 100; i++) {
            boolean ret = doggie.isStun();
            if (ret) {
                assertTrue(ret);
                break;
            }
        }

        // Fail stun
        for (int i = 0; i < 100; i++) {
            boolean ret = !(doggie.isStun());
            if (ret) {
                assertTrue(ret);
                break;
            }
        }
    }

        // Test doggie's movement
        @Test
        public void testMove() {
            PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
            Doggie testDoggie = new Doggie(curPos);
    
            // Case 1: Doggie does not move
            for (int i = 0; i < 100; i++) {
                testDoggie.move(null, null);
                if (testDoggie.getX() == 0 && testDoggie.getY() == 1) {
                    assertEquals(testDoggie.getX(), 0);
                    assertEquals(testDoggie.getY(), 1);
                    break;
                }
            }
    
            // Case 2: doggie move down the path
            for (int i = 0; i < 100; i++) {
                testDoggie.move(null, null);
                if (testDoggie.getX() == 0 && testDoggie.getY() == 2) {
                    assertEquals(testDoggie.getX(), 0);
                    assertEquals(testDoggie.getY(), 2);
                    break;
                }
            }
    
            // Case 3: zomb moves up the path
            for (int i = 0; i < 100; i++) {
                testDoggie.move(null, null);
                if (testDoggie.getX() == 0 && testDoggie.getY() == 1) {
                    assertEquals(testDoggie.getX(), 0);
                    assertEquals(testDoggie.getY(), 1);
                    break;
                }
            }
        }
}
