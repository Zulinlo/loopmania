package unsw.loopmania;

import unsw.loopmania.entity.*;
import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.entity.moving.PathPosition;
import unsw.loopmania.entity.moving.enemy.*;
import unsw.loopmania.entity.moving.enemy.Enemy;
import unsw.loopmania.entity.notmoving.*;
import unsw.loopmania.entity.notmoving.building.*;
import unsw.loopmania.entity.notmoving.card.*;
import unsw.loopmania.entity.notmoving.item.Item;
import unsw.loopmania.goal.Goal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * A backend world.
 *
 * A world can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 */
public class LoopManiaWorld {

    /**
     * width of the world in GridPane cells
     */
    private int width;

    /**
     * height of the world in GridPane cells
     */
    private int height;

    /**
     * generic entitites - i.e. those which don't have dedicated fields
     */
    private List<Entity> nonSpecifiedEntities;

    private Character character;

    private List<Enemy> enemies;

    private List<Card> cardEntities;

    private List<Building> buildingEntities;

    private List<HealthPotionDrop> healthPotionDrops;

    private List<GoldDrop> goldDrops;

    private HerosCastle herosCastle;

    private int cycles = 0;

    private int elonMuskSpawnCooldown = 0;

    private boolean newCycle;

    private int doggiePrice;

    private boolean hasBeatElan = false;

    private boolean hasBeatDoggie = false;

    GameModeState standardState;
	GameModeState berserkerState;
	GameModeState survivalState;
    GameModeState confusingState;
    GameModeState state;

    private Battle battle;

    private Goal goal;

    /**
     * list of x,y coordinate pairs in the order by which moving entities traverse them
     */
    private List<Pair<Integer, Integer>> orderedPath;

    /**
     * create the world (constructor)
     * 
     * @param width width of world in number of cells
     * @param height height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath, Goal goal) {
        this.width = width;
        this.height = height;
        this.goal = goal;
        nonSpecifiedEntities = new ArrayList<>();
        character = null;
        enemies = new ArrayList<>();
        cardEntities = new ArrayList<>();
        this.doggiePrice = 200;

        this.standardState = new StandardState();
        this.berserkerState = new BerserkerState();
        this.survivalState = new SurvivalState();
        this.confusingState = new ConfusingState();

        this.orderedPath = orderedPath;
        healthPotionDrops = new ArrayList<>();
        goldDrops = new ArrayList<>();
        buildingEntities = new ArrayList<>(); 
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Character getCharacter() {
        return character;
    }

    public int getCycle() {
        return cycles;
    }

    public SimpleIntegerProperty getCycleProperty() {
        return new SimpleIntegerProperty(cycles);
    }

    //for test
    public void setElanBeat() {
        hasBeatElan = true;
    }

    //for test
    public void setDoggieBeat() {
        hasBeatDoggie = true;
    }

    public List<HealthPotionDrop> getHealthPotionDrops() {
        return healthPotionDrops;
    }

    public List<GoldDrop> getGoldDrops() {
        return goldDrops;
    }

    /**
     * set the character. This is necessary because it is loaded as a special entity out of the file
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * set the cycle count in the world. Should only be used for testing
     * @param cycle new cycle that wants to be set
     */
    public void setCycle(int cycle) {
        this.cycles = cycle;
    }


    /**
     * add a generic entity (without it's own dedicated method for adding to the world)
     * @param entity
     */
    public void addEntity(Entity entity) {
        // for adding non-specific entities (ones without another dedicated list)
        nonSpecifiedEntities.add(entity);
    }

    /**
     * spawn HealthPotion on ground at the start of each cycle
     */
    public HealthPotionDrop spawnHealthPotionDropOnGround() {
        // Health Potions Drop spawns
        Pair<Integer, Integer> randomPosition = getRandomPositionOnPath();
        if (randomPosition == null)
            return null;

        HealthPotionDrop healthPotion = new HealthPotionDrop(new SimpleIntegerProperty(randomPosition.getValue0().intValue()), new SimpleIntegerProperty(randomPosition.getValue1().intValue()));
        healthPotionDrops.add(healthPotion);
        return healthPotion;
    }

