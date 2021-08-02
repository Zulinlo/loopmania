package unsw.loopmania.entity.moving.enemy;

import unsw.loopmania.entity.moving.*;
import unsw.loopmania.entity.notmoving.building.*;

import java.util.Map;
import java.util.Random;
import java.util.List;

import org.javatuples.Pair;

/**
 * Weakest (fodder) enemy in the game
 */
public class Slug extends Enemy {

    /**
     * Description / stats of a slug according to our assumptions:
     * Max Health / Hp: 12
     * Attack         : 6
     * Battle radius  : 2
     * Support radius : 2
     * attackOrder    : 4
     * xpDrop         : 1
     * goldDrop       : 5
     */

    // Slug Constructor
    public Slug(PathPosition position) {
        super(position);
        this.setEnemyMaxHp(12);
        setEnemyCurHp(this.getEnemyMaxHp());
        this.setEnemyAtk(6);
        this.setBattleRadius(2);
        this.setSupportRadius(2);
        this.setAttackOrder(4);
        this.setXpDrop(1);
        this.setGoldDrop(5);
        initializeLootDrops();
        this.setEnemyType("Slug");
    }

    /**
     * Initialises the loot / item drops of the slug
     */
    @Override
    public void initializeLootDrops() {
        Map<String, Integer> lootDrops = this.getLootDrops();
        lootDrops.put("Sword", 10);
        lootDrops.put("Stake", 8);
        lootDrops.put("Staff", 9);
        lootDrops.put("Armour", 1);
        lootDrops.put("Shield", 2);
        lootDrops.put("Helmet", 2);
        lootDrops.put("HealthPotion", 15);
    }

    /**
     * The slug is one of the slower enemy types in the game.
     * It has a 50% chance to not move and 50% chance to move from its current position when move is called.
     * When moving, it has a 50% chance to move up the path or 50% chance to move down the path.
     * @landmark : uneccesary / not used in determining slime movement.
     * @orderedPath: unnecessary / not used in determining vampire movement
     */
    @Override
    public void move(List<Building> landmark, List<Pair<Integer, Integer>> orderedPath) {
        int moveChoice = (new Random()).nextInt(2);
        // 50% chance to move or not move
        if (moveChoice == 0){
            // Does not move, i.e. does nothing
        }
        else if (moveChoice == 1){
            // Move
            int directionChoice = (new Random()).nextInt(2);
            if (directionChoice == 0) {
                moveUpPath();
            } else if (directionChoice == 1) {
                moveDownPath();
            }
        }
    }
}
