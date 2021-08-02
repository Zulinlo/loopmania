package test.entity.notmoving.building;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.CreateGoal;

import org.javatuples.Pair;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.entity.notmoving.building.*;
import unsw.loopmania.goal.Goal;

public class BuildingTest {

    Goal goal = (new CreateGoal()).DummyGoal();

    // Testing for Vampire Building loading properly
    @Test
    public void VampireBuilderLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("VampireCastle");
        Building ret = world.convertCardToBuildingByCoordinates(0, 0, 0, 1);
        Building check = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertFalse(check.canPlace(orderedPath));
        Building check1 = new VampireCastleBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(check1.canPlace(orderedPath));
        
        assertEquals(check.getType(), ret.getType());
    }

    // Testing for Barracks Building loading properly
    @Test
    public void BarracksBuilderLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("Barracks");
        Building ret = world.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        Building check = new BarracksBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertFalse(check.canPlace(orderedPath));
        Building check1 = new BarracksBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertTrue(check1.canPlace(orderedPath));
        
        assertEquals(check.getType(), ret.getType());
    }

    // Testing for Trap Building loading properly
    @Test
    public void TrapBuilderLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("Trap");
        Building ret = world.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        Building check = new TrapBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertFalse(check.canPlace(orderedPath));
        Building check1 = new TrapBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertTrue(check1.canPlace(orderedPath));
        
        assertEquals(check.getType(), ret.getType());
    }

    // Testing for Campfire Building loading properly
    @Test
    public void CampfireBuilderLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("Campfire");
        Building ret = world.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        Building check = new CampfireBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(check.canPlace(orderedPath));
        Building check1 = new CampfireBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertFalse(check1.canPlace(orderedPath));
        
        assertEquals(check.getType(), ret.getType());
    }

    // Testing for Tower Building loading properly
    @Test
    public void TowerBuilderLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("Tower");
        Building ret = world.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        Building check = new TowerBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(check.canPlace(orderedPath));
        Building check1 = new TowerBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertFalse(check1.canPlace(orderedPath));
        
        assertEquals(check.getType(), ret.getType());
    }

    // Testing for ZombiePit Building loading properly
    @Test
    public void ZombiePitBuilderLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("ZombiePit");
        Building ret = world.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        Building check = new ZombiePitBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertTrue(check.canPlace(orderedPath));
        Building check1 = new ZombiePitBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertFalse(check1.canPlace(orderedPath));
        
        assertEquals(check.getType(), ret.getType());
    }

    // Testing for Village Building loading properly
    @Test
    public void VillageBuilderLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("Village");
        Building ret = world.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        Building check = new VillageBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        assertFalse(check.canPlace(orderedPath));
        Building check1 = new VillageBuilding(new SimpleIntegerProperty(0), new SimpleIntegerProperty(1));
        assertTrue(check1.canPlace(orderedPath));
        
        assertEquals(check.getType(), ret.getType());
    }
    
}