    /**
     * spawn Gold on ground at the start of each cycle
     */
    public GoldDrop spawnGoldDropOnGround() {
        // Gold Drop spawns
        Pair<Integer, Integer> randomPosition = getRandomPositionOnPath();
        if (randomPosition == null)
            return null;

        GoldDrop goldDrop = new GoldDrop(new SimpleIntegerProperty(randomPosition.getValue0().intValue()), new SimpleIntegerProperty(randomPosition.getValue1().intValue()));
        goldDrops.add(goldDrop);

        return goldDrop;
    }

    /**
     * spawns enemies if the conditions warrant it, adds to world
     * @return list of the enemies to be displayed on screen
     */
    public List<Enemy> possiblySpawnEnemies(){
        List<Enemy> enemiesToSpawn = new ArrayList<>();

        // ONLY spawns enemies when a cycle is recently completed
        // Get the possible spawn position of a / multiple slugs to spawn in the world.
        // Can sometimes not be a valid position, i.e. does not spawn slugs.
        List<Pair<Integer, Integer>> slugsPos = possiblyGetSlugsSpawnPosition();
        for (Pair<Integer, Integer> curPos : slugsPos) { // Adds the slugs to the list of enemies to spawn
            int indexInPath = orderedPath.indexOf(curPos);
            Slug newSlug = new Slug(new PathPosition(indexInPath, orderedPath));
            enemies.add(newSlug);
            enemiesToSpawn.add(newSlug);
        }

        // Get the possible spawn positions of a / multiple vampire(s) to spawn in the world
        // Can sometimes not do anything; i.e, does not spawn anything
        // Will only add spawn positions of vampires when there is at least 1 vampire castle in the world.
        // We should only getPossibleSpawn for vampires when the character is on the hero's castle (just recently completes a cyle)
        List<Pair<Integer, Integer>> vampiresPos = new ArrayList<>();
        for (Building building : buildingEntities) {
            if (building.getType().equals("VampireCastle")) {
                VampireCastleBuilding vampSpawner = (VampireCastleBuilding) building;
                vampiresPos.addAll(vampSpawner.possiblyGetVampiresSpawnPosition(cycles, orderedPath));
            }
        }
        for (Pair<Integer, Integer> curPos : vampiresPos) {
            int indexInPath = orderedPath.indexOf(curPos);
            Vampire newVampire = new Vampire(new PathPosition (indexInPath, orderedPath));
            enemies.add(newVampire);
            enemiesToSpawn.add(newVampire);
        }

        // Get the possible spawn positions of multiple zombie(s) to spawn in the world
        // Can sometimes not do anything; i.e, does not spawn anything
        // Will only add spawn positions of zombies when there is at least 1 zombie pit in the world.
        // We should only getPossibleSpawn for zombies when the character is on the hero's castle (just recently completes a cyle)
        List<Pair<Integer, Integer>> zombiesPos = new ArrayList<>();
        for (Building building : buildingEntities) {
            if (building.getType().equals("ZombiePit")) {
                ZombiePitBuilding zombSpawner = (ZombiePitBuilding) building;
                zombiesPos.addAll(zombSpawner.possiblyGetZombiesSpawnPosition(orderedPath));
            }
        }
        for (Pair<Integer, Integer> curPos : zombiesPos) {
            int indexInPath = orderedPath.indexOf(curPos);
            Zombie newZombie = new Zombie(new PathPosition (indexInPath, orderedPath));
            enemies.add(newZombie);
            enemiesToSpawn.add(newZombie);
        }

        // Get the possible spawn position of Doggie in the world
        // Will spawn doggie if there is a valid spawn position passed from the function
        Pair<Integer, Integer> doggiePos = possiblyGetDoggieSpawnPosition(cycles);
        if (doggiePos != null) {
            int indexInPath = orderedPath.indexOf(doggiePos);
            Doggie newDoggie = new Doggie(new PathPosition (indexInPath, orderedPath));
            enemies.add(newDoggie);
            enemiesToSpawn.add(newDoggie);
        }

        // Get the possible spawn position of ElanMuske in the world
        // Will spawn elan muske if there is a valid spawn position passed from the function
        // Will only spawn elon if cooldown spawn is 0.
        // I.e. when elon is killed, another elon can spawn in the next 15 cycles.
        Pair<Integer, Integer> elonMuskPos = possiblyGetElanMuskeSpawnPosition(cycles, character.getExp());
        if (elonMuskPos != null && this.elonMuskSpawnCooldown == 0) {
            int indexInPath = orderedPath.indexOf(elonMuskPos);
            ElanMuske newRichPerson = new ElanMuske(new PathPosition (indexInPath, orderedPath));
            enemies.add(newRichPerson);
            enemiesToSpawn.add(newRichPerson);
            // Sets elon spawn spawn cd to 15. Will only spawn another elon if cd = 0.
            elonMuskSpawnCooldown = 15;

            // Increase doggie coin value when spawning
            increaseDoggieCoinPrice();
        }  

        return enemiesToSpawn;
    }

    /**
     * Run the expected battles in the world, based on current world state
     * Stores the new instance of a battle
     * @return boolean if battle started or not
     */
    public boolean newBattle() {
        List<Enemy> enemiesToBattle = new ArrayList<Enemy>();

        // Get and add all enemies in the world that has the character inside their battle range
        for (Enemy e: enemies) {
            if (e.isInEnemyBattleRange(character.getX(), character.getY()) && enemiesToBattle.size() < 3) {
                enemiesToBattle.add(e);
            }
        }
        
        // Initiates a battle if character encounters an enemy
        if (enemiesToBattle.size() > 0) {
            // Retrieve supporting enemies that are within support radius
            for (Enemy e: enemies) {
                if (e.isInEnemySupportRange(character.getX(), character.getY())
                    && !enemiesToBattle.contains(e)
                    && enemiesToBattle.size() < 3    
                ) {
                    enemiesToBattle.add(e);
                }
            }

            Collections.sort(enemiesToBattle, Comparator.comparing(Enemy::getEnemyMaxHp));
        
            this.battle = new Battle(character, enemiesToBattle, campfireInRange(), towersInRange());
            return true;
        }

        return false;
    }

    /**
     * When battle has ended, kill enemies and get loot
     * @return enemies defeated during the battle
     */
    public List<Enemy> endBattle() {
        // remove all enemies made or battle
        
        List<Enemy> totalEnemiesBattled = battle.endBattle();
        for (Enemy curr : totalEnemiesBattled) {
            if (curr.getEnemyType().equals("ElanMuske")) decreaseDoggieCoinPrice(); // elan musk defeated and dogeCoin plummets in value
            killEnemy(curr);
        }

        levelCheck();
        return totalEnemiesBattled;
    }

    /**
     * Checks if there is a campfire in the range of the character currently
     * @return true if campfire in range (campfire has 2 tiles radius), else false.
     */
    public boolean campfireInRange() {
        // Pythagoras: x^2 + y^2 < campfireRange^2 to see if within campfire range (must be inside and not on edge)
        for (Building building: buildingEntities) {
            if ((Math.pow((character.getX() - building.getX()), 2) +  
                Math.pow((character.getY() - building.getY()), 2)) <
                Math.pow(2, 2) && building.getType().equals("Campfire")
            ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the number of towers that are in range of the character currently (tower has range of 3 tiles).
     * @return number of towers in range.
     */
    public int towersInRange() {
        // Pythagoras: x^2 + y^2 < TowerRange^2 to see if within tower's range
        int ret = 0;
        for (Building building: buildingEntities) {
            if ((Math.pow((character.getX() - building.getX()), 2) +  
                Math.pow((character.getY() - building.getY()), 2)) <
                Math.pow(3, 2) && building.getType().equals("Tower")
            ) {
                ret++;
            }
        }
        return ret;
    }

    /**
     * spawn a card in the world and return the card entity
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public Card loadCard(String type){
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()){
            removeCard(0);
        }
        Card card = null;
        switch(type){
            case "Barracks": 
                card = new BarracksCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
                break;
            case "Campfire": 
                card = new CampfireCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
                break;
            case "Tower": 
                card = new TowerCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
                break;
            case "Trap": 
                card = new TrapCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
                break;
            case "VampireCastle": 
                card = new VampireCastleCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
                break;
            case "ZombiePit": 
                card = new ZombiePitCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
                break;
            case "Village":
                card = new VillageCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
                break;      
        }
        cardEntities.add(card);
        return card;
    }

    public void setNewCycle(boolean bool) {
        newCycle = bool;
    }

    public boolean isNewCycle() {
        return this.newCycle;
    }

    /**
     * run moves which occur with every tick without needing to spawn anything immediately
     */
    public void runTickMoves(){
        character.moveDownPath();
        moveAllEnemies(buildingEntities, orderedPath);

        int charX = character.getX();
        int charY = character.getY();

        if (isCharacterAtStartOfCycle()) {
            cycles++;
            // Reduce spawn cooldown of elon if needed
            if (elonMuskSpawnCooldown > 0) elonMuskSpawnCooldown--;
            setNewCycle(true);
        } else {
            setNewCycle(false);
        }
        
        // Updates buildings
        for (Building building: buildingEntities) {
            if (building.getX() == charX && building.getY() == charY) {
                switch (building.getType()){
                    case "Village":
                        character.addHealth(20);
                        break;
                    case "Barracks":
                        character.addAlliedSoldier();
                }
            }
        }
    }

    /**
     * checks if character is at the start of a cycle
     * @return boolean
     */
    public boolean isCharacterAtStartOfCycle() {
        int xCoord = herosCastle.getX();
        int yCoord = herosCastle.getY(); 
        return character.getX() == xCoord && character.getY() == yCoord;
    }

    /**
     * remove a card by its x, y coordinates
     * @param cardNodeX x index from 0 to width-1 of card to be removed
     * @param cardNodeY y index from 0 to height-1 of card to be removed
     * @param buildingNodeX x index from 0 to width-1 of building to be added
     * @param buildingNodeY y index from 0 to height-1 of building to be added
     */
    public Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        // start by getting card
        Card card = null;
        for (Card c: cardEntities){
            if ((c.getX() == cardNodeX) && (c.getY() == cardNodeY)){
                card = c;
                break;
            }
        }
        Building newBuilding = new BarracksBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
        // now spawn building
        switch(card.getType()) {
            case "Barracks": 
                newBuilding = new BarracksBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                break;
            case "Campfire": 
                newBuilding = new CampfireBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                break;
            case "Tower": 
                newBuilding = new TowerBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                break;
            case "Trap": 
                newBuilding = new TrapBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                break;
            case "VampireCastle": 
                newBuilding = new VampireCastleBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                break;
            case "ZombiePit": 
                newBuilding = new ZombiePitBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                break;
            case "Village": 
                newBuilding = new VillageBuilding(new SimpleIntegerProperty(buildingNodeX), new SimpleIntegerProperty(buildingNodeY));
                break;
        }


        buildingEntities.add(newBuilding);

        // destroy the card
        card.destroy();
        cardEntities.remove(card);
        shiftCardsDownFromXCoordinate(cardNodeX);

        return newBuilding;
    }

    public boolean buildingPositionValid(Building building) {
        return building.canPlace(orderedPath);
    }

    public void setHerosCastle(HerosCastle building) {
        herosCastle = building;
    }

    public Building getHerosCastle() {
        return herosCastle;
    }

    public List<Card> getCardEntities() {
        return cardEntities;
    }

    public List<Item> getUnequippedItems() {
        return character.getUnequippedInventory();
    }

    public boolean gameWon() {
       return goal.isGoalComplete(character.getGold(), character.getExp(), cycles, (hasBeatDoggie && hasBeatElan));
    }

    //Sets the correct level and max health for character
    public void levelCheck() {
        if (character.getExp() >= 406) {
            character.setLevel(10);
            character.setMaxHealth(145);
            character.setBaseAtk(12);
            return;
        }
        if (character.getExp() >= 266) {
            character.setLevel(9);
            character.setMaxHealth(140);
            character.setBaseAtk(11);
            return;
        }
        if (character.getExp() >= 173) {
            character.setLevel(8);
            character.setMaxHealth(135);
            character.setBaseAtk(10);
            return;
        }
        if (character.getExp() >= 111) {
            character.setLevel(7);
            character.setMaxHealth(130);
            character.setBaseAtk(9);
            return;
        }
        if (character.getExp() >= 70) {
            character.setLevel(6);
            character.setMaxHealth(125);
            character.setBaseAtk(8);
            return;
        }
        if (character.getExp() >= 43) {
            character.setLevel(5);
            character.setMaxHealth(120);
            character.setBaseAtk(7);
            return;
        }
        if (character.getExp() >= 25) {
            character.setLevel(4);
            character.setMaxHealth(115);
            character.setBaseAtk(6);
            return;
        }
        if (character.getExp() >= 13) {
            character.setLevel(3);
            character.setMaxHealth(110);
            character.setBaseAtk(5);
            return;
        }
        if (character.getExp() >= 5) {
            character.setLevel(2);
            character.setMaxHealth(105);
            character.setBaseAtk(4);
            return;
        }
        if (character.getExp() >= 0) {
            character.setLevel(1);
            character.setMaxHealth(100);
            character.setBaseAtk(3);
            return;
        }
    }

    // resets world by clearing all enities and character attributes in current world
    public void reset() {
        while (character.getX() != herosCastle.getX() || character.getY() != herosCastle.getY()) {
            character.moveDownPath();
        }

        cycles = 0;
        for (Enemy e: enemies) {
            e.destroy();
        }
        enemies.clear();

        for (Card c: cardEntities) {
            c.destroy();
        }
        cardEntities.clear();

        for (Building b: buildingEntities) {
            b.destroy();
        }
        buildingEntities.clear();

        for (GoldDrop g: goldDrops) {
            g.destroy();
        }
        goldDrops.clear();

        for (HealthPotionDrop h: healthPotionDrops) {
            h.destroy();
        }
        healthPotionDrops.clear();

        for (Entity n : nonSpecifiedEntities) {
            if (n != null)
                n.destroy();
        }
        nonSpecifiedEntities.clear();

        character.reset();
    }

    public List<Pair<Integer, Integer>> getPath() {
        return orderedPath;
    }

    public SimpleIntegerProperty getCharacterGold() {
        return character.getGoldProperty();
    }

    public SimpleIntegerProperty getCharacterExp() {
        return character.getExpProperty();
    }

    public SimpleIntegerProperty getCharacterHealth() {
        return character.getHealthProperty();
    }

    public SimpleIntegerProperty getCharacterMaxHealth() {
        return character.getMaxHealthProperty();
    }

    public SimpleIntegerProperty getCharacterLevel() {
        return character.getLevelProperty();
    }

    public List<Item> getShop() {
        Random random = new Random();
        int offset = random.nextInt(500);
        offset -= 250;
        doggiePrice = offset+doggiePrice;
        if (doggiePrice < 0) doggiePrice = doggiePrice*-1;
        for (Item i: character.getUnequippedInventory()) {
            if (i.getType().equals("DoggieCoin")) {
                i.setSellPrice(doggiePrice);
            }
        }
        return herosCastle.shop();
    }

    public int getDoggiePrice() {
        return this.doggiePrice;
    }
 
    public List<Item> getCurrShop() {
        return herosCastle.getShop();
    }

    public boolean buyItem(Item i) {
        if (character.getGold() - i.getBuyPrice() > 0) {
            state.buy(i, herosCastle);
            character.removeGold(i.getBuyPrice());
            return true;
        }
        return false;
    }

    public void setMode(String string) {
        switch (string) {
            case "Standard":
                state = standardState;
                break;
            case "Survival":
                state = survivalState;
                break;
            case "Berserker":
                state = berserkerState;
                break;
            case "Confusing":
                state = confusingState;
                break;
        }
    }

    public GameModeState getMode() {
        return this.state;
    }

    /**
     * kill an enemy
     * @param enemy enemy to be killed
     */
    public void killEnemy(Enemy enemy){
        enemy.destroy();
        enemies.remove(enemy);
    }

    /**
     * Counts how many slugs exist in the enemies list (current existing enemies in the world).
     * @return number of slugs in enemies list.
     */
    private int getSlugsCount() {
        int count = 0;
        for (Enemy e : enemies) {
            if (e instanceof Slug) {
                count++;
            }
        }
        return count;
    }

    /**
     * Get a list of multiple randomly generated position which can be used to spawn multiple slugs.
     * 100% chance to spawn multiple slugs until there are 4 slugs in the world every cycle in the world.
     * A slug can spawn randomly on any path tiles in the world, but cannot spawn when:
     *      - spawn position set to on, before or after 2 tiles of the character in the world
     *      - there is already 4 slugs in the world
     * @return empty list if that wont be spawning any slug or it isn't possible, or multiple random coordinate pair if should go ahead.
     */
    private List<Pair<Integer, Integer>> possiblyGetSlugsSpawnPosition() {
        List<Pair<Integer, Integer>> spawnPositions = new ArrayList<>();
        Random rand = new Random();

        // Get the possible path tiles that can be use to spawn the slugs on
        List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
        int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
        // Slugs cannot spawn immediately before or after 2 tiles of the character (so not instant battle after spawning)
        // inclusive start and exclusive end of range of positions not allowed
        int startNotAllowed = (indexPosition - 2 + orderedPath.size())%orderedPath.size();
        int endNotAllowed = (indexPosition + 3)%orderedPath.size();
        // note terminating condition has to be != rather than < since wrap around...
        for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
            orderedPathSpawnCandidates.add(orderedPath.get(i));
        }

        // Limits the number of slugs in the world to 4 
        // Spawns slugs until there are 4 slugs at most in the world
        // Random choice of choosing which tile to spawn slugs in; slugs can spawn on different tiles
        int slugsToSpawn = 4 - getSlugsCount();
        for (int spawn = 0; spawn < slugsToSpawn; spawn++) {
            if (orderedPathSpawnCandidates.size() > 0) {
                Pair<Integer, Integer> spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));
                spawnPositions.add(spawnPosition);
            }
        }
        
