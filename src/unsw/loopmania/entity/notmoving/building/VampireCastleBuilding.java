package unsw.loopmania.entity.notmoving.building;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;

/**
 * a vampire castle building
 * It spawns 1-2 vampires every 5 cycles the character makes on a nearby path within a 3 tiles radius of the building.
 */
public class VampireCastleBuilding extends Building {
    private int spawnRange;

    public VampireCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setStrategy(new NotPath());
        this.setType("VampireCastle");
        this.spawnRange = 3;
    }

    /**
     * Get a list of randomly generated valid spawn positions which can be used to spawn 1-2 vampires.
     * @cycles : the amount of cycles the character has make in the current world
     * @orderedPath : the list of coordinates which represents the path in the world
     * @return a list containing the spawn positions for the vampires given the cycle of the world and orderedPath
     * Spawns 1 - 2 vampires every 5 cycles of the world in a nearby path within a 3 tiles radius of the castle.
     * When the cycle given is exactly the every 5th cycle in the world (modulo of 5), then
     * there is a 50% chance to spawn 1 vampire (other 50% chance to spawn 2 vampires) in the nearby tiles
     * IF AND ONLY IF there is a path tile in the 3 block radius of the vampire castle.
     */
    public List<Pair<Integer, Integer>> possiblyGetVampiresSpawnPosition(int cycles, List<Pair<Integer, Integer>> orderedPath) {
        List<Pair<Integer, Integer>> spawnPositions = new ArrayList<>();
        
        if (cycles % 5 == 0 && cycles > 0) {
            // Get the number of vampires to spawn  (50% chance for 1 and 50% chance for 2)
            int vampiresToSpawn = (new Random()).nextInt(2) + 1;

            // Get the possible path tiles that can be use to spawn the vampires on
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

            // Get the vampire(s) spawn position in a random path tile in orderedPathSpawnCandidates
            Random rand = new Random();
            for (int spawn = 0; spawn < vampiresToSpawn; spawn++) {
                if (orderedPathSpawnCandidates.size() > 0) { // It should only add spawnPosition if there is a valid spawnPosition
                    Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));
                    spawnPositions.add(spawnPosition);
                }
            }
        }

        // spawnPositions can either be empty, contains 1 elements or 2 elements.
        return spawnPositions;
    }
}
