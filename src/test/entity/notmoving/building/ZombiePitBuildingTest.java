package test.entity.notmoving.building;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.javatuples.Pair;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.entity.notmoving.building.ZombiePitBuilding;

public class ZombiePitBuildingTest {
    // Test the PossiblyGetZombiesSpawnPosition method of the ZombiePitBuilding subclass
    // This test will take a look if the PossiblyGetZombiesSpawnPosition method will not spawn zombies into the world
    // when the requirements of spawning them are not fulfilled,
    // i.e. there are no path tiles in the 3 tiles radius of the zombie pit
    @Test
    public void testPossiblyGetZombiesSpawnPositionNoSpawns() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>(
            Arrays.asList(
                new Pair<>(0, 0),
                new Pair<>(0, 1),
                new Pair<>(0, 2)
            )
        );

        // Place a zombie pit at (5,0)
        SimpleIntegerProperty x = new SimpleIntegerProperty(5);
        SimpleIntegerProperty y = new SimpleIntegerProperty(0);
        ZombiePitBuilding zombSpawnerA = new ZombiePitBuilding(x,y);
        // Place a zombie pit at (3,4)
        SimpleIntegerProperty x2 = new SimpleIntegerProperty(3);
        SimpleIntegerProperty y2 = new SimpleIntegerProperty(4);
        ZombiePitBuilding zombSpawnerB = new ZombiePitBuilding(x2,y2);

        // Check if any of the zombie pits are spawning zombies
        // They should not be giving possible spawn positions to spawn zombies.
        assertTrue(zombSpawnerA.possiblyGetZombiesSpawnPosition(orderedPath).isEmpty()); // Should be empty list
        assertTrue(zombSpawnerB.possiblyGetZombiesSpawnPosition(orderedPath).isEmpty());
    }

    // Test the PossiblyGetZombiesSpawnPosition method of the ZombiePitBuilding subclass
    // This test will take a look if the PossiblyGetZombiesSpawnPosition method will spawn zombies correctly into the world
    @Test
    public void testPossiblyGetZombiesSpawnPositionValidSpawns() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>(
            Arrays.asList(
                new Pair<>(0, 0),
                new Pair<>(0, 1),
                new Pair<>(0, 2),
                new Pair<>(1, 2),
                new Pair<>(2, 2),
                new Pair<>(2, 3)
            )
        );

        // Place a zombie pit at (3, 3)
        SimpleIntegerProperty x = new SimpleIntegerProperty(3);
        SimpleIntegerProperty y = new SimpleIntegerProperty(3);
        ZombiePitBuilding zombSpawnerA = new ZombiePitBuilding(x,y);

        // CHECKS
        List<Pair<Integer, Integer>> validSpawnA = zombSpawnerA.possiblyGetZombiesSpawnPosition(orderedPath);
        assertFalse(validSpawnA.isEmpty());
        assertTrue(validSpawnA.size() >= 2 && validSpawnA.size() <= 3); // There can be either two or three zombies
    }
}
