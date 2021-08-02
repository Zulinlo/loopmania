package unsw.loopmania.entity.moving.enemy;

import unsw.loopmania.entity.moving.*;
import unsw.loopmania.entity.notmoving.building.*;

import java.util.Map;
import java.util.Random;
import java.util.List;

import org.javatuples.Pair;

public class Zombie extends Enemy{
    private double critChance;

    /**
     * Description / stats of a zombie according to our assumptions:
     * Max Health / Hp: 24
     * Attack         : 10
     * Battle radius  : 3
     * Support radius : 2
     * attackOrder    : 3
     * xpDrop         : 3
     * goldDrop       : 8 - 10 inclusive (random)
     */

    public Zombie(PathPosition position) {
        super(position);
        this.setEnemyMaxHp(24);
        setEnemyCurHp(this.getEnemyMaxHp());
        this.setEnemyAtk(10);
        this.setBattleRadius(3);
        this.setSupportRadius(2);
        this.setAttackOrder(3);
        this.setXpDrop(3);

        // Generate the amount of gold that will be dropped by the zombie
        // Should be between 8 - 10 inclusive
        int high = 10;
        int low = 8;
        int goldDrop = (new Random()).nextInt(high - low) + low;
        this.setGoldDrop(goldDrop);

        // Sets default crit chance to 30 for zombies
        this.critChance = 30;
        initializeLootDrops();
        this.setEnemyType("Zombie");
    }

    @Override
    public void initializeLootDrops() {
        Map<String, Integer> lootDrops = this.getLootDrops();
        lootDrops.put("Sword", 15);
        lootDrops.put("Stake", 12);
        lootDrops.put("Staff", 13);
        lootDrops.put("Armour", 2);
        lootDrops.put("Shield", 5);
        lootDrops.put("Helmet", 5);
        lootDrops.put("HealthPotion", 20);
    }

    /**
     * The zombie is the slowest enemy in the game
     * It has a 70% chance to not move from 30% chance to move from its current position when move is called.
     * When moving, it has a 50/50 chance to move in either directions
     * (Overall, 15% chance to move up the path and 15% chance to move down the path)
     * @landmark : unecessary / not used in determining movement of zombie
     * @orderedPath: unecessary / not used in determining movement of zombie
     */
    @Override
    public void move(List<Building> landmarks, List<Pair<Integer, Integer>> orderedPath) {
        int moveChoice = (new Random()).nextInt(10);
        // 70% chance to not move and 30% not move
        if (moveChoice < 7){
            // Does not move, i.e. does nothing
        }
        else {
            // Move
            int directionChoice = (new Random()).nextInt(2);
            if (directionChoice == 0) {
                moveUpPath();
            } else if (directionChoice == 1) {
                moveDownPath();
            }
        }
    }

    /**
     * Sees if the zombie will crit when method called
     * @return  true if will crit (critRoll < critChance) and vice versa.
     */
    public boolean isCrit() {
        int critRoll = (new Random()).nextInt(100);
        if (critRoll < critChance) { // Sucessfully crits
            return true;
        }
        return false;
    }
}
