package test.entity.notmoving.building;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.javatuples.Pair;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;

import unsw.loopmania.entity.notmoving.building.VampireCastleBuilding;

public class VampireCastleBuildingTest {
    // Test the PossiblyGetVampiresSpawnPosition method of the VampireCastleBuilding subclass
    // This test will take a look if the PossiblyGetVampiresSpawnPosition method will not spawn vampires into the world
    // when the requirements of spawning them are not fulfilled, this includes:
    // 1. the number of cycles the character has make is not a multiple of 5
    // 2. there are no path tiles in the 3 tiles radius of the castle
    @Test
    public void testPossiblyGetVampiresSpawnPositionNoSpawns() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>(
            Arrays.asList(
                new Pair<>(0, 0),
                new Pair<>(0, 1),
                new Pair<>(0, 2)
            )
        );

        // Place a vampire castle at (1,0)
        SimpleIntegerProperty x = new SimpleIntegerProperty(1);
        SimpleIntegerProperty y = new SimpleIntegerProperty(0);
        VampireCastleBuilding vampCastle = new VampireCastleBuilding(x,y);
        // (1) Check if cycle checking for vampire spawning is correct
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(0, orderedPath).isEmpty()); // Should be empty list
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(1, orderedPath).isEmpty());
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(2, orderedPath).isEmpty());
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(3, orderedPath).isEmpty());
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(4, orderedPath).isEmpty());
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(21, orderedPath).isEmpty());

        // Place a vampire castle at (5, 5)
        // It should not be possible to spawn vampires as path is out of spawnRadius
        SimpleIntegerProperty x2 = new SimpleIntegerProperty(5);
        SimpleIntegerProperty y2 = new SimpleIntegerProperty(5);
        vampCastle = new VampireCastleBuilding(x2,y2);
        // (2) Check if spawnRegion of the vampire castle is correct
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(0, orderedPath).isEmpty()); // Should be empty list
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(5, orderedPath).isEmpty());
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(10, orderedPath).isEmpty());
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(20, orderedPath).isEmpty());
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(500, orderedPath).isEmpty());
        assertTrue(vampCastle.possiblyGetVampiresSpawnPosition(21, orderedPath).isEmpty());
    }

    // Test the PossiblyGetVampiresSpawnPosition method of the VampireCastleBuilding subclass
    // This test will take a look if the PossiblyGetVampiresSpawnPosition method will spawn vampires correctly into the world
    @Test
    public void testPossiblyGetVampiresSpawnPositionValidSpawns() {
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

        // Place a vampire castle at (1,0)
        SimpleIntegerProperty x = new SimpleIntegerProperty(1);
        SimpleIntegerProperty y = new SimpleIntegerProperty(0);
        VampireCastleBuilding vampCastleA = new VampireCastleBuilding(x,y);
        // Place another vampire castle at (3, 3)
        SimpleIntegerProperty x2 = new SimpleIntegerProperty(3);
        SimpleIntegerProperty y2 = new SimpleIntegerProperty(3);
        VampireCastleBuilding vampCastleB = new VampireCastleBuilding(x2,y2);

        // CHECKS
        assertTrue(vampCastleA.possiblyGetVampiresSpawnPosition(0, orderedPath).isEmpty());
        List<Pair<Integer, Integer>> validSpawnA = vampCastleA.possiblyGetVampiresSpawnPosition(5, orderedPath);
        assertFalse(validSpawnA.isEmpty());
        assertTrue(validSpawnA.size() >= 1 && validSpawnA.size() <= 2); // There can be either one or two vamps

        assertTrue(vampCastleB.possiblyGetVampiresSpawnPosition(0, orderedPath).isEmpty());
        List<Pair<Integer, Integer>> validSpawnB = vampCastleB.possiblyGetVampiresSpawnPosition(20, orderedPath);
        assertFalse(validSpawnB.isEmpty());
        assertTrue(validSpawnB.size() >= 1 && validSpawnB.size() <= 2); // There can be either one or two vamps
    }
}
