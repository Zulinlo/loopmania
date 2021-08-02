package test.entity.notmoving.building;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import test.testHelpers.CreateGoal;

import org.javatuples.Pair;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.entity.moving.PathPosition;
import unsw.loopmania.entity.notmoving.building.*;
import unsw.loopmania.entity.notmoving.item.HealthPotion;
import unsw.loopmania.entity.notmoving.item.Sword;
import unsw.loopmania.goal.Goal;

public class HerosCastleTest {

    Goal goal = (new CreateGoal()).DummyGoal();

    // Testing for HerosCastle loading properly
    @Test
    public void HerosCastleLoaderTest() {
        List<Pair<Integer,Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0,0));
        orderedPath.add(new Pair<Integer, Integer>(1,0));
        orderedPath.add(new Pair<Integer, Integer>(2,0));

        int width = 8;
        int height = 14;
        LoopManiaWorld world = new LoopManiaWorld(width, height, orderedPath, goal);

        world.setCharacter(new Character(new PathPosition(0, Arrays.asList(new Pair<>(0, 1), new Pair<>(0, 2)))));
        HerosCastle hCastle = new HerosCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));

        world.setHerosCastle(hCastle);

        assertTrue(world.getShop().size() >= 2);

        assertTrue(hCastle.getShop().size() >= 2);
        
        assertSame(new Sword(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)).getType(), world.getShop().get(0).getType());
        assertSame(new HealthPotion(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0)).getType(), world.getShop().get(1).getType());

        hCastle.removeItem(hCastle.getShop().get(0));
        assertTrue(!(hCastle.getShop().get(0) instanceof Sword));

        // make sure all items can spawn by making shop 1000x and checking for a single instance
        boolean hasSpawnedAllItems = false;
        for (int i = 0; i < 1000; i++) {
            hCastle = new HerosCastle(new SimpleIntegerProperty(0), new SimpleIntegerProperty(0));
            world.setHerosCastle(hCastle);
            if (world.getShop().size() > 4) {
                hasSpawnedAllItems = true;
            }
        }

        assertTrue(hasSpawnedAllItems);
    }
}