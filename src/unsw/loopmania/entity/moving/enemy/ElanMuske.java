package unsw.loopmania.entity.moving.enemy;

import unsw.loopmania.entity.moving.*;
import unsw.loopmania.entity.notmoving.building.*;

import java.util.Map;
import java.util.Random;
import java.util.List;

import org.javatuples.Pair;

/**
 * Elan Muske, a parody of Elon Musk and toughest boss & enemy in the current game.
 * "To the moon!"
 */
public class ElanMuske extends Enemy {
    private int healChance = 20;

    /**
     * Description / stats of a ELAN MUSKE according to our assumptions:
     * Max Health / Hp: 300
     * Attack         : 54
     * Battle radius  : 2
     * Support radius : 2
     * attackOrder    : 1
     * xpDrop         : 100
     * goldDrop       : 150 - 250
     */
    public ElanMuske(PathPosition position) {
        super(position);
        this.setEnemyMaxHp(300);
        setEnemyCurHp(this.getEnemyMaxHp());
        this.setEnemyAtk(54);
        this.setBattleRadius(2);
        this.setSupportRadius(2);
        this.setAttackOrder(1);
        this.setXpDrop(100);

        // Generate the amount of gold that will be dropped by Doggie
        // Should be between 150 - 250 inclusive
        int high = 250;
        int low = 150;
        int goldDrop = (new Random()).nextInt(high - low) + low;
        this.setGoldDrop(goldDrop);
        initializeLootDrops();
        this.setEnemyType("ElanMuske");
    }

    /**
     * Initialises the loot / item drops of ELAN MUSKE
     */
    @Override
    public void initializeLootDrops() {
        Map<String, Integer> lootDrops = this.getLootDrops();
        lootDrops.put("Sword", 30);
        lootDrops.put("Stake", 25);
        lootDrops.put("Staff", 25);
        lootDrops.put("Armour", 15);
        lootDrops.put("Shield", 12);
        lootDrops.put("Helmet", 12);
        lootDrops.put("HealthPotion", 100);
    }

    /**
     * Elan Muske is the stongest enemy available and one of the most active ones to boot.
     * It has a 20% chance to not move and 80% chance to move from its current position when move is called.
     * When moving, it has a 70% chance to move 2 tiles and other 30% chance to move 1 tile.
     * Once it has decided how many tiles to move, he has a 70% chance to move down the path or 30% up the path.
     * @landmark : uneccesary / not
     * @orderedPath: unnecessary / not used
     */
    @Override
    public void move(List<Building> landmark, List<Pair<Integer, Integer>> orderedPath) {
        int moveChoice = (new Random()).nextInt(10);
        // 80% chance to move or not move
        if (moveChoice < 2){
            // Does not move, i.e. does nothing
        }
        else {
            // Move
            int directionChoice = (new Random()).nextInt(10);
            // Sets tiles to move, defaults to 1, 70% chance for tilesToMove to change to 2
            int tilesToMoveChoice = (new Random()).nextInt(10);
            int tilesToMove = 1;
            if (tilesToMoveChoice > 2) {
                tilesToMove = 2;
            }

            for (int i = 0; i < tilesToMove; i++) { // Does multiple move (jumps) if necessary
                if (directionChoice > 2) {
                    moveDownPath();
                } else {
                    moveUpPath();
                }
            }
        }
    }

    /**
     * Sees if elan will heal himself or fellow enemies
     * 20% to heal when called
     * @return true if to heal else false
     */
    public boolean isHeal() {
        int healRoll = (new Random()).nextInt(100);
        if (healRoll < healChance) { // will heal
            return true;
        }
        return false;
    }

    /**     
     * Given a list of enemy and himself, do HEAL.
     * Elan will prioritise healing himself over other enemies.
     * He will only heal his supports (enemies) IFF Elan is currently max health / unharmed.
     * He heals 10 hp lost and will heal lowest cur hp enemy in the given list.
     * if all enemies and himself is full health, does nothing.
     * @param enemies : list of enemies fighting alongside Elan including himself.
     */
    public void doHeal(List<Enemy> enemies) {
        double elanCurHp = this.getEnemyCurHp();
        if (elanCurHp < this.getEnemyMaxHp()) { // Heal Elan muske only as he is not full hp.
            this.setEnemyCurHp(elanCurHp + 10);
            return;
        }

        // Heal other enemies in the list, healing the lowest cur hp enemy in the list given.
        Enemy enemyWithLowestHp = this;
        for (Enemy e : enemies) {
            if (e.getEnemyCurHp() < enemyWithLowestHp.getEnemyCurHp() &&
                e.getEnemyCurHp() >= 0 && !e.isTranced()
            ) {
                enemyWithLowestHp = e;
            }
        }
        enemyWithLowestHp.setEnemyCurHp(enemyWithLowestHp.getEnemyCurHp() + 10);
    }
}
