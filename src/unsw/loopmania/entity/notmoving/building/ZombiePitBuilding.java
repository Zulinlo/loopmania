package unsw.loopmania.entity.notmoving.building;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;

/**
 * a zombie pit building
 * It spawns 2-3 zombies every cycle the character makes on a nearby path within a 3 tiles radius of the building.
 */
public class ZombiePitBuilding extends Building {
    private int spawnRange;

    public ZombiePitBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setStrategy(new NotPath());
        this.setType("ZombiePit");
        this.spawnRange = 3;
    }

    /**
     *  Get a list of randomly generated valid spawn positions which can be used to spawn 2-3 zombies.
     * @param orderedPath : the list of coordinates (x, y) which represents the path in the world
     * @return a list containing the spawn positions for the zombies given the orderedPath
     * Spawns 2-3 zombies in a nearby path within a 3 tiles radius of the zombie pit.
     * There is an equal chance of spawning 2 or 3 zombies nearby given that 
     * there is a path tile in the 3 block radius of the zombie castle; 50% chance to spawn 2 and 50% chance to spawn 3
     */
    public List<Pair<Integer, Integer>> possiblyGetZombiesSpawnPosition(List<Pair<Integer, Integer>> orderedPath) {
        List<Pair<Integer, Integer>> spawnPositions = new ArrayList<>();

        // Get the amount of zombies to spawn  (50% chance for 2 and 50% chance for 3)
        int zombiesToSpawn = (new Random()).nextInt(2) + 2;

        // Get the possible path tiles that can be use to spawn the zombies on
        List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
        for (Pair<Integer,Integer> pos : orderedPath) {
            // Possible spawn tiles are nearby path tiles in a radius of 3 tiles inclusive
            if ((Math.pow((pos.getValue0() - this.getX()), 2) +  
                 Math.pow((pos.getValue1() - this.getY()), 2)) <=
                 Math.pow(this.spawnRange, 2)
            ) {
                orderedPathSpawnCandidates.add(pos);
            }
        }

        // Get the zombies' spawn position in a random path tile in orderedPathSpawnCandidates
        // Each zombie might have differing spawn positions.
        Random rand = new Random();
        for (int spawn = 0; spawn < zombiesToSpawn; spawn++) {
            if (orderedPathSpawnCandidates.size() > 0) { // It should only add spawnPosition if there is a valid spawnPosition
                Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));
                spawnPositions.add(spawnPosition);
            }
        }

        // spawnPositions can either be empty, contains 2 elements or 3 elements.
        return spawnPositions;
    }
}