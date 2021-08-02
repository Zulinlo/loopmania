package unsw.loopmania.entity.moving.enemy;

import unsw.loopmania.entity.moving.*;
import unsw.loopmania.entity.notmoving.building.*;

import java.util.Map;
import java.util.Random;
import java.util.List;

import org.javatuples.Pair;

/**
 * Doggie, a parody of Doge and a sturdy boss enemy in the game
 * Wow much coin how money so crypto plz mine v rich very currency
 */
public class Doggie extends Enemy{
    private int stunChance = 10;

    /**
     * Description / stats of DOGGIE according to our assumptions:
     * Max Health / Hp: 230
     * Attack         : 46
     * Battle radius  : 2
     * Support radius : 2
     * attackOrder    : 1
     * xpDrop         : 50
     * goldDrop       : 50 - 100
     */
    public Doggie(PathPosition position) {
        super(position);
        this.setEnemyMaxHp(230);
        setEnemyCurHp(this.getEnemyMaxHp());
        this.setEnemyAtk(46);
        this.setBattleRadius(2);
        this.setSupportRadius(2);
        this.setAttackOrder(1);
        this.setXpDrop(50);

        // Generate the amount of gold that will be dropped by Doggie
        // Should be between 50 - 100 inclusive
        int high = 100;
        int low = 50;
        int goldDrop = (new Random()).nextInt(high - low) + low;
        this.setGoldDrop(goldDrop);
        initializeLootDrops();
        this.setEnemyType("Doggie");
    }

    /**
     * Initialises the loot / item drops of Doggie
     * Doggie has a 100% chance of dropping the DoggieCoin
     */
    @Override
    public void initializeLootDrops() {
        Map<String, Integer> lootDrops = this.getLootDrops();
        lootDrops.put("Sword", 20);
        lootDrops.put("Stake", 10);
        lootDrops.put("Staff", 10);
        lootDrops.put("Armour", 8);
        lootDrops.put("Shield", 8);
        lootDrops.put("Helmet", 8);
        lootDrops.put("HealthPotion", 50);
        lootDrops.put("DoggieCoin", 100);
    }

    /**
     * The Doggie is one of the laziest enemy types in the game.
     * It has a 90% chance to not move and 10% chance to move from its current position when move is called.
     * When moving, it has a 70% chance to move down the path or 30% chance to move up the path (becoz dog dont like going backwards).
     * @landmark : uneccesary / not
     * @orderedPath: unnecessary / not used
     */
    @Override
    public void move(List<Building> landmark, List<Pair<Integer, Integer>> orderedPath) {
        int moveChoice = (new Random()).nextInt(10);
        // 90% chance to move or not move
        if (moveChoice != 0){
            // Does not move, i.e. does nothing
        }
        else if (moveChoice == 0){
            // Move
            int directionChoice = (new Random()).nextInt(10);
            if (directionChoice > 2) {
                moveDownPath();
            } else {
                moveUpPath();
            }
        }
    }

    /**
     * Sees if doggie managed to stun player / character
     * @return true 
     */
    public boolean isStun() {
        int stunRoll = (new Random()).nextInt(100);
        if (stunRoll < stunChance) { // Sucessfully stuns
            return true;
        }
        return false;
    }
}
