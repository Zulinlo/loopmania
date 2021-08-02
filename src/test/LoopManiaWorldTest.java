package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;
import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.CreateGoal;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.entity.moving.PathPosition;
import unsw.loopmania.entity.moving.enemy.Enemy;
import unsw.loopmania.entity.notmoving.building.HerosCastle;
import unsw.loopmania.entity.notmoving.item.Sword;
import unsw.loopmania.goal.Goal;
import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.entity.notmoving.building.*;
import unsw.loopmania.entity.notmoving.card.*;

public class LoopManiaWorldTest {
    Goal goal = (new CreateGoal()).DummyGoal();
    
    @Test
    public void TestRunBattles() {
        /**
        * Making sure that spawning enemies works
        */
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));
        orderedPath.add(new Pair<Integer, Integer>(3,0));
        orderedPath.add(new Pair<Integer, Integer>(4,0));
        orderedPath.add(new Pair<Integer, Integer>(5,0));
        orderedPath.add(new Pair<Integer, Integer>(5,1));
        orderedPath.add(new Pair<Integer, Integer>(5,2));
        orderedPath.add(new Pair<Integer, Integer>(5,3));
        
        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        Character character = new Character(new PathPosition(0, orderedPath));
        
        world.setCharacter(character);
        
        world.loadCard("ZombiePit");
        world.convertCardToBuildingByCoordinates(0, 0, 1, 2);
        
        world.loadCard("Tower");
        world.convertCardToBuildingByCoordinates(0, 0, 1, 1);
        
        world.possiblySpawnEnemies();
        
        assertTrue(world.newBattle());
    }
    
    @Test
    public void TestSpawnEnemies() {
        /**
        * Making sure that spawning enemies works
        */
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));
        orderedPath.add(new Pair<Integer, Integer>(3,0));
        orderedPath.add(new Pair<Integer, Integer>(4,0));
        orderedPath.add(new Pair<Integer, Integer>(5,0));
        orderedPath.add(new Pair<Integer, Integer>(5,1));
        orderedPath.add(new Pair<Integer, Integer>(5,2));
        orderedPath.add(new Pair<Integer, Integer>(5,3));
        
        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        Character character = new Character(new PathPosition(0, orderedPath));
        
        world.setCharacter(character);
        assertTrue(world.possiblySpawnEnemies().size() > 3);
        
        for (int i = 0; i <30; i++) {
        world.possiblySpawnEnemies().size();
        }
        assertTrue(world.possiblySpawnEnemies().size() == 0);
    }
    
    @Test
    public void TestSpawnGold() {
        /**
        * Testing to see the spawn behaviours of gold are working
        */
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));
        orderedPath.add(new Pair<Integer, Integer>(3,0));
        orderedPath.add(new Pair<Integer, Integer>(4,0));
        orderedPath.add(new Pair<Integer, Integer>(5,0));
        orderedPath.add(new Pair<Integer, Integer>(5,1));
        orderedPath.add(new Pair<Integer, Integer>(5,2));
        orderedPath.add(new Pair<Integer, Integer>(5,3));
        
        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setHerosCastle(new HerosCastle(new SimpleIntegerProperty(0), new
        SimpleIntegerProperty(0)));
        
        world.setCharacter(character);
        assertTrue(world.spawnGoldDropOnGround().getGoldAmount() >= 5);
        for (int i = 0; i <30; i++) {
        world.spawnGoldDropOnGround();
        }
        
        // If its full dont spawn any
        assertTrue(world.spawnGoldDropOnGround() == null);
    }
    
    @Test
    public void TestSpawnHealthPots() {
        /**
        * Testing to see the spawn behaviours of HP Pots are working
        */
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));
        orderedPath.add(new Pair<Integer, Integer>(3,0));
        orderedPath.add(new Pair<Integer, Integer>(4,0));
        orderedPath.add(new Pair<Integer, Integer>(5,0));
        orderedPath.add(new Pair<Integer, Integer>(5,1));
        orderedPath.add(new Pair<Integer, Integer>(5,2));
        orderedPath.add(new Pair<Integer, Integer>(5,3));
        
        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setHerosCastle(new HerosCastle(new SimpleIntegerProperty(0), new
        SimpleIntegerProperty(0)));
        
        world.setCharacter(character);
        assertTrue(world.spawnHealthPotionDropOnGround() != null);
        for (int i = 0; i <30; i++) {
        world.spawnHealthPotionDropOnGround();
        }
        
        // If its full dont spawn any
        assertTrue(world.spawnHealthPotionDropOnGround() == null);
    }
    
    @Test
    public void Overfillingcards() {
        /**
        * Testing to see if it is full it would delete oldest one and shift down
        */
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));
        orderedPath.add(new Pair<Integer, Integer>(3,0));
        orderedPath.add(new Pair<Integer, Integer>(4,0));
        orderedPath.add(new Pair<Integer, Integer>(5,0));
        orderedPath.add(new Pair<Integer, Integer>(5,1));
        orderedPath.add(new Pair<Integer, Integer>(5,2));
        orderedPath.add(new Pair<Integer, Integer>(5,3));
        
        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setHerosCastle(new HerosCastle(new SimpleIntegerProperty(0), new
        SimpleIntegerProperty(0)));
        
        world.setCharacter(character);
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("Tower");
        world.loadCard("Trap");
        world.loadCard("Trap");
        world.loadCard("Trap");
        world.loadCard("Trap");
        world.loadCard("Trap");
        world.loadCard("Trap");
        world.loadCard("Trap");
        
        assertEquals("Tower", world.getCardEntities().get(0).getType());
        assertEquals("Trap", world.getCardEntities().get(1).getType());
        assertEquals("Trap", world.getCardEntities().get(2).getType());
        assertEquals("Trap", world.getCardEntities().get(3).getType());
        assertEquals("Trap", world.getCardEntities().get(4).getType());
        assertEquals("Trap", world.getCardEntities().get(5).getType());
        assertEquals("Trap", world.getCardEntities().get(6).getType());
        assertEquals("Trap", world.getCardEntities().get(7).getType());
    }
    
    @Test
    public void Reset() {
        /**
        * Resetting
        */
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));
        orderedPath.add(new Pair<Integer, Integer>(3,0));
        orderedPath.add(new Pair<Integer, Integer>(4,0));
        orderedPath.add(new Pair<Integer, Integer>(5,0));
        orderedPath.add(new Pair<Integer, Integer>(5,1));
        orderedPath.add(new Pair<Integer, Integer>(5,2));
        orderedPath.add(new Pair<Integer, Integer>(5,3));
        
        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setHerosCastle(new HerosCastle(new SimpleIntegerProperty(0), new
        SimpleIntegerProperty(0)));
        character.moveDownPath();
        world.setCharacter(character);
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        world.loadCard("Tower");
        world.loadCard("Trap");
        world.loadCard("Trap");
        world.loadCard("Trap");
        world.loadCard("Trap");
        world.loadCard("Trap");
        world.loadCard("Trap");
        world.loadCard("Trap");
        
        assertEquals("Tower", world.getCardEntities().get(0).getType());
        assertEquals("Trap", world.getCardEntities().get(1).getType());
        assertEquals("Trap", world.getCardEntities().get(2).getType());
        assertEquals("Trap", world.getCardEntities().get(3).getType());
        assertEquals("Trap", world.getCardEntities().get(4).getType());
        assertEquals("Trap", world.getCardEntities().get(5).getType());
        assertEquals("Trap", world.getCardEntities().get(6).getType());
        assertEquals("Trap", world.getCardEntities().get(7).getType());
        
        world.possiblySpawnEnemies();
        world.convertCardToBuildingByCoordinates(0, 0, 0, 1);
        world.spawnGoldDropOnGround();
        world.spawnHealthPotionDropOnGround();
        world.addEntity(new Sword(new SimpleIntegerProperty(0), new
        SimpleIntegerProperty(0)));
        
        world.reset();
        
        assertEquals(new ArrayList<>(), world.getCardEntities());
        assertEquals(new ArrayList<>(), world.getGoldDrops());
        assertEquals(0, world.getCycle());
        assertEquals(new ArrayList<>(), world.getHealthPotionDrops());
        assertEquals(new ArrayList<>(), world.getUnequippedItems());
    
    }
    
    @Test
    public void RunTick() {
        /**
        * Resetting
        */
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));
        orderedPath.add(new Pair<Integer, Integer>(3,0));
        orderedPath.add(new Pair<Integer, Integer>(4,0));
        orderedPath.add(new Pair<Integer, Integer>(5,0));
        orderedPath.add(new Pair<Integer, Integer>(5,1));
        orderedPath.add(new Pair<Integer, Integer>(5,2));
        orderedPath.add(new Pair<Integer, Integer>(5,3));
        
        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        
        Character character = new Character(new PathPosition(0, orderedPath));
        world.setHerosCastle(new HerosCastle(new SimpleIntegerProperty(0), new
        SimpleIntegerProperty(0)));
        character.moveDownPath();
        world.setCharacter(character);
        world.loadCard("VampireCastle");
        world.loadCard("VampireCastle");
        
        
        world.possiblySpawnEnemies();
        world.convertCardToBuildingByCoordinates(0, 0, 0, 1);
        world.spawnGoldDropOnGround();
        world.spawnHealthPotionDropOnGround();
        world.addEntity(new Sword(new SimpleIntegerProperty(0), new
        SimpleIntegerProperty(0)));
        
        world.runTickMoves();
        assertEquals(2, character.getX());
        assertEquals(0, character.getY());
    }

    @Test
    public void testImpossibleSpawn() {
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>(Arrays.asList(new Pair<Integer, Integer>(0,0)));
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath, null);
        Character character = new Character(new PathPosition(0, orderedPath));
        HerosCastle herosCastle = new HerosCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.setCharacter(character);
        world.setHerosCastle(herosCastle);

        List<Enemy> enemySpawned = world.possiblySpawnEnemies();
        // Since there is only one tile and tile is occupied by character, nothing should spawn in the world
        assertEquals(enemySpawned.size(), 0);
        assertEquals(world.getCycle(), 0);
    }

    @Test
    public void testSlugSpawn() {
        // Create a path of 5 horizontal tiles
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>(Arrays.asList(
                new Pair<Integer, Integer>(0,0), new Pair<Integer, Integer>(1,0),
                new Pair<Integer, Integer>(2,0), new Pair<Integer, Integer>(3,0),
                new Pair<Integer, Integer>(4,0)
            )
        );
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath, null);
        // Place character and heros castle at (0,0).
        Character character = new Character(new PathPosition(0, orderedPath));
        HerosCastle herosCastle = new HerosCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.setCharacter(character);
        world.setHerosCastle(herosCastle);

        List<Enemy> enemySpawned = world.possiblySpawnEnemies();
        // Currently all tiles are 2 tiles away from the character so no slugs should spawn
        assertEquals(enemySpawned.size(), 0);

        // MORE TESTS //
        // Add more path tiles
        orderedPath.add(new Pair<Integer, Integer>(5,0));
        orderedPath.add(new Pair<Integer, Integer>(6,0));
        LoopManiaWorld world2 = new LoopManiaWorld(10, 14, orderedPath, null);
        // Place character and heros castle at (0,0).
        world2.setCharacter(character);
        world2.setHerosCastle(herosCastle);
        
        // World 2 should have tiles that are valid spots for slugs to spawn
        enemySpawned = world2.possiblySpawnEnemies();
        assertTrue(enemySpawned.size() > 0);
        
        for (Enemy e : enemySpawned) {
            // All enemy that can possibly spawn initially must be a slug
            // Since no buildings and conditions are not met to spawn bosses.
            assertEquals("Slug", e.getEnemyType());
        }
        assertEquals(enemySpawned.size(), 4); // Only 4 slugs should spawn
    }

    @Test
    public void testDoggieSpawn() {
        // Create a path of 7 horizontal tiles
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>(Arrays.asList(
                new Pair<Integer, Integer>(0,0), new Pair<Integer, Integer>(1,0),
                new Pair<Integer, Integer>(2,0), new Pair<Integer, Integer>(3,0),
                new Pair<Integer, Integer>(4,0), new Pair<Integer, Integer>(5,0),
                new Pair<Integer, Integer>(6,0)
            )
        );
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath, null);
        // Place character and heros castle at (0,0).
        Character character = new Character(new PathPosition(0, orderedPath));
        HerosCastle herosCastle = new HerosCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.setCharacter(character);
        world.setHerosCastle(herosCastle);

        // RN its the 0th cycle meaning doggie cannot spawn
        // ONly slugs fill up the playing field
        List<Enemy> enemySpawned = world.possiblySpawnEnemies();
        assertEquals(enemySpawned.size(), 4);
        for (Enemy e : enemySpawned) {
            assertEquals("Slug", e.getEnemyType());
        }
        assertEquals(world.getCycle(), 0);

        // Set cycle in the world to 10
        // Doggie spawn conditions are met BUT all tiles are 3 tiles away from char. So invalid spawn.
        world.setCycle(10);
        enemySpawned = world.possiblySpawnEnemies();
        assertEquals(enemySpawned.size(), 0); // Since there are already 4 slugs, spawn nothing in the world
        assertEquals(world.getCycle(), 10);

        //-----//
        // Add a tile so that doggie can spawn and set cycle to 10
        orderedPath.add(new Pair<Integer, Integer>(8,0));
        LoopManiaWorld world2 = new LoopManiaWorld(10, 14, orderedPath, null);
        // Place character and heros castle at (0,0).
        world2.setCharacter(character);
        world2.setHerosCastle(herosCastle);
        world2.setCycle(10);

        enemySpawned = world2.possiblySpawnEnemies();
        assertTrue(enemySpawned.size() > 0);
        assertEquals(enemySpawned.size(), 5);
        assertEquals(world2.getCycle(), 10);

        // Count enemy types if they are correct
        // 4 slugs + 1 doggie
        int slugsCount = 0;
        int doggieCount = 0;
        for (Enemy e : enemySpawned) {
            if (e.getEnemyType().equals("Slug")) slugsCount++;
            if (e.getEnemyType().equals("Doggie")) doggieCount++;
        }
        assertEquals(slugsCount, 4);
        assertEquals(doggieCount, 1);
    }

    @Test
    public void testElanMuskeSpawn() {
        // Create a path of 11 horizontal tiles
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>(Arrays.asList(
                new Pair<Integer, Integer>(0,0), new Pair<Integer, Integer>(1,0),
                new Pair<Integer, Integer>(2,0), new Pair<Integer, Integer>(3,0),
                new Pair<Integer, Integer>(4,0), new Pair<Integer, Integer>(5,0),
                new Pair<Integer, Integer>(6,0), new Pair<Integer, Integer>(7,0),
                new Pair<Integer, Integer>(8,0), new Pair<Integer, Integer>(8,1),
                new Pair<Integer, Integer>(9,0)
            )
        );
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath, null);
        // Place character and heros castle at (0,0).
        Character character = new Character(new PathPosition(0, orderedPath));
        HerosCastle herosCastle = new HerosCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        character.setExp(500);
        world.setCharacter(character);
        world.setHerosCastle(herosCastle);

        // RN its the 0th cycle meaning elan cannot spawn
        // ONly slugs fill up the playing field
        List<Enemy> enemySpawned = world.possiblySpawnEnemies();
        assertEquals(enemySpawned.size(), 4);
        for (Enemy e : enemySpawned) {
            assertEquals("Slug", e.getEnemyType());
        }
        assertEquals(world.getCycle(), 0);

        // Set cycle in the world to 15
        // Elan spawn conditions are met BUT all tiles are 5 tiles away from char. So invalid spawn.
        world.setCycle(15);
        enemySpawned = world.possiblySpawnEnemies();
        assertEquals(enemySpawned.size(), 0); // Since there are already 4 slugs, spawn nothing in the world
        assertEquals(world.getCycle(), 15);

        
        //-----//
        // Add a tile so that elancan spawn and set cycle to 15
        orderedPath.add(new Pair<Integer, Integer>(9,1));
        LoopManiaWorld world2 = new LoopManiaWorld(10, 14, orderedPath, null);
        // Place character and heros castle at (0,0).
        world2.setCharacter(character);
        world2.setHerosCastle(herosCastle);
        world2.setCycle(15);

        enemySpawned = world2.possiblySpawnEnemies();
        assertTrue(enemySpawned.size() > 0);
        assertEquals(enemySpawned.size(), 5);
        assertEquals(world2.getCycle(), 15);

        // Count enemy types if they are correct
        // 4 slugs + 1 elanMusk
        int slugsCount = 0;
        int elon = 0;
        for (Enemy e : enemySpawned) {
            if (e.getEnemyType().equals("Slug")) slugsCount++;
            if (e.getEnemyType().equals("ElanMuske")) elon++;
        }
        assertEquals(slugsCount, 4);
        assertEquals(elon, 1);
    }

    @Test
    public void testVampireSpawnFromVampireBuildings() {
        // Create a path of 2 horizontal tiles
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>(Arrays.asList(
                new Pair<Integer, Integer>(0,0), new Pair<Integer, Integer>(1,0)
            )
        );
        LoopManiaWorld world = new LoopManiaWorld(8, 14, orderedPath, null);
        // Place character and heros castle at (0,0).
        Character character = new Character(new PathPosition(0, orderedPath));
        HerosCastle herosCastle = new HerosCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        world.setCharacter(character);
        world.setHerosCastle(herosCastle);

        // Add vampire castle to the world
        Card card = world.loadCard("VampireCastle");
        assertEquals(card.getType(), "VampireCastle");
        Building castle = world.convertCardToBuildingByCoordinates(0, 0, 1, 2);
        assertEquals(castle.getType(), "VampireCastle");

        // Since its cycle 0 vampires should not spawn
        List<Enemy> enemySpawned = world.possiblySpawnEnemies();
        assertEquals(enemySpawned.size(), 4);
        for (Enemy e : enemySpawned) {
            assertEquals("Slug", e.getEnemyType());
        }
        assertEquals(world.getCycle(), 0);

        world.setCycle(5);
        // Since its cycle 5, vampires should now spawn
        enemySpawned = world.possiblySpawnEnemies();
        assertTrue(enemySpawned.size() > 0); // 1 - 2 vamp can spawn
        for (Enemy e : enemySpawned) {
            assertEquals("Vampire", e.getEnemyType());
        }
        assertEquals(world.getCycle(), 5);
    }
}