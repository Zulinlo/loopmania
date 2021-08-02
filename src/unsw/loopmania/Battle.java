package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.entity.moving.enemy.*;
import unsw.loopmania.entity.notmoving.item.*;

public class Battle {
    private Character character;
    private List<Enemy> enemiesDefeated = new ArrayList<Enemy>();
    private List<Enemy> totalEnemiesToBattle = new ArrayList<Enemy>();
    private List<Enemy> enemiesTargetable = new ArrayList<Enemy>();
    private Enemy targetEnemy;
    private boolean hasCampfire;
    private boolean isCharStunned = false;
    private int towersInRange;
    private Map<String, Integer> pp;

    public Battle(Character character, List<Enemy> enemiesToBattle, boolean hasCampfire, int towersInRange) {
        this.totalEnemiesToBattle = enemiesToBattle;
        this.character = character;
        this.hasCampfire = hasCampfire;
        this.towersInRange = towersInRange;
        for (Enemy e: enemiesToBattle) {
            enemiesTargetable.add(e);
        }

        if (character.getEquippedItems().get("Weapon") == null) {
            this.pp = new Unarmed().getPP();
        } else {
            // Check if confusing mode
            if (character.getEquippedItems().get("Weapon") instanceof CompoundRareItem) {
                this.pp = ((CompoundRareItem) character.getEquippedItems().get("Weapon")).getItems().get(0).getPP();
            } else {
                this.pp = character.getEquippedItems().get("Weapon").getPP();
            }
        }
    }

    /**
     * Character does an action, then enemy attacks
     * @return message string for result of character's action
     */
    public String characterAction(String action) {
        // Check if character is stunned
        if (isCharStunned) {
            isCharStunned = false;
            return "Character is stunned";
        }

        // Character deals damage based on equipped items, base stats, and action chosen
        // Calculates damage based on if helmet, base stats, factors in hidden anduril if confusing mode (+10 damage each)
        int dealDamage = character.dealDamage(targetEnemy);
        int AOEDamage = 0;
        boolean actionMissed = false;

        switch (action) {
            // Unarmed move set
            case "Slap":
                dealDamage = new Unarmed().slap(dealDamage);
                break;
            case "Punch":
                dealDamage = new Unarmed().punch(dealDamage);
                break;
            
            // Sword move set
            case "Slash":
                dealDamage = new Sword(null, null).slash(dealDamage);
                break;
            case "Strike":
                dealDamage = new Sword(null, null).strike(dealDamage);
                break;
            case "Heavy Slash":
                dealDamage = new Sword(null, null).heavySlash(dealDamage);
                // AOE effect that deals its damage to other enemies, then allies and this hits target
                List<Enemy> copyEnemiesTargetable = new ArrayList<Enemy>();
                for (Enemy curr : enemiesTargetable) {
                    copyEnemiesTargetable.add(curr);
                }

                for (Enemy curr : copyEnemiesTargetable) {
                    if (!curr.equals(targetEnemy)) {
                        AOEDamage += dealDamage;
                        curr.takeDamage(dealDamage);
                        if (curr.getEnemyCurHp() <= 0) {
                            enemiesDefeated.add(curr);
                            enemiesTargetable.remove(curr);
                        }
                    }
                }
                break;
            
            // Staff move set
            case "Jab":
                dealDamage = new Staff(null, null).jab(dealDamage);
                break;
            case "Zap":
                dealDamage = new Staff(null, null).zap(dealDamage);
                break;
            case "Trance":
                // Decrease PP
                int prePP = pp.get(action);
                pp.put(action, prePP - 1);
                
                if (new Staff(null, null).trance()) {
                    // trance target enemy
                    if (targetEnemy.getEnemyType().equals("Doggie") || targetEnemy.getEnemyType().equals("ElanMuske")) {
                        targetEnemy.takeDamage(allyDamage(0));
                        return "Trance is ineffective against " + targetEnemy.getEnemyType() + "\n\nAllies dealt: " + Integer.valueOf(dealDamage).toString();
                    }

                    targetEnemy.trance();
                    enemiesTargetable.remove(targetEnemy);
                    return targetEnemy.getEnemyType() + " successfully tranced";
                } else {
                    // failed trance, allied entities do damage
                    dealDamage = allyDamage(0);
                    targetEnemy.takeDamage(dealDamage);
                    return "Trance failed\n\nAllies dealt: " + Integer.valueOf(dealDamage).toString();
                }
            
            // Stake move set
            case "Stab":
                dealDamage = new Stake(null, null).stab(dealDamage);
                break;
            case "Garlic Stab":
                dealDamage = new Stake(null, null).garlicStab(dealDamage, targetEnemy.getEnemyType().equals("Vampire"));
                break;
            
            // Anduril move set
            case "Swing":
                dealDamage = new Anduril(null, null).swing(dealDamage);
                break;
            case "Bad Boy":
                dealDamage = new Anduril(null, null).badBoy(dealDamage, targetEnemy.getEnemyType().equals("Doggie"));
                break;
            case "Space Ex":
                dealDamage = new Anduril(null,  null).spaceEx(dealDamage, targetEnemy.getEnemyType().equals("ElanMuske"));
                break;
        }

        // Decrease PP
        int prePP = pp.get(action);
        pp.put(action, prePP - 1);

        if (dealDamage == 0) {
            actionMissed = true;
        }

        if (hasCampfire) {
            dealDamage *= 2;
        }

        dealDamage = allyDamage(dealDamage);

        targetEnemy.takeDamage(dealDamage);

        // Check if target enemy died
        if (targetEnemy.getEnemyCurHp() <= 0) {
            enemiesTargetable.remove(targetEnemy);
            enemiesDefeated.add(targetEnemy);
        }

        // Message sent to player on how the attack went
        if (action.equals("Heavy Slash")) {
            return action + (actionMissed ? " missed" : " succeeded") + "\n\nCharacter dealt: " + Integer.valueOf(dealDamage - allyDamage(0) + AOEDamage).toString() + "\nAllies dealt: " + Integer.valueOf(allyDamage(0)).toString() + "\nTotal damage: " + Integer.valueOf(dealDamage + AOEDamage).toString();
        } else {
            return action + (actionMissed ? " missed" : " succeeded") + "\n\nCharacter dealt: " + Integer.valueOf(dealDamage - allyDamage(0)).toString() + "\nAllies dealt: " + Integer.valueOf(allyDamage(0)).toString() + "\nTotal damage: " + Integer.valueOf(dealDamage).toString();
        }
    }

