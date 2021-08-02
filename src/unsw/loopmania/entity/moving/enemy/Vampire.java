package unsw.loopmania.entity.moving.enemy;

import unsw.loopmania.entity.moving.*;
import unsw.loopmania.entity.notmoving.building.*;

import java.util.Map;
import java.util.Random;
import java.util.List;

import org.javatuples.Pair;

/**
 * Advanced and very deadly enemy in the game
 */
public class Vampire extends Enemy{
    private double critChance;
    private int critBuffedTurns;

    /**
     * Description / stats of a vampire according to our assumptions:
     * Max Health / Hp: 100
     * Attack         : 18
     * Battle radius  : 5
     * Support radius : 7
     * attackOrder    : 5
     * xpDrop         : 10
     * goldDrop       : 30 - 50 inclusive (random)
     */

    public Vampire(PathPosition position) {
        super(position);
        this.setEnemyMaxHp(100);
        setEnemyCurHp(this.getEnemyMaxHp());
        this.setEnemyAtk(18);
        this.setBattleRadius(5);
        this.setSupportRadius(7);
        this.setAttackOrder(5);
        this.setXpDrop(10);

        // Generate the amount of gold that will be dropped by the vampire
        // Should be between 30 - 50 inclusive
        int high = 50;
        int low = 30;
        int goldDrop = (new Random()).nextInt(high - low) + low;
        this.setGoldDrop(goldDrop);

        // Sets default crit chance to 30 for vampires and the dmg buff gained from crit to 0 turns intially
        this.critChance = 30;
        this.critBuffedTurns = 0;
        initializeLootDrops();
        this.setEnemyType("Vampire");
    }

    @Override
    public void initializeLootDrops() {
        Map<String, Integer> lootDrops = this.getLootDrops();
        lootDrops.put("Sword", 20);
        lootDrops.put("Stake", 16);
        lootDrops.put("Staff", 18);
        lootDrops.put("Armour", 5);
        lootDrops.put("Shield", 8);
        lootDrops.put("Helmet", 8);
        lootDrops.put("HealthPotion", 30);
    }

    /**
     * The vampires is the most proactive enemy in the game
     * It will constantly move to seek its next prey to feast on.
     * It has a 100% chance to move from its current position when move is called.
     * When moving, it has a 50/50 chance to move in either directions if there is no campfire in the world. 
     * However if there is a campfire the vampire will be moving on the opposite direction of the nearest campfire.
     * @landmarks : list of buildings placed in the world
     * @orderedPath : path of the world
     */
    @Override
    public void move(List<Building> landmarks, List<Pair<Integer, Integer>> orderedPath) {
        // Move
        Building nearestCampfire = getNearestCampfireToVampire(landmarks);
        if (nearestCampfire == null) { // There are no campfires yet in the world
            // 50% chance to move in either direction
            int directionChoice = (new Random()).nextInt(2);
            if (directionChoice == 0) {
                moveUpPath();
    
            } else if (directionChoice == 1) {
                moveDownPath();
            }
        } else {
            runAwayFromCampfire(nearestCampfire, orderedPath);
        }
    }

    /**
     * Get the nearest campfire to this vampire in the world. Returns null if there isnt any campfire
     * @param buildings : list of buildings placed in the world
     * @return nearest campfire (type building) if it exists else null
     * nearest campfire is determined from the shortest straight line distance of the campfire to the vampire
     * If there is multiple campfires with the same distance (picks the campfire that is placed first in the world)
     */
    private Building getNearestCampfireToVampire(List<Building> buildings) {
        Building nearestCampfire = null;
        if (buildings == null) return nearestCampfire;
        for (Building building : buildings) {
            if (building.getType().equals("Campfire")) {
                if (nearestCampfire == null) { // Presets the nearest campfire to first campfire found in the list of placed buildings
                    nearestCampfire = building;
                }
                // Distance of building to vampire = sqrt ((x_b^2 - x_v^2) + (y_b^2 - y_b^2))
                double distanceCurCampfire = Math.pow(building.getX() - this.getX(), 2) + Math.pow(building.getY() - this.getY(), 2);
                distanceCurCampfire = Math.sqrt(distanceCurCampfire);
                double distanceNearestCampfire = Math.pow(nearestCampfire.getX() - this.getX(), 2) + Math.pow(nearestCampfire.getY() - this.getY(), 2);
                distanceNearestCampfire = Math.sqrt(distanceNearestCampfire);

                if (distanceCurCampfire < distanceNearestCampfire) {
                    nearestCampfire = building;
                }
            }
        }

        return nearestCampfire;
    }

    private void runAwayFromCampfire(Building nearestCampfire, List<Pair<Integer,Integer>> orderedPath) {
        Pair<Integer, Integer> vampPos = new Pair<>(this.getX(), this.getY());
        int indexVampInPath = orderedPath.indexOf(vampPos);
        Pair<Integer, Integer> downPathPos = orderedPath.get((indexVampInPath + 1) % orderedPath.size());
        Pair<Integer, Integer> upPathPos = orderedPath.get((indexVampInPath - 1 + orderedPath.size()) % orderedPath.size());

        // Find where the campfire is closer to, by calculating the distance of:
        // - the path down of the vampire and the campfire
        // - the path up of the vampire and the campfire
        // Note: distance = sqrt ((x_1^2 - x_2^2) + (y_1^2 - y_2^2)) 
        double distanceDownPath = Math.pow(downPathPos.getValue0() - nearestCampfire.getX(), 2) + Math.pow(downPathPos.getValue1() - nearestCampfire.getY(), 2);
        distanceDownPath = Math.sqrt(distanceDownPath);
        double distanceUpPath = Math.pow(upPathPos.getValue0() - nearestCampfire.getX(), 2) + Math.pow(upPathPos.getValue1() - nearestCampfire.getY(), 2);
        distanceUpPath = Math.sqrt(distanceUpPath);
        if (distanceDownPath < distanceUpPath) {
            // Nearest campfire is located down the path, goes opposite direction; i.e. move up path
            moveUpPath();
        } else { // Nearest campfire is located up the path or same distance  
            moveDownPath();
        }
    }
    
    /**
     * gets atk of vampire, can possibly crit in times
     * Whenever it is called it will account for the crit chance of the vampire
     * and return additional damage if the odds are right (successfully crits)
     */
    public double getPossiblyCritAtk(boolean characterHasShield) {
        Random rand = new Random();
        // calculate crit chance prob and rolls
        int critRoll;
        if (characterHasShield) {
            critRoll = rand.nextInt(200);
        } else {
            critRoll = rand.nextInt(100);
        }
        double dmgMultiplier = 1.0;
        if (critRoll < critChance) { // Successfully crits
            // Updates the turns that the vampire will be empowered, it can be 1-4 turns
            int buffedTurns = rand.nextInt(4) + 1;
            this.critBuffedTurns += buffedTurns;
        }

        // Does additional damage if critBuffed
        // Can do 1.2 to  1.5x more damage (random)
        if (critBuffedTurns > 0) { 
            int additionalDmg = rand.nextInt(4) + 2;
            dmgMultiplier = (double) additionalDmg / 10.0 + dmgMultiplier;
            critBuffedTurns--;
            return super.getEnemyAtk() * dmgMultiplier;
        }

        // Does normal damage
        return super.getEnemyAtk();
    }
}