        return spawnPositions;
    }

    /**
     * Get a randomly generated position which can be used to spawn DOGGIE.
     * 100% chance to spawn doggie every 10 cycles in the world.
     * Doggie can only spawn on, before or after 3 tiles of the character in the world.
     * @return null if that wont be spawning doggie or isn't possible, or a random coordinate pair if valid.
     */
    private Pair<Integer, Integer> possiblyGetDoggieSpawnPosition(int cycles) {
        Pair<Integer, Integer> spawnPosition = null;
        Random rand = new Random();

        if ((cycles % 10) != 0 || cycles == 0) {
            // Cannot spawn Doggie
            return spawnPosition;
        }

        // Get the possible path tiles that can be use to spawn the doggie on iff cycle conditions are met
        List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
        int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
        int startNotAllowed = (indexPosition - 3 + orderedPath.size())%orderedPath.size();
        int endNotAllowed = (indexPosition + 4)%orderedPath.size();
        for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
            orderedPathSpawnCandidates.add(orderedPath.get(i));
        }

        // Random choice of choosing which tile to spawn the Doggie in.
        if (orderedPathSpawnCandidates.size() > 0) {
            spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));
        }
        
        return spawnPosition;
    }

    /**
     * Get a randomly generated position which can be used to spawn Elan Muske.
     * 100% chance to spawn Elan Muske if character have at least make 40 cycles and character has 408 exp or more.
     * ElanMuske can only spawn on, before or after 5 tiles of the character in the world.
     * @return null if that wont be spawning elon or isn't possible, or a random coordinate pair if valid.
     */
    private Pair<Integer, Integer> possiblyGetElanMuskeSpawnPosition(int cycles, int exp) {
        Pair<Integer, Integer> spawnPosition = null;
        Random rand = new Random();

        if (cycles < 15 || exp < 406) {
            // Cannot spawn ElanMuske
            return spawnPosition;
        }

        // Get the possible path tiles that can be use to spawn elan muske on iff conditions are met
        List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
        int indexPosition = orderedPath.indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
        int startNotAllowed = (indexPosition - 5 + orderedPath.size())%orderedPath.size();
        int endNotAllowed = (indexPosition + 6)%orderedPath.size();
        for (int i=endNotAllowed; i!=startNotAllowed; i=(i+1)%orderedPath.size()){
            orderedPathSpawnCandidates.add(orderedPath.get(i));
        }

        // Random choice of choosing which tile to spawn the Elon Musk in.
        if (orderedPathSpawnCandidates.size() > 0) {
            spawnPosition = orderedPathSpawnCandidates.get(rand.nextInt(orderedPathSpawnCandidates.size()));
        }
        
        return spawnPosition;
    }

    /**
     * remove card at a particular index of cards (position in gridpane of unplayed cards)
     * @param index the index of the card, from 0 to length-1
     */
    private void removeCard(int index){
        Card c = cardEntities.get(index);
        int x = c.getX();
        c.destroy();
        cardEntities.remove(index);
        shiftCardsDownFromXCoordinate(x);
    }

    /**
     * shift card coordinates down starting from x coordinate
     * @param x x coordinate which can range from 0 to width-1
     */
    private void shiftCardsDownFromXCoordinate(int x){
        for (Card c: cardEntities){
            if (c.getX() >= x){
                c.x().set(c.getX()-1);
            }
        }
    }

    /**
     * move all enemies and damage them if they pass the traps
     * @buidlingEntities: buildings placed in the world
     * @orderedPath : orderedPath of the world
     */
    private void moveAllEnemies(List<Building> buildingEntities, List<Pair<Integer, Integer>> orderedPath) {
        List<Enemy> removeEnemies = new ArrayList<>();
        List<Building> removeBuildings = new ArrayList<>();
        for (Enemy e: enemies){
            e.move(buildingEntities, orderedPath);
            for (Building building: buildingEntities) {
                if (building.getType().equals("Trap") && building.getX() == e.getX() && building.getY() == e.getY()) {
                    removeEnemies.add(e);
                    removeBuildings.add(building);
                }
            }
        }
        for (Enemy e: removeEnemies) {
            e.takeDamage(10);
            if (e.getEnemyCurHp() <= 0) killEnemy(e);
        }

        for (Building b: removeBuildings) {
            b.destroy();
            buildingEntities.remove(b);
        }
    }

    
    /**
     * Helper to get a random position on the path which is not occupied by a building or another HealthPotionDrop or GoldDrop
     * @return Pair<Integer, Integer> for coordinates of random empty path or null
     */
    private Pair<Integer, Integer> getRandomPositionOnPath() {
        Random random = new Random();

        if (isOrderedPathFull()) {
            return null;
        }

        while (true) {
            // OrderedPath[0] is HerosCastle so remove that possibility
            Pair<Integer, Integer> newPos = orderedPath.get(random.nextInt(orderedPath.size() - 1) + 1);
            if (!isTileOccupiedByCoordinates(newPos.getValue0(), newPos.getValue1()))
                return newPos;
        }
    }

    /**
     * helper to determine if orderedPath is full
     * @return boolean
     */
    private boolean isOrderedPathFull() {
        for (Pair<Integer, Integer> curr : orderedPath) {
            if (!isTileOccupiedByCoordinates(curr.getValue0().intValue(), curr.getValue1().intValue()))
                return false;
        }

        return true;
    }

    /**
     * helper to determine if the tile is occupied by either a building or goldDrop or healthPotionDrop
     * @return boolean
     */
    private boolean isTileOccupiedByCoordinates(int x, int y) {
        if (x == herosCastle.getX() && x == herosCastle.getY()) return true;
        for (Building building : buildingEntities) {
            if (x == building.getX() && y == building.getY()) {
                return true;
            }
        }

        for (HealthPotionDrop healthPotion : healthPotionDrops) {
            if (x == healthPotion.getX() && y == healthPotion.getY()) {
                return true;
            }
        }

        for (GoldDrop goldDrop : goldDrops) {
            if (x == goldDrop.getX() && y == goldDrop.getY()) {
                return true;
            }
        }

        return false;
    }

    public Battle getBattle() {
        return battle;
    }
    
    /**
     * Drastically increase the price of DoggieCoin
     * random chance of 200 - 500 increase in price
     */
    private void increaseDoggieCoinPrice() {
        int offset = (new Random()).nextInt(301) + 200;
        this.doggiePrice += offset;
    }

    /**
     * Causes the doggie coin to plummet in value.
     * Doggie coin price will drop to less than a quarter of its current value.
     */
    private void decreaseDoggieCoinPrice() {
        int lowBound = doggiePrice / 4;
        if (lowBound == 0) lowBound = 1;
        int newPrice = (new Random()).nextInt(lowBound) + 1; // price will be 1 + lower bound so its not 0.
        this.doggiePrice = newPrice;
    }
}