    /**
     * Damage of allied soldiers, transformed allied soldiers, and towers is factored in
     * @return total damage
     */
    private int allyDamage(int dealDamage) {
        dealDamage += 5 * towersInRange;

        int trancedAlliedSoldiers = 0;
        for (Enemy curr : totalEnemiesToBattle) {
            if (curr.isTranced()) {
                trancedAlliedSoldiers++;
            }
        }

        dealDamage += (trancedAlliedSoldiers + character.getAlliedSoldiers().size()) * 3;
        return dealDamage;
    }

    /**
     * Enemy does an action
     * @return string of enemy's damage done
     */
    public String enemyAction() {
        String enemyActionMessage = "";

        // Each enemy performs their action
        // Make a copy so that added zombie doesn't screw up the attacking
        List<Enemy> copyEnemiesTargetable = new ArrayList<Enemy>();
        for (Enemy curr : enemiesTargetable) {
            copyEnemiesTargetable.add(curr);
        }

        for (Enemy curr : copyEnemiesTargetable) {

            // Elan Muske
            if (curr.getEnemyType().equals("ElanMuske")) {
                ElanMuske elon = ((ElanMuske) curr);
                if (elon.isHeal()) {
                    elon.doHeal(enemiesTargetable);
                    enemyActionMessage += "\nElanMuske used heal";
                }

                int enemyDamage = (int)curr.getEnemyAtk();
                enemyActionMessage += damageTake(enemyDamage, "ElanMuske");
            }

            // Doggie
            else if (curr.getEnemyType().equals("Doggie")) {
                int enemyDamage = (int)curr.getEnemyAtk();
                enemyActionMessage += damageTake(enemyDamage, "Doggie");
            }

            // Vampire
            else if (curr.getEnemyType().equals("Vampire")) {
                int enemyDamage = (int) ((Vampire) curr).getPossiblyCritAtk(character.getEquippedItems().get("Shield") != null);
                enemyActionMessage += damageTake(enemyDamage, "Vampire");
            }

            // Zombie
            else if (curr.getEnemyType().equals("Zombie")) {
                int enemyDamage = (int)curr.getEnemyAtk();
                enemyActionMessage += damageTake(enemyDamage, "Zombie");
            }

            // Slug
            else if (curr.getEnemyType().equals("Slug")) {
                int enemyDamage = (int)curr.getEnemyAtk();
                enemyActionMessage += damageTake(enemyDamage, "Slug");
            }
        }

        tranceUpdateAll();
        return enemyActionMessage.isEmpty() ? "" : enemyActionMessage.substring(1);
    }

