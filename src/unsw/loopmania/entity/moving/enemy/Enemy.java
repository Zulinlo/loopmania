package unsw.loopmania.entity.moving.enemy;

import unsw.loopmania.entity.moving.*;
import unsw.loopmania.entity.notmoving.building.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.javatuples.Pair;

/**
 * a basic form of enemy in the world
 */
public abstract class Enemy extends MovingEntity {
    private double enemyMaxHp, enemyCurHp, enemyAtk, battleRadius, supportRadius;
    private int attackOrder, xpDrop, goldDrop;
    private Map<String, Integer> lootDrops;
    private String enemyType;
    private boolean isTranced = false;
    private int roundsTranced = 0;
    private int trancedHp;

    public Enemy(PathPosition position) {
        super(position);
        lootDrops = new HashMap<>();
    }

    // Simple getter function for enemyMaxHp, returns max hp of enemy (not current hp).
    public double getEnemyMaxHp() {
        return enemyMaxHp;
    }

    // Simple getter function to return current hp of the enemy, returns current hp of enemy.
    // Note: Enemies with hp less than or equal to 0 should be killed / destroyed.
    public double getEnemyCurHp() {
        return enemyCurHp;
    }

    // Simple getter function for enemyAtk, returns attack damage of the enemy.
    public double getEnemyAtk() {
        return enemyAtk;
    }

    // Simple getter function for battleRadius, returns the battleRadius of the enemy.
    public double getBattleRadius() {
        return battleRadius;
    }

    // Simple getter function for supportRadius, returns the supportRadius of the enemy.
    public double getSupportRadius() {
        return supportRadius;
    }

    // Simple getter function for attackOrder, returns the attackOrder of the enemy.
    // Note: Attack order determines which enemies attack first (5: fastest, 1: slowest)
    public int getAttackOrder() {
        return attackOrder;
    }

    // Simple getter function for xpDrop, returns the exp dropped by the enemy after being killed.
    public int getXpDrop() {
        return xpDrop;
    }

    // Simple getter function for goldDrop, returns the amount of gold dropped from enemy after being killed.
    public int getGoldDrop() {
        return goldDrop;
    }

    // Simple getter function to return mapping of the loot drops of an enemy, returns lootDrops.
    // Loot drops are items that can be dropped from an enemy after being killed
    // It is the mapping of a 
    // String containing the name of the item to the                       
    // Integer depicting the probability of the item dropping out of a 100.
    public Map<String, Integer> getLootDrops() {
        return lootDrops;
    }

    // Simple getter function to return the enemy type
    public String getEnemyType() {
        return enemyType;
    }

    public int getRoundsTranced() {
        return roundsTranced;
    }

    public boolean isTranced() {
        return isTranced;
    }
    
    // Applies damage to enemy
    public void takeDamage(double damage) {
        enemyCurHp = enemyCurHp - damage;
    }

    // Simple setter function for enemyHp, sets the enemy max hp as the value given
    public void setEnemyMaxHp(double hp) {
        this.enemyMaxHp = hp;
    }

    // Simple setter function to set current enemy hp, Enemy's hp should never exceed the maxHp.
    public void setEnemyCurHp(double newHp) {
        if (newHp > this.getEnemyMaxHp()) {
            setEnemyCurHp(this.enemyMaxHp);
        } else {
            this.enemyCurHp = newHp;
        }
    }

    // Simple setter function for enemyAtk, sets the enemy atk as the value given
    public void setEnemyAtk(double atk) {
        this.enemyAtk = atk;
    }

    // Simple setter function for battleRadius, sets the battle radius of the enemy
    public void setBattleRadius(double battleRange) {
        this.battleRadius = battleRange;
    }

    // Simple setter function for supportRadius, sets the support radius of the enemy
    public void setSupportRadius(double supportRange) {
        this.supportRadius = supportRange;
    }

    // Simple setter function for attackOrder, sets the attack order of the enemy
    public void setAttackOrder(int attackOrder) {
        this.attackOrder = attackOrder;
    }

    // Simple setter function for xpDrop, sets amount of xp dropped from the enemy after being killed
    public void setXpDrop(int xp) {
        this.xpDrop = xp;
    }

    // Simple setter function for goldDrop, sets the amount of gold dropped from the enemy after being killed
    public void setGoldDrop(int gold) {
        this.goldDrop = gold;
    }

    // Simple setter function for enemyType, sets the type of the enemy
    public void setEnemyType(String type) {
        this.enemyType = type;
    }

    /**
     * Check if the enemy is within the battle radius of an entity in the <x, y> position
     * @param posX
     * @param posY
     * @return true if entity is inside the enemy's battle radius adn vice versa.
     * Note: Being on the edge od the radius does not include being in the battle range
     */
    public boolean isInEnemyBattleRange(int posX, int posY) {
        // Pythagoras: x^2 + y^2 < battleRadius^2 to see if within battle radius of enemy
        if ((Math.pow((posX - super.getX()), 2) +  
            Math.pow((posY - super.getY()), 2)) <
            Math.pow(battleRadius, 2)
        ) {
            return true;
        }
        return false;
    }

    /**
     * Check if the enemy is within the support radius of an entity in the <x, y> position
     * @param posX
     * @param posY
     * @return true if entity is inside the enemy's support radius adn vice versa.
     */
    public boolean isInEnemySupportRange(int posX, int posY) {
        // Pythagoras: x^2 + y^2 < battleRadius^2 to see if within support radius of enemy
        if ((Math.pow((posX - super.getX()), 2) +  
            Math.pow((posY - super.getY()), 2)) <
            Math.pow(supportRadius, 2)
        ) {
            return true;
        }
        return false;
    }

    /**
     * move the enemy
     * @buildingEntities: list of buildings in the world, will be use as reference on how the enemy should move
     * @orderedPath : list of pairs of coordinates depicting the path of the world
     */
    public abstract void move(List<Building> buildingEntities, List<Pair<Integer, Integer>> orderedPath);

    /**
     * Initialises the loot / item drops of the enemy
     */
    public abstract void initializeLootDrops();

    /**
     * Trance this enemy
     */
    public void trance() {
        this.isTranced = true;
        this.roundsTranced = 0;
        this.trancedHp = 40;
    }

    /**
     * Update this enemy's tranced status
     * @return boolean if no longer tranced then add to end of enemies to battle
     */
    public boolean updateTrance() {
        roundsTranced++;

        if (roundsTranced == 3) {
            isTranced = false;
            roundsTranced = 0;
            trancedHp = 40;
            return true;
        }

        return false;
    }

    /**
     * Take damage when in tranced form
     * @param damage to be taken
     * @return boolean for if dead or alive
     */
    public boolean reduceTrancedHp(int damage) {
        trancedHp -= damage;

        if (trancedHp <= 0) {
            return true;
        }

        return false;
    }
}
