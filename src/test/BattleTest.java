package test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import test.testHelpers.CreateGoal;

import org.javatuples.Pair;

import unsw.loopmania.Battle;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.entity.moving.PathPosition;
import unsw.loopmania.entity.moving.enemy.*;
import unsw.loopmania.goal.Goal;
import java.util.Arrays;

public class BattleTest {
    Goal goal = (new CreateGoal()).DummyGoal();

    // Testing for Vampire Building loading properly
    @Test
    public void HittingSlugUnarmedTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("VampireCastle");
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Battle battle = new Battle(new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))), enemies, false, 0);

        battle.setTargetEnemy(battle.getEnemies().get(0));
        battle.characterAction("Slap");
        
        Enemy e = battle.getEnemies().get(0);
        assertEquals(8, battle.getEnemies().get(0).getEnemyCurHp());

        for (int i = 0; i <=100 ; i++) {
            try {
                battle.characterAction("Punch");
            } catch (IndexOutOfBoundsException q) {
                break;
            }
        }
        assertTrue(0 > e.getEnemyCurHp());

        assertTrue(e.equals(battle.endBattle().get(0)));
    }

    @Test
    public void HittingSlugandZombiesSwordTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("VampireCastle");
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Sword", false);
        Battle battle = new Battle(character, enemies, false, 0);

        battle.setTargetEnemy(battle.getEnemies().get(0));
        
        battle.characterAction("Slash");
        
        Enemy e = battle.getEnemies().get(0);
        assertEquals(4, battle.getEnemies().get(0).getEnemyCurHp());

        for (int i = 0; i <=100 ; i++) {
            try {
                battle.characterAction("Strike");
            } catch (IndexOutOfBoundsException q) {
                break;
            }
        }
        assertTrue(0 > e.getEnemyCurHp());

        enemies = new ArrayList<>();
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Zombie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Zombie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        battle = new Battle(character, enemies, false, 0);

        battle.setTargetEnemy(battle.getEnemies().get(0));
        
        assertTrue(!battle.hasBattleEnded());
        
        e = battle.getEnemies().get(0);
        Enemy e1 = battle.getEnemies().get(1);
        Enemy e2 = battle.getEnemies().get(2);
        Enemy e3 = battle.getEnemies().get(3);
        Enemy e4 = battle.getEnemies().get(4);
        Enemy e5 = battle.getEnemies().get(5);

        for (int i = 0; i <= 500 ; i++) {
            try {
                battle.characterAction("Heavy Slash");
            } catch (IndexOutOfBoundsException q) {
                break;
            }
        }
        assertTrue(0 > e.getEnemyCurHp());
        assertTrue(0 > e1.getEnemyCurHp());
        assertTrue(0 > e2.getEnemyCurHp());
        assertTrue(0 > e3.getEnemyCurHp());
        assertTrue(0 > e4.getEnemyCurHp());
        assertTrue(0 > e5.getEnemyCurHp());

        assertTrue(battle.hasBattleEnded());
    }

    @Test
    public void HittingSlugandZombiesStaffTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("VampireCastle");
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Staff", false);
        Battle battle = new Battle(character, enemies, false, 0);

        battle.setTargetEnemy(battle.getEnemies().get(0));
        
        battle.characterAction("Jab");
        
        Enemy e = battle.getEnemies().get(0);
        assertEquals(8, battle.getEnemies().get(0).getEnemyCurHp());

        battle.characterAction("Trance");
        
        assertEquals(4, battle.getPP("Trance"));

        for (int i = 0; i <=100 ; i++) {
            try {
                battle.characterAction("Trance");
            } catch (IndexOutOfBoundsException q) {
                break;
            }
        }
        assertTrue(0 > e.getEnemyCurHp());

        enemies = new ArrayList<>();
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        battle = new Battle(character, enemies, false, 0);

        battle.setTargetEnemy(battle.getEnemies().get(0));
        
        
        e = battle.getEnemies().get(0);

        for (int i = 0; i <=500 ; i++) {
            try {
                battle.characterAction("Zap");
            } catch (IndexOutOfBoundsException q) {
                break;
            }
        }
        assertTrue(0 > e.getEnemyCurHp());
    }

    @Test
    public void HittingSlugandZombiesStakeTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("VampireCastle");
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Stake", false);
        Battle battle = new Battle(character, enemies, false, 0);

        battle.setTargetEnemy(battle.getEnemies().get(0));
        
        battle.characterAction("Stab");
        
        Enemy e = battle.getEnemies().get(0);
        assertEquals(7, battle.getEnemies().get(0).getEnemyCurHp());

        battle.characterAction("Garlic Stab");
        
        assertEquals(6, battle.getPP("Garlic Stab"));

        for (int i = 0; i <=100 ; i++) {
            try {
                battle.characterAction("Garlic Stab");
            } catch (IndexOutOfBoundsException q) {
                break;
            }
        }
        assertTrue(0 > e.getEnemyCurHp());
    }

    @Test
    public void HittingAndurilBossesTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("VampireCastle");
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Vampire(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new ElanMuske(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Doggie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));

        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Anduril", false);
        Battle battle = new Battle(character, enemies, false, 0);
        Enemy e = battle.getEnemies().get(0);
        battle.setTargetEnemy(battle.getEnemies().get(0));
        
        battle.characterAction("Swing");
        
        assertEquals(87, e.getEnemyCurHp());
        battle.setTargetEnemy(battle.getEnemies().get(1));
        for (int i = 0; i <=100 ; i++) {
            battle.characterAction("Space Ex");
            if (battle.getEnemies().get(1).getEnemyCurHp() != battle.getEnemies().get(1).getEnemyMaxHp()) break;
        }
        assertEquals(267, battle.getEnemies().get(1).getEnemyCurHp());
        
        battle.setTargetEnemy(battle.getEnemies().get(1));
        for (int i = 0; i <=100 ; i++) {
            battle.characterAction("Bad Boy");
            if (battle.getEnemies().get(1).getEnemyCurHp() != 267) break;
        }
        assertEquals(253, battle.getEnemies().get(1).getEnemyCurHp());

        assertEquals(87, e.getEnemyCurHp());
        battle.setTargetEnemy(battle.getEnemies().get(2));
        for (int i = 0; i <=100 ; i++) {
            battle.characterAction("Space Ex");
            if (battle.getEnemies().get(2).getEnemyCurHp() != battle.getEnemies().get(2).getEnemyMaxHp()) break;
        }
        assertEquals(216, battle.getEnemies().get(2).getEnemyCurHp());
        
        for (int i = 0; i <=100 ; i++) {
            battle.characterAction("Bad Boy");
            if (battle.getEnemies().get(2).getEnemyCurHp() != 216) break;
        }
        assertEquals(183, battle.getEnemies().get(2).getEnemyCurHp());
    }

    @Test
    public void TranceBossesTest(){
        // Make sure bosses cannot be tranced
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new ElanMuske(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Doggie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));

        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Staff", false);
        Battle battle = new Battle(character, enemies, false, 0);

        Enemy Elan = battle.getEnemies().get(0);
        Enemy Doggie = battle.getEnemies().get(1);

        // Trance ElanMuske boss type 1000x to guarantee it happening at least once 
        battle.setTargetEnemy(Elan);
        
        boolean hasElanBeenTranced = false;
        for (int i = 0; i < 1000 ; i++) {
            battle.characterAction("Trance");
            
            if (Elan.isTranced()) {
                hasElanBeenTranced = true;
            }
        }

        assertFalse(hasElanBeenTranced);

        // Trance ElanMuske boss type 1000x to guarantee it happening at least once 
        battle.setTargetEnemy(Doggie);
        
        boolean hasDoggieBeenTranced = false;
        for (int i = 0; i < 1000 ; i++) {
            battle.characterAction("Trance");
            
            if (Doggie.isTranced()) {
                hasDoggieBeenTranced = true;
            }
        }

        assertFalse(hasDoggieBeenTranced);
    }

    @Test
    public void BuildingEffects(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("VampireCastle");
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Stake", false);
        Battle battle = new Battle(character, enemies, true, 0);

        battle.setTargetEnemy(battle.getEnemies().get(0));
        
        battle.characterAction("Stab");
        assertEquals(2, battle.getEnemies().get(0).getEnemyCurHp());

        enemies = new ArrayList<>();
        enemies.add(new Zombie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        battle = new Battle(character, enemies, false, 2);

        battle.setTargetEnemy(battle.getEnemies().get(0));
        
        battle.characterAction("Stab");
        assertEquals(9, battle.getEnemies().get(0).getEnemyCurHp());
    }

    @Test
    public void CompoundRareItemTest() {
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        // Set to confusing mode then equip compound rare item
        world.setMode("Confusing");
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Vampire(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new ElanMuske(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Doggie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));

        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Anduril", true);
        Battle battle = new Battle(character, enemies, false, 0);
        Enemy e = battle.getEnemies().get(0);
        battle.setTargetEnemy(battle.getEnemies().get(0));
        
        battle.characterAction("Swing");
        
        assertEquals(87, e.getEnemyCurHp());
        battle.setTargetEnemy(battle.getEnemies().get(1));
        for (int i = 0; i <=100 ; i++) {
            battle.characterAction("Space Ex");
            if (battle.getEnemies().get(1).getEnemyCurHp() != battle.getEnemies().get(1).getEnemyMaxHp()) break;
        }
        assertEquals(267, battle.getEnemies().get(1).getEnemyCurHp());
        
        battle.setTargetEnemy(battle.getEnemies().get(1));
        for (int i = 0; i <=100 ; i++) {
            battle.characterAction("Bad Boy");
            if (battle.getEnemies().get(1).getEnemyCurHp() != 267) break;
        }
        assertEquals(253, battle.getEnemies().get(1).getEnemyCurHp());

        assertEquals(87, e.getEnemyCurHp());
        battle.setTargetEnemy(battle.getEnemies().get(2));
        for (int i = 0; i <=100 ; i++) {
            battle.characterAction("Space Ex");
            if (battle.getEnemies().get(2).getEnemyCurHp() != battle.getEnemies().get(2).getEnemyMaxHp()) break;
        }
        assertEquals(216, battle.getEnemies().get(2).getEnemyCurHp());
        
        for (int i = 0; i <=100 ; i++) {
            battle.characterAction("Bad Boy");
            if (battle.getEnemies().get(2).getEnemyCurHp() != 216) break;
        }
        assertEquals(183, battle.getEnemies().get(2).getEnemyCurHp());
    }

    @Test
    public void CharGotStunned() {
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        Enemy doggie = new Doggie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        enemies.add(doggie);

        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        Battle battle = new Battle(character, enemies, false, 0);
        battle.setTargetEnemy(battle.getEnemies().get(0));
        

        // Make doggie hit character 1000 times to basically guarantee stun to occur at least once
        boolean hasBeenStunned = false;
        for (int i = 0; i < 1000 ; i++) {
            battle.enemyAction();
            character.addHealth(1000);
            int currDoggieHp = (int) doggie.getEnemyCurHp();
            battle.characterAction("Slap");
            
            if (currDoggieHp == (int) doggie.getEnemyCurHp()) {
                hasBeenStunned = true;
            }

            doggie.setEnemyCurHp(1000);
        }

        assertTrue(hasBeenStunned);
    }

    @Test
    public void EnemyAction(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("VampireCastle");
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Zombie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Vampire(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Doggie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new ElanMuske(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Stake", false);
        Battle battle = new Battle(character, enemies, true, 0);

        battle.enemyAction();
        assertTrue(-30 >= character.getCurHp());

        enemies = new ArrayList<>();
        enemies.add(new ElanMuske(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Zombie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        battle = new Battle(character, enemies, true, 0);
        battle.setTargetEnemy(battle.getEnemies().get(0));
        battle.characterAction("Slap");
        battle.setTargetEnemy(battle.getEnemies().get(1));
        battle.characterAction("Slap");
        while (true) {
            battle.enemyAction();
            if (battle.getEnemies().get(0).getEnemyCurHp() == battle.getEnemies().get(0).getEnemyMaxHp()) break;
        }
        assertTrue(battle.getEnemies().get(1).getEnemyCurHp() == 16);
        battle.characterAction("Slap");
        while (true) {
            battle.enemyAction();
            if (battle.getEnemies().get(1).getEnemyCurHp() != 8) break;
        }

        assertEquals(18, battle.getEnemies().get(1).getEnemyCurHp());
    }

    @Test
    public void InvalidCharacterActionTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Battle battle = new Battle(new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))), enemies, false, 0);

        battle.setTargetEnemy(battle.getEnemies().get(0));

        assertThrows(NullPointerException.class, () -> {
            battle.characterAction("InvalidMove");
        });
    }

    @Test
    public void TranceDamageTaken(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Zombie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Staff", false);
        character.addAlliedSoldier();
        character.addAlliedSoldier();
        character.addAlliedSoldier();
        character.addAlliedSoldier();
        character.addAlliedSoldier();
        Battle battle = new Battle(character, enemies, true, 0);

        battle.enemyAction();
        assertEquals(100, character.getCurHp());

        assertTrue(battle.actionHasPP("Trance"));

        battle.setTargetEnemy(battle.getEnemies().get(1));
        for (int i = 0; i < 500 ; i++) {
            battle.characterAction("Trance");
        }

        assertTrue(!battle.actionHasPP("Trance"));

        battle.enemyAction();
        assertEquals(100, character.getCurHp());
        Enemy e = battle.getEnemies().get(0);
        battle.setTargetEnemy(battle.getEnemies().get(0));
        battle.characterAction("Jab");
        assertTrue(2 > e.getEnemyCurHp());
    }

    @Test
    public void TrancedAllyDie(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Vampire(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Zombie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Zombie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Staff", false);
        character.addAlliedSoldier();
        character.addAlliedSoldier();
        Battle battle = new Battle(character, enemies, true, 0);

        battle.enemyAction();
        assertEquals(100, character.getCurHp());

        // Trance the 2 zombies
        Enemy zombie1 = battle.getEnemies().get(1);
        Enemy zombie2 = battle.getEnemies().get(2);

        battle.setTargetEnemy(zombie1);
        for (int i = 0; i < 1000 ; i++) {
            battle.characterAction("Trance");
            if (zombie1.isTranced()) {
                battle.characterAction("Trance");

                if (zombie2.isTranced()) {
                    // Enemy vampire kills everything including allied soldiers
                    for (int j = 0; j < 500; j++) {
                        battle.enemyAction();
                    }

                    assertTrue(battle.getEnemies().size() == 1);
                }
            }
        }
    }

    @Test
    public void EnemiesReturnsFromTranced(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Slug(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Staff", false);
        character.addAlliedSoldier();
        character.addAlliedSoldier();
        Battle battle = new Battle(character, enemies, true, 0);

        battle.enemyAction();
        assertEquals(100, character.getCurHp());

        // Trance the 2 slugs and make sure they are no longer tranced after 3 update trance calls (3 turns)
        Enemy slug1 = battle.getEnemies().get(1);
        Enemy slug2 = battle.getEnemies().get(2);

        battle.setTargetEnemy(slug1);
        for (int i = 0; i < 1000 ; i++) {
            battle.characterAction("Trance");

            if (slug1.isTranced()) {
                battle.setTargetEnemy(slug2);
                battle.characterAction("Trance");

                if (slug2.isTranced()) {
                    break;
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            battle.enemyAction();
        }

        assertTrue(battle.getEnemies().size() == 3);
    }

    @Test
    public void LastTrancedEnemyKilled(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Vampire(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Staff", false);
        character.addAlliedSoldier();
        character.addAlliedSoldier();
        Battle battle = new Battle(character, enemies, true, 0);

        // Trance the Vampire
        Enemy vampire = battle.getEnemies().get(0);

        battle.setTargetEnemy(vampire);
        for (int i = 0; i < 1000 ; i++) {
            battle.characterAction("Trance");
            if (vampire.isTranced()) {
                break;
            }
        }

        assertTrue(battle.hasBattleEnded());
        assertTrue(battle.endBattle().get(0).equals(vampire));
    }

    @Test
    public void TrancedAllyTransformedByZombie(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        world.newBattle();

        List<Enemy> enemies = new ArrayList<>();
        enemies.add(new Zombie(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        enemies.add(new Vampire(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        Character character = new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2))));
        character.equipItemByType("Staff", false);
        Battle battle = new Battle(character, enemies, true, 0);

        // Trance vampires
        Enemy vampire2 = battle.getEnemies().get(1);

        for (int i = 0; i < 1000 ; i++) {
            battle.setTargetEnemy(vampire2);
            battle.characterAction("Trance");

            // At least 1 tranced
            if (vampire2.isTranced()) {
                for (int j = 0; j < 10; j++) {
                    // zombie will transform at least one from biting 1000x
                    battle.enemyAction();
                    if (battle.getEnemies().size() == 2) {
                        // At least 1 vampire was deleted and transformed into a zombie (original size of enemies is 10)
                        assertTrue(battle.getEnemies().size() == 2);
                        return;
                    }
                }
            }
        }
    }
}