    /**
     * Calculate the received damage and its effect on either an ally or the character
     * @param enemyDamage to be received
     * @param boolean if it is a boss attack
     * @return message 
     */
    private String damageTake(int enemyDamage, String enemyType) {
        String message = "";

        // Get allies that can tank damage
        int targetAlliedSoldier = character.getHighestHpAlliedSoldierIndex();
        Enemy oldestTrancedAlliedSoldier = null;
        for (Enemy curr : totalEnemiesToBattle) {
            if (curr.isTranced() && oldestTrancedAlliedSoldier == null) {
                oldestTrancedAlliedSoldier = curr;
            } else if (curr.isTranced() && curr.getRoundsTranced() >= oldestTrancedAlliedSoldier.getRoundsTranced()) {
                oldestTrancedAlliedSoldier = curr;
            }
        }

        // Deal the damage prioritizing tranced allies > allies > character
        // Event where character takes damage
        if (targetAlliedSoldier == -1 && oldestTrancedAlliedSoldier == null) {
            message += "\n" + enemyType + " dealt " + Integer.valueOf(character.damageTaken(enemyDamage, enemyType.equals("Doggie") || enemyType.equals("ElanMuske"))).toString() + " to character";
        }

        // Event where tranced allied soldier takes damage
        else if (oldestTrancedAlliedSoldier != null) {
            // If is zombie and crits, then remove prior state of tranced enemy
            if (enemyType.equals("Zombie") && new Zombie(null).isCrit()) {
                totalEnemiesToBattle.remove(oldestTrancedAlliedSoldier);
                enemiesDefeated.add(oldestTrancedAlliedSoldier);
                
                Enemy newZombie = new Zombie(null);
                enemiesTargetable.add(newZombie);
                totalEnemiesToBattle.add(newZombie);

                message += "\nZombie infected oldest tranced ally";
            } else {
                // Deal damage to tranced ally soldier and check if died
                if (oldestTrancedAlliedSoldier.reduceTrancedHp(enemyDamage)) {
                    totalEnemiesToBattle.remove(oldestTrancedAlliedSoldier);
                    enemiesDefeated.add(oldestTrancedAlliedSoldier);
                    
                    message += "\n" + enemyType + " dealt " + Integer.valueOf(enemyDamage).toString() + " and killed tranced ally";
                } else {
                    message += "\n" + enemyType + " dealt " + Integer.valueOf(enemyDamage).toString() + " to tranced ally";
                }
            }
        }

        // Event where allied soldier takes damage
        else if (targetAlliedSoldier != -1) {
            // If is zombie and crits, then transform allied soldier
            if (enemyType.equals("Zombie") && new Zombie(null).isCrit()) {
                character.reduceAlliedSoldierHpByIndex(targetAlliedSoldier, 100);
                
                Enemy newZombie = new Zombie(null);
                enemiesTargetable.add(newZombie);
                totalEnemiesToBattle.add(newZombie);

                message += "\n" + enemyType + " infected ally";
            } else {
                // Deal damage to allied soldier and check if died
                if (character.reduceAlliedSoldierHpByIndex(targetAlliedSoldier, enemyDamage)) {
                    // If still alive
                    message += "\n" + enemyType + " dealt " + Integer.valueOf(enemyDamage).toString() + " to ally";
                } else {
                    message += "\n" + enemyType + " dealt " + Integer.valueOf(enemyDamage).toString() + ", killed ally";
                }
            }
        }

        // Additional enemy effects
        if (enemyType.equals("Doggie") && targetAlliedSoldier == -1 && oldestTrancedAlliedSoldier == null) {
            if (new Doggie(null).isStun()) {
                isCharStunned = true;
                message += "\n" + enemyType + " stunned character";
            }
        }

        return message;
    }

    /**
     * Updates tranced status of all enemies
     */
    private void tranceUpdateAll() {
        // Update tranced enemies status
        for (Enemy curr : totalEnemiesToBattle) {
            if (curr.isTranced()) {
                // if no longer tranced then add to targetable enemies list
                boolean isNotStillTranced = curr.updateTrance();
                if (isNotStillTranced) {
                    enemiesTargetable.add(curr);
                }
            }
        }
    }

    /**
     * Setter for target enemy to attack
     * @param target enemy
     */
    public void setTargetEnemy(Enemy enemy) {
        this.targetEnemy = enemy;
    }
    
    /**
     * Get PP
     * @param action
     * @return pp
     */
    public int getPP(String action) {
        return pp.get(action);
    }

    /**
     * Is there PP left for the action
     * @param action 
     * @return boolean
     */
    public boolean actionHasPP(String action) {
        return pp.get(action) > 0;
    }

    /**
     * Get if battle has ended
     * @return boolean
     */
    public boolean hasBattleEnded() {
        return enemiesTargetable.size() == 0;
    }

    /**
     * When all enemies are dead or the character has died then end the battle
     * @return enemiesDefeated to collect their loot
     */
    public List<Enemy> endBattle() {
        // Check for any still tranced enemies survived, then get their loot
        for (Enemy curr : totalEnemiesToBattle) {
            if (curr.isTranced()) {
                enemiesDefeated.add(curr);
            }
        }

        return enemiesDefeated;
    }

    public List<Enemy> getEnemies() {
        return enemiesTargetable;
    }
}
