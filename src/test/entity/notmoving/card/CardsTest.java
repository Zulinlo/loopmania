package test.entity.notmoving.card;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.CreateGoal;

import org.javatuples.Pair;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.entity.notmoving.card.Card;
import unsw.loopmania.goal.Goal;
import unsw.loopmania.entity.notmoving.card.*;

public class CardsTest {

    Goal goal = (new CreateGoal()).DummyGoal();
    // Testing to see if loadCard for a Vampire card will work
    @Test
    public void VampireCardLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("VampireCastle");
        Card card = new VampireCastleCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        
        assertEquals(card.getType(), world.getCardEntities().get(0).getType());
    }

    // Testing to see if loadCard for a Barracks card will work
    @Test
    public void BarracksCardLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("Barracks");
        Card card = new BarracksCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        
        assertEquals(card.getType(), world.getCardEntities().get(0).getType());
    }

    // Testing to see if loadCard for a Campfire card will work
    @Test
    public void CampfireCardLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("Campfire");
        Card card = new CampfireCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        
        assertEquals(card.getType(), world.getCardEntities().get(0).getType());
    }

    // Testing to see if loadCard for a Tower card will work
    @Test
    public void TowerCardLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("Tower");
        Card card = new TowerCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        
        assertEquals(card.getType(), world.getCardEntities().get(0).getType());
    }

    // Testing to see if loadCard for a Trap card will work
    @Test
    public void TrapCardLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("Trap");
        Card card = new TrapCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        
        assertEquals(card.getType(), world.getCardEntities().get(0).getType());
    }

    // Testing to see if loadCard for a Village card will work
    @Test
    public void VillageCardLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("Village");
        Card card = new VillageCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        
        assertEquals(card.getType(), world.getCardEntities().get(0).getType());
    }

    // Testing to see if loadCard for a ZombiePit card will work
    @Test
    public void ZombiePitCardLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("ZombiePit");
        Card card = new ZombiePitCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
        
        assertEquals(card.getType(), world.getCardEntities().get(0).getType());
    }

    // Testing to see if loadCard for multiple cards will work
    @Test
    public void MultipleLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        world.loadCard("ZombiePit");
        world.loadCard("Campfire");
        world.loadCard("Barracks");
        List<String> list = new ArrayList<>();
        list.add(new ZombiePitCard(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)).getType());
        list.add(new CampfireCard(new SimpleIntegerProperty(1), new SimpleIntegerProperty(0)).getType());
        list.add(new BarracksCard(new SimpleIntegerProperty(2), new SimpleIntegerProperty(0)).getType());
        
        List<String> ret = new ArrayList<>();
        for (Card c: world.getCardEntities()) {
            ret.add(c.getType());
        }
        assertEquals(list, ret);
    }

    // Testing to see if overloading LoadCard will work
    @Test
    public void MultipleOverLoaderTest(){
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);
        for (int i = 0; i < 30; i++) {
            world.loadCard("ZombiePit");
            world.loadCard("Campfire");
            world.loadCard("Barracks");
        }
        assertEquals(8, world.getCardEntities().size());
    }
}
