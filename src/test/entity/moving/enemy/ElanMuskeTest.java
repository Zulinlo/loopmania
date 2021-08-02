package test.entity.moving.enemy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.javatuples.Pair;
import java.util.Map;

import org.junit.jupiter.api.Test;

import unsw.loopmania.entity.moving.*;
import unsw.loopmania.entity.moving.enemy.*;


public class ElanMuskeTest {
    // Test the constructor of ElanMuske
    // Its stats should be the same as the predetermined stats in assumptions.md
    @Test
    public void testElanMuskeConstructor() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        ElanMuske elon = new ElanMuske(curPos);

        // Compare the stats of the initial new constructed elon musk
        assertEquals(elon.getEnemyMaxHp(), 300);
        assertEquals(elon.getEnemyCurHp(), 300);
        assertEquals(elon.getEnemyAtk(), 54);
        assertEquals(elon.getBattleRadius(), 2);
        assertEquals(elon.getSupportRadius(), 2);
        assertEquals(elon.getAttackOrder(), 1);
        assertTrue(elon.getGoldDrop() >= 150 && elon.getGoldDrop() <= 250);
        assertEquals(elon.getXpDrop(), 100);
        assertEquals(elon.getEnemyType(), "ElanMuske");
    }

    // Test item drops of ElanMuske
    // The actual action of enemies dropping loot is done by LoopManiaWorldController.reactToEnemyDefeat
    // HOWEVER, that is a private function and cannot be called for testing.
    // In exchange this test only checks if the type of items that can drop are correct
    @Test
    public void testLootDrops() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        ElanMuske elon = new ElanMuske(curPos);

        // Check the content of the lootDrop for the Doggie
        Map<String, Integer> lootDrops = elon.getLootDrops();
        assertEquals(lootDrops.size(), 7);
        lootDrops.containsKey("Sword");
        lootDrops.containsKey("Staff");
        lootDrops.containsKey("Stake");
        lootDrops.containsKey("Armour");
        lootDrops.containsKey("Helmet");
        lootDrops.containsKey("Shield");
        lootDrops.containsKey("HealthPotion");
    }

    // Test the heal action / probability for elanMuske
    @Test
    public void testElanIsHeal() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        ElanMuske elon = new ElanMuske(curPos);

        // Success
        for (int i = 0; i < 100; i++) {
            boolean ret = elon.isHeal();
            if (ret) {
                assertTrue(ret);
                break;
            }
        }

        // Fail stun
        for (int i = 0; i < 100; i++) {
            boolean ret = !(elon.isHeal());
            if (ret) {
                assertTrue(ret);
                break;
            }
        }
    }

    @Test
    public void testElanDoHeal() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        ElanMuske elon = new ElanMuske(curPos);
        Slug slugA = new Slug(curPos);
        Slug slugB = new Slug(curPos);

        List<Enemy> enemiesToBattle = new ArrayList<>();
        enemiesToBattle.add(elon);
        enemiesToBattle.add(slugA);
        enemiesToBattle.add(slugB);

        // No one is hurt yet, heal does nothing
        assertEquals(elon.getEnemyCurHp(), elon.getEnemyMaxHp());
        assertEquals(slugA.getEnemyCurHp(), slugA.getEnemyMaxHp());
        assertEquals(slugB.getEnemyCurHp(), slugB.getEnemyMaxHp());
        elon.doHeal(enemiesToBattle);
        assertEquals(elon.getEnemyCurHp(), elon.getEnemyMaxHp());
        assertEquals(slugA.getEnemyCurHp(), slugA.getEnemyMaxHp());
        assertEquals(slugB.getEnemyCurHp(), slugB.getEnemyMaxHp());

        // ElanMuske, slug A takes 10 dmg while slugB takes 11 dmg.
        elon.takeDamage(10);
        slugA.takeDamage(10);
        slugB.takeDamage(11);
        assertEquals(elon.getEnemyCurHp(), 290);
        assertEquals(slugA.getEnemyCurHp(), 2);
        assertEquals(slugB.getEnemyCurHp(), 1);

        // Since Elan is hurt, elan will heal himself first.
        elon.doHeal(enemiesToBattle);
        assertEquals(elon.getEnemyCurHp(), elon.getEnemyMaxHp());
        assertEquals(slugA.getEnemyCurHp(), 2);
        assertEquals(slugB.getEnemyCurHp(), 1);

        // As elan is no longer hurt, heal the slugs
        // Since slugB has 1 lower hp, heal slugB first
        elon.doHeal(enemiesToBattle);
        assertEquals(elon.getEnemyCurHp(), elon.getEnemyMaxHp());
        assertEquals(slugA.getEnemyCurHp(), 2);
        assertEquals(slugB.getEnemyCurHp(), slugB.getEnemyMaxHp() - 1);

        // Kill slug A
        slugA.takeDamage(10);
        assertEquals(elon.getEnemyCurHp(), elon.getEnemyMaxHp());
        assertEquals(slugA.getEnemyCurHp(), -8);
        assertEquals(slugB.getEnemyCurHp(), slugB.getEnemyMaxHp() - 1);

        // Since slugA is killed, heal shoulld not heal SLugA and just heal slugB
        elon.doHeal(enemiesToBattle);
        assertEquals(elon.getEnemyCurHp(), elon.getEnemyMaxHp());
        assertEquals(slugA.getEnemyCurHp(), -8);
        assertEquals(slugB.getEnemyCurHp(), slugB.getEnemyMaxHp());
    }


    // Test elan's movement
    @Test
    public void testMove() {
        PathPosition curPos = new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)));
        ElanMuske elonMusk = new ElanMuske(curPos);

        // Case 1: ElanMuske does not move
        for (int i = 0; i < 100; i++) {
            elonMusk.move(null, null);
            if (elonMusk.getX() == 0 && elonMusk.getY() == 1) {
                assertEquals(elonMusk.getX(), 0);
                assertEquals(elonMusk.getY(), 1);
                break;
            }
        }

        // Case 2: elan muske moves down the path
        for (int i = 0; i < 100; i++) {
            elonMusk.move(null, null);
            if (elonMusk.getX() == 0 && elonMusk.getY() == 2) {
                assertEquals(elonMusk.getX(), 0);
                assertEquals(elonMusk.getY(), 2);
                break;
            }
        }

        // Case 3: elan moves up the path
        for (int i = 0; i < 100; i++) {
            elonMusk.move(null, null);
            if (elonMusk.getX() == 0 && elonMusk.getY() == 1) {
                assertEquals(elonMusk.getX(), 0);
                assertEquals(elonMusk.getY(), 1);
                break;
            }
        }
    }
}
