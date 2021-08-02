package unsw.loopmania.controllers;

import unsw.loopmania.*;
import unsw.loopmania.entity.*;
import unsw.loopmania.entity.notmoving.card.*;
import unsw.loopmania.entity.notmoving.item.*;
import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.entity.moving.enemy.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.ProgressBar;

import java.util.HashMap;
import java.io.File;
import java.io.IOException;


/**
 * the draggable types.
 * If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */


/**
 * A JavaFX controller for the world.
 * 
 * All event handlers and the timeline in JavaFX run on the JavaFX application thread:
 *     https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 *     Note in https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html under heading "Threading", it specifies animation timelines are run in the application thread.
 * This means that the starter code does not need locks (mutexes) for resources shared between the timeline KeyFrame, and all of the  event handlers (including between different event handlers).
 * This will make the game easier for you to implement. However, if you add time-consuming processes to this, the game may lag or become choppy.
 * 
 * If you need to implement time-consuming processes, we recommend:
 *     using Task https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by itself or within a Service https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 * 
 *     Tasks ensure that any changes to public properties, change notifications for errors or cancellation, event handlers, and states occur on the JavaFX Application thread,
 *         so is a better alternative to using a basic Java Thread: https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm
 *     The Service class is used for executing/reusing tasks. You can run tasks without Service, however, if you don't need to reuse it.
 *
 * If you implement time-consuming processes in a Task or thread, you may need to implement locks on resources shared with the application thread (i.e. Timeline KeyFrame and drag Event handlers).
 * You can check whether code is running on the JavaFX application thread by running the helper method printThreadingNotes in this class.
 * 
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 * 
 * If you need to delay some code but it is not long-running, consider using Platform.runLater https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 *     This is run on the JavaFX application thread when it has enough time.
 */
public class BattleController {

    /**
     * squares gridpane includes path images, enemies, character, empty grass, buildings
     */
    @FXML
    private GridPane squares;

    /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML
    private GridPane cards;

    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot stretches over the entire game world,
     * so we can detect dragging of cards/items over this and accordingly update DragIcon coordinates
     */
    @FXML
    private AnchorPane anchorPaneRoot;

    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML
    private GridPane equippedItems;

    @FXML
    private GridPane unequippedInventory;

    // all image views including tiles, character, enemies, cards... even though cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * runs the periodic game logic - second-by-second moving of character through maze, as well as enemies, and running of battles
     */

    @FXML 
    private Text gold;

    @FXML 
    private Text exp;

    @FXML 
    private Text cycles;

    @FXML
    private Text healthText;

    @FXML 
    private ProgressBar healthBar = new ProgressBar();

    @FXML 
    private Text level;
    
    @FXML
    private GridPane allies;

    private Image vampireCastleCardImage;
    private Image barracksCardImage;
    private Image zombiePitCardImage;
    private Image towerCardImage;
    private Image villageCardImage;
    private Image trapCardImage;
    private Image campfireCardImage;

    private Image swordImage;
    private Image armourImage;
    private Image shieldImage;
    private Image helmetImage;
    private Image theOneRingImage;
    private Image healthPotionImage;
    private Image doggieCoinImage;

    private Battle battle;

    private Map<String, Integer> trackingEquippedPositions;
    private Image emptyInventorySlotImage;
    private Image emptySwordImage;
    private Image emptyArmourImage;
    private Image emptyShieldImage;
    private Image emptyHelmetImage;
    private Image emptyTheOneRingImage;

    private Image slugEnemyImage;
    private Image vampireEnemyImage;
    private Image zombieEnemyImage;

    private Image characterImage;

    private List<Enemy> enemiesToFight = new ArrayList<Enemy>();
    private List<Object> entities = new ArrayList<>();
    private boolean isHumanNormal;
    private int humanX, humanY;
    private Timeline attackTimeline;
    private String weapon;
    private Image andurilImage;
    private Image treeStumpImage;
    private Label text;

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;
    private LoopManiaWorldController controller;
    private LoopManiaWorld world;

    private Image stakeImage;
    private boolean selectStage;

    private Image staffImage;
    private Timeline timeline;
    private List<Button> actions;
    private ImageView charImg;

    private Image doggieEnemyImage;
    private Image elanMuskeEnemyImage;

    private String attack;
    private boolean battleWon;
    private boolean enemyAttackStage;
    private List<ProgressBar> enemyHealth;

    /**
     * @param controller controller of the game
     */
    public BattleController(LoopManiaWorldController controller) {
        this.controller = controller;
        
        entityImages = new ArrayList<>();
        enemyHealth = new ArrayList<>();

        characterImage = new Image((new File("src/images/human.png")).toURI().toString());

        vampireCastleCardImage = new Image((new File("src/images/vampire_castle.png")).toURI().toString());
        barracksCardImage = new Image((new File("src/images/barracks.jpeg")).toURI().toString());
        zombiePitCardImage = new Image((new File("src/images/zombie_pit.png")).toURI().toString());
        towerCardImage = new Image((new File("src/images/tower.png")).toURI().toString());
        villageCardImage = new Image((new File("src/images/village.jpeg")).toURI().toString());
        trapCardImage = new Image((new File("src/images/trap.png")).toURI().toString());
        campfireCardImage = new Image((new File("src/images/campfire.png")).toURI().toString());

        actions = new ArrayList<>();
        swordImage = new Image((new File("src/images/sword.png")).toURI().toString());
        stakeImage = new Image((new File("src/images/stake.png")).toURI().toString());
        staffImage = new Image((new File("src/images/staff.png")).toURI().toString());
        helmetImage = new Image((new File("src/images/helmet.png")).toURI().toString());
        shieldImage = new Image((new File("src/images/shield.png")).toURI().toString());
        armourImage = new Image((new File("src/images/armour.png")).toURI().toString());
        healthPotionImage = new Image((new File("src/images/healthPotion.png")).toURI().toString());
        theOneRingImage = new Image((new File("src/images/theOneRing.png")).toURI().toString());
        doggieCoinImage = new Image((new File("src/images/doggiecoin.png")).toURI().toString());
        andurilImage = new Image((new File("src/images/anduril.png")).toURI().toString());
        treeStumpImage = new Image((new File("src/images/treeStump.png")).toURI().toString());

        emptyInventorySlotImage = new Image((new File("src/images/emptyInventorySlot.png")).toURI().toString());
        emptySwordImage = new Image((new File("src/images/emptySword.png")).toURI().toString());
        emptyHelmetImage = new Image((new File("src/images/emptyHelmet.png")).toURI().toString());
        emptyArmourImage = new Image((new File("src/images/emptyArmour.png")).toURI().toString());
        emptyShieldImage = new Image((new File("src/images/emptyShield.png")).toURI().toString());
        emptyTheOneRingImage = new Image((new File("src/images/emptyTheOneRing.png")).toURI().toString());

        slugEnemyImage = new Image((new File("src/images/slug.png")).toURI().toString());
        zombieEnemyImage = new Image((new File("src/images/zombie.png")).toURI().toString());
        vampireEnemyImage = new Image((new File("src/images/vampire.png")).toURI().toString());
        doggieEnemyImage = new Image((new File("src/images/doggie.png")).toURI().toString());
        elanMuskeEnemyImage = new Image((new File("src/images/ElanMuske.png")).toURI().toString());

        trackingEquippedPositions = new HashMap<String, Integer>();
        trackingEquippedPositions.put("TheOneRing", Integer.valueOf(0));
        trackingEquippedPositions.put("Helmet", Integer.valueOf(1));
        trackingEquippedPositions.put("Weapon", Integer.valueOf(2));
        trackingEquippedPositions.put("Armour", Integer.valueOf(3));
        trackingEquippedPositions.put("Shield", Integer.valueOf(4));

        
    }

    @FXML
    public void initialize() {
        this.world = controller.getWorld();
        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);
        
        Image grassImage = new Image((new File("src/images/grass.png")).toURI().toString());

        NumberStringConverter converter = new NumberStringConverter();
        gold.textProperty().bindBidirectional(world.getCharacterGold(), converter);
        exp.textProperty().bindBidirectional(world.getCharacterExp(), converter);
        double h = (double)world.getCharacterHealth().getValue();
        double mh = (double)world.getCharacterMaxHealth().getValue();
        healthBar.setProgress(h/mh);
        healthBar.setStyle("-fx-accent: red;");
        cycles.setText(world.getCycle() + "");
        level.textProperty().bindBidirectional(world.getCharacterLevel(), converter);
        healthText.setText(world.getCharacterHealth().get() + "/" + world.getCharacterMaxHealth().get());
        healthText.setFill(Color.BLACK);
        Image inventorySlotImage = new Image((new File("src/images/emptyInventorySlot.png")).toURI().toString());

        // Add the ground first so it is below all other entities (inculding all the twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(grassImage);
                groundView.setFitHeight(32);
                groundView.setFitWidth(32);
                squares.add(groundView, x, y);
            }
        }

        // Add the ground first so it is below all other entities (inculding all the twists and turns)
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 14; y++) {
                ImageView groundView = new ImageView(grassImage);
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // add the ground underneath the cards
        for (int x=0; x<8; x++){
            ImageView groundView = new ImageView(grassImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        
        // add the empty slot images for the unequipped inventory
        for (int x=0; x<Character.unequippedInventoryWidth; x++){
            for (int y=0; y<Character.unequippedInventoryHeight; y++){
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                emptySlotView.setFitWidth(32);
                emptySlotView.setFitHeight(32);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        ImageView charImg = new ImageView(characterImage);
        charImg.setFitWidth(75);
        charImg.setFitHeight(75);
        squares.add(charImg, 1, 5, 4, 6);
        this.charImg = charImg;

        Text shopText = new Text("Battle");
        shopText.setFont(new Font(25));
        squares.add(shopText,0,0,4,2);

        Text actionText = new Text("Actions");
        actionText.setFont(new Font(17));
        squares.add(actionText,0,6,3,8);

        Text descriptionText = new Text("Hover Over Attacks for Descriptions");
        descriptionText.setFont(new Font(12));
        squares.add(descriptionText,0,13,8,13);
    }

    /**
     * Maintains index of positions for equipped items
     * @param equipmentSlot
     */
    private void updateTrackingEquippedPositions(String equipmentSlot) {
        int itemPosition = trackingEquippedPositions.get(equipmentSlot);
        // decrement all items subsequent to item
        for (Map.Entry<String, Integer> entry : trackingEquippedPositions.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            
            if (value.intValue() > itemPosition)
                trackingEquippedPositions.put(key, value - 1);
        }

        // move item to end 
        trackingEquippedPositions.put(equipmentSlot, 4);
    }
    
    /**
     * starts and creates the shop interface
     */
    public void start(){
        this.world = controller.getWorld();
        selectStage = false;

        cycles.setText(world.getCycle() + "");

        for (Object o: actions) {
            squares.getChildren().remove(o);
        }
        actions.clear();
        for (Object e: enemiesToFight) squares.getChildren().remove(e);
        enemiesToFight.clear();

        for (Object o: entities) {
            squares.getChildren().remove(o);
        }
        entities.clear();

        for (Item item: controller.getUnequippedInventory()) {
            onLoad(item);
        }
        
        for (Card item: controller.getCardEntities()) {
            onLoad(item);
        }

        Character character = world.getCharacter();
        Map<String, Item> equippedItemsList = character.getEquippedItems();
        equippedItems.getChildren().clear();
        
        // equipped inventory loader
        this.weapon = "Unarmed";
        for (Map.Entry<String, Item> entry : equippedItemsList.entrySet()) {
            String string = entry.getKey();
            Item item = entry.getValue();
            ImageView img = new ImageView(emptyInventorySlotImage);
            img.setFitWidth(32);
            img.setFitHeight(32);
            switch (string) {
                case "Weapon":
                    if (item == null) {
                        img.setImage(emptySwordImage);
                    } else if (item.getType().equals("Sword")) {
                        img.setImage(swordImage);
                        weapon = "Sword";
                    } else if (item.getType().equals("Staff")) {
                        img.setImage(staffImage);
                        weapon = "Staff";
                    } else if (item.getType().equals("Stake")) {
                        img.setImage(stakeImage);
                        weapon = "Stake";
                    } else if (item.getType().equals("Anduril")) {
                        img.setImage(andurilImage);
                        weapon = "Anduril";
                    }

                    equippedItems.add(img, 0, 1);
                    updateTrackingEquippedPositions("Weapon");
                    break;
                case "Helmet":
                    if (item == null) {
                        img.setImage(emptyHelmetImage);
                    } else if (item != null) {
                        img.setImage(helmetImage);
                    }
                    equippedItems.add(img, 1, 0);
                    updateTrackingEquippedPositions("Helmet");
                    break;
                case "TheOneRing":
                    if (item == null) {
                        img.setImage(emptyTheOneRingImage);
                    } else {
                        img.setImage(theOneRingImage);
                    }
                    equippedItems.add(img, 0, 0);
                    updateTrackingEquippedPositions("TheOneRing");
                    break;
                case "Armour":
                    if (item == null) {
                        img.setImage(emptyArmourImage);
                    } else {
                        img.setImage(armourImage);
                    }
                    equippedItems.add(img, 1, 1);
                    updateTrackingEquippedPositions("Armour");
                    break;
                case "Shield":
                    if (item == null) {
                        img.setImage(emptyShieldImage);
                    } else if (item.getType().equals("Shield")) {
                        img.setImage(shieldImage);
                    } else if (item.getType().equals("TreeStump")) {
                        img.setImage(treeStumpImage);
                    }
                    equippedItems.add(img, 2, 1);
                    updateTrackingEquippedPositions("Shield");
                    break;
                default:
                    break;
            }
        }
        chooseAction();
    }
        
    public void chooseAction() {
        squares.getChildren().remove(text);
        text = new Label();
        battleWon = false;
        selectStage = false;
        attack = "";
        for (Object o: actions) {
            squares.getChildren().remove(o);
        }
        actions.clear();
        for (Object e: enemiesToFight) squares.getChildren().remove(e);
        enemiesToFight.clear();
        for (Object o: entities) {
            squares.getChildren().remove(o);
        }
        entities.clear();

        allies.getChildren().clear();

        // Creating the allied soldier in the front end
        List<Integer> alliedSoldier = world.getCharacter().getAlliedSoldiers();
        int size = alliedSoldier.size();
        for (int i = 0; i < size; i++) {
            ImageView view = new ImageView(new Image((new File("src/images/alliedSoldier.png")).toURI().toString()));
            view.setFitHeight(50);
            view.setFitWidth(50);
            allies.add(view, i, 0);
        }
        
        battle = world.getBattle();
        for (Object o: enemyHealth) {
            squares.getChildren().remove(o);
        }
        enemyHealth.clear();

        if (battle.getEnemies().size() == 0) {
            List<Enemy> defeatedEnemies = world.endBattle();

            // Get enemy loot
            for (Enemy e: defeatedEnemies){
                if (e.getEnemyType() == "ElanMuske") {
                    world.setElanBeat();
                }
                if (e.getEnemyType() == "Doggie") {
                    world.setDoggieBeat();
                }
                reactToEnemyDefeat(e);
            }

            // Upon battle victory, have a 1% chance to get each of the rare items (can get more than 1 at once)
            if (new Random().nextInt(100) == 69) {
                loadItem("TheOneRing");
            }

            if (new Random().nextInt(100) == 69) {
                loadItem("Anduril");
            }

            if (new Random().nextInt(100) == 69) {
                loadItem("TreeStump");
            }

            Rectangle shape = new Rectangle();
            shape.setX(150.0f);
            shape.setY(75.0f);
            shape.setWidth(255.0f);
            shape.setHeight(110.0f);
            shape.setFill(Color.LIGHTGRAY);

            text = new Label("Battle Won!!\nPress 'x' to continue");
            squares.add(shape, 0, 10, 8, 12);
            squares.add(text, 0, 10, 8, 10);
            entities.add(shape);
            battleWon = true;

            startHeroJumpAnimation();
            return;
        }

        if (world.getCharacter().getCurHp() <= 0) switchToGame();

        double h = (double)world.getCharacterHealth().getValue();
        double mh = (double)world.getCharacterMaxHealth().getValue();
        healthBar.setProgress(h/mh);
        healthBar.setStyle("-fx-accent: red;");
        healthText.setText(world.getCharacterHealth().get() + "/" + world.getCharacterMaxHealth().get());

        int x = 5;
        int y = 2;
        if (battle.getEnemies().size() <= 3) {
            for (Enemy e: battle.getEnemies()) {
                ProgressBar hp1 = new ProgressBar();
                h = (double)e.getEnemyCurHp();
                mh = (double)e.getEnemyMaxHp();
                hp1.setMaxSize(32, 12);
                hp1.setProgress(h/mh);
                hp1.setStyle("-fx-accent: red;");
                squares.add(hp1, x, y-1);
                enemyHealth.add(hp1);

                ImageView view = new ImageView();
                switch (e.getEnemyType()) {
                    case "Doggie":
                        view = new ImageView(doggieEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "ElanMuske":
                        view = new ImageView(elanMuskeEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "Slug":
                        view = new ImageView(slugEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "Vampire":
                        view = new ImageView(vampireEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "Zombie":
                        view = new ImageView(zombieEnemyImage);
                        squares.add(view, x, y);
                        break;
                }
                x++;
                y++;
                entities.add(view);
            }
        } else if (battle.getEnemies().size() > 3) {
            for (int i = 0; i < 3; i++) {
                ProgressBar hp1 = new ProgressBar();
                h = (double)battle.getEnemies().get(i).getEnemyCurHp();
                mh = (double)battle.getEnemies().get(i).getEnemyMaxHp();
                hp1.setProgress(h/mh);
                hp1.setStyle("-fx-accent: red;");
                hp1.setMaxSize(32, 12);
                squares.add(hp1, x, y-1);
                enemyHealth.add(hp1);

                ImageView view = new ImageView();
                switch (battle.getEnemies().get(i).getEnemyType()) {
                    case "Doggie":
                        view = new ImageView(doggieEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "ElanMuske":
                        view = new ImageView(elanMuskeEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "Slug":
                        view = new ImageView(slugEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "Vampire":
                        view = new ImageView(vampireEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "Zombie":
                        view = new ImageView(zombieEnemyImage);
                        squares.add(view, x, y);
                        break;
                }
                x++;
                y++;
                entities.add(view);
            }
            x = 4;
            y = 3;
            for (int i = 3; i < battle.getEnemies().size(); i++) {
                ProgressBar hp1 = new ProgressBar();
                h = (double)battle.getEnemies().get(i).getEnemyCurHp();
                mh = (double)battle.getEnemies().get(i).getEnemyMaxHp();
                hp1.setProgress(h/mh);
                hp1.setStyle("-fx-accent: red;");
                hp1.setMaxSize(32, 12);
                squares.add(hp1, x, y-1);
                enemyHealth.add(hp1);
                ImageView view = new ImageView();
                switch (battle.getEnemies().get(i).getEnemyType()) {
                    case "Doggie":
                        view = new ImageView(doggieEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "ElanMuske":
                        view = new ImageView(elanMuskeEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "Slug":
                        view = new ImageView(slugEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "Vampire":
                        view = new ImageView(vampireEnemyImage);
                        squares.add(view, x, y);
                        break;
                    case "Zombie":
                        view = new ImageView(zombieEnemyImage);
                        squares.add(view, x, y);
                        break;
                }
                x++;
                y++;
                entities.add(view);
            }
        }
        // load buttons
        switch (weapon) {
            case "Sword":
                // Creating Slash Action Button
                // Basic Attack
                Button slash = new Button();
                slash.setText("Slash");
                slash.setMaxSize(130, 47);
                slash.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Slash");
                    }});
                slash.setTooltip(new Tooltip("DMG +5\nACCURACY 100%"));
                squares.add(slash, 0, 8, 4, 9);
                actions.add(slash);

                // Creating Stab Action Button
                // More dmg but chance to miss
                Button stab = new Button();
                stab.setMaxSize(130, 47);
                stab.setText("Strike\nPP "+ battle.getPP("Strike"));
                stab.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Strike");
                    }});
                stab.setTooltip(new Tooltip("DMG +8\nACCURACY 70%"));
                squares.add(stab, 4, 8, 7, 9);
                actions.add(stab);

                // Creating Heavy Slash Action Button
                // More AOE dmg but chance to miss
                Button heavySlash = new Button();
                heavySlash.setText("Heavy Slash\nPP "+ battle.getPP("Heavy Slash"));
                heavySlash.setMaxSize(130, 47);
                heavySlash.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Heavy Slash");
                    }});
                heavySlash.setTooltip(new Tooltip("AOE DMG +8\nACCURACY 40%"));
                squares.add(heavySlash, 0, 11, 4, 12);
                actions.add(heavySlash);
                break;

            case "Staff":
                // Creating Jab Action Button
                // Basic Attack
                Button jab = new Button();
                jab.setText("Jab");
                jab.setMaxSize(130, 47);
                jab.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Jab");
                    }});
                jab.setTooltip(new Tooltip("DMG +1\nACCURACY 100%"));
                squares.add(jab, 0, 8, 4, 9);
                actions.add(jab);

                // Creating Zap Action Button
                // More Damage but able to miss
                Button zap = new Button();
                zap.setMaxSize(130, 47);
                zap.setText("Zap\nPP " + battle.getPP("Zap"));
                zap.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Zap");
                    }});
                zap.setTooltip(new Tooltip("DMG +6\nACCURACY 70%"));
                squares.add(zap, 4, 8, 7, 9);
                actions.add(zap);

                // Creating Trance Action Button
                // No damage but trances
                Button trance = new Button();
                trance.setText("Trance\nPP " + battle.getPP("Trance"));
                trance.setMaxSize(130, 47);
                trance.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Trance");
                    }});
                trance.setTooltip(new Tooltip("Trance DMG 0\nACCURACY 30%\nTrance lasts 3 rounds"));
                squares.add(trance, 0, 11, 4, 12);
                actions.add(trance);
                break;
            case "Stake":
                // Creating Stab Action Button
                // Basic Attack
                Button stab1 = new Button();
                stab1.setText("Stab");
                stab1.setMaxSize(130, 47);
                stab1.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Stab");
                    }});
                stab1.setTooltip(new Tooltip("DMG +2\nACCURACY 100%"));
                squares.add(stab1, 0, 8, 4, 9);
                actions.add(stab1);

                // Creating Garlic Stab Action Button
                // Basic Attack but super effective to vamps
                Button gstab = new Button();
                gstab.setMaxSize(130, 47);
                gstab.setText("Garlic Stab\nPP "+ battle.getPP("Garlic Stab"));
                gstab.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Garlic Stab");
                    }});
                gstab.setTooltip(new Tooltip("To Vampires DMG +9 or DMG +3\nAccuracy 80%"));
                squares.add(gstab, 4, 8, 7, 9);
                actions.add(gstab);

                // Creating Nothing Action Button
                Button nothing = new Button();
                nothing.setText("");
                nothing.setMaxSize(130, 47);
                nothing.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                    }});
                squares.add(nothing, 0, 11, 4, 12);
                actions.add(nothing);
                break;
            case "Anduril":
                // Creating Slash Action Button
                // Basic Attack
                Button slash1 = new Button();
                slash1.setText("Swing");
                slash1.setMaxSize(130, 47);
                slash1.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Swing");
                    }});
                slash1.setTooltip(new Tooltip("DMG +10\nACCURACY 100%"));
                squares.add(slash1, 0, 8, 4, 9);
                actions.add(slash1);

                // Creating Bad Boy Action Button
                // Basic Attack but super effective to doge
                Button badBoy = new Button();
                badBoy.setMaxSize(130, 47);
                badBoy.setText("Bad Boy\nPP "+ battle.getPP("Bad Boy"));
                badBoy.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Bad Boy");
                    }});
                badBoy.setTooltip(new Tooltip("To Doggie DMG +30 or DMG +11\nAccuracy 80%"));
                squares.add(badBoy, 4, 8, 7, 9);
                actions.add(badBoy);

                // Creating Space Ex Action Button
                // Basic Attack but super effective to muske
                Button spaceEx = new Button();
                spaceEx.setText("Space Ex\nPP " + battle.getPP("Space Ex"));
                spaceEx.setMaxSize(130, 47);
                spaceEx.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Space Ex");
                    }});
                badBoy.setTooltip(new Tooltip("To Muske DMG +30 or DMG +11\nAccuracy 80%"));
                squares.add(spaceEx, 0, 11, 4, 12);
                actions.add(spaceEx);
                break;
            case "Unarmed":
                // Creating Slap Button
                // Basic Attack
                Button slap = new Button();
                slap.setText("Slap");
                slap.setMaxSize(130, 47);
                slap.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        doAction("Slap");
                    }});
                slap.setTooltip(new Tooltip("DMG +1\nACCURACY 100%"));
                squares.add(slap, 0, 8, 7, 9);
                actions.add(slap);

                // Creating Punch Action Button
                // More damage but can miss
                Button punch = new Button();
                punch.setMaxSize(130, 47);
                punch.setText("Punch");
                punch.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {  
                        doAction("Punch");
                    }});
                punch.setTooltip(new Tooltip("DMG +3\nACCURACY 70%"));
                squares.add(punch, 4, 8, 7, 9);
                actions.add(punch);

                // Creating Null Action Button
                Button nullButton = new Button();
                nullButton.setText("");
                nullButton.setMaxSize(130, 47);
                nullButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                    }});
                squares.add(nullButton, 0, 11, 4, 12);
                actions.add(nullButton);
                break;
        }

        Button healthPotion = new Button();
                healthPotion.setText("Heal");
                healthPotion.setMaxSize(130, 47);
                healthPotion.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent arg0) {
                        Rectangle shape = new Rectangle();
                        shape.setX(150.0f);
                        shape.setY(75.0f);
                        shape.setWidth(255.0f);
                        shape.setHeight(110.0f);
                        shape.setFill(Color.LIGHTGRAY);

                        text = new Label("The hero has used a health potion!\nPress 'x' to continue");

                        if (!world.getCharacter().hasHealthPotion()) {
                            text.setText("No Health Potions to use\nPress 'x' to continue");
                        }               
                        // if character is already on maximum health then do nothing
                        else if (world.getCharacter().isMaxHp()) {
                            text.setText("Hero is already max HP\nPress 'x' to continue");
                            
                        }
                        else {
                            // use a health potion and remove
                            text.setText("Restored 33% of max HP\nPress 'x' to continue");
                        }                        
                        squares.add(shape, 0, 10, 8, 12);

                        squares.add(text, 0, 10, 8, 10);

                        entities.add(shape);

                        useHealthPotion();
                        startHeroJumpAnimation();
                        enemyAttackStage = true; 
                    }});
                healthPotion.setTooltip(new Tooltip("Consumes Health Potion"));
                squares.add(healthPotion, 4, 11, 8, 12);
                actions.add(healthPotion);
    }
    
    /**
     * Creates a message box to say what action is done
     * @param action The action it is performing
     */
    public void doAction(String attack) {
        if (battle.actionHasPP(attack)) {
            Rectangle shape = new Rectangle();
            shape.setX(150.0f);
            shape.setY(75.0f);
            shape.setWidth(255.0f);
            shape.setHeight(110.0f);
            shape.setFill(Color.LIGHTGRAY);

            text = new Label("Select Target");
            squares.add(shape, 0, 10, 8, 12);
            squares.add(text, 0, 10, 8, 10);
            entities.add(shape);
            
            selectStage = true;
            this.attack = attack;
        }
    }

    /**
     * handle the pressing of keyboard keys.
     * Specifically, we should pause when pressing SPACE
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case X:
                if (battleWon) { 
                    pauseHuman();
                    squares.getChildren().remove(text);
                    switchToGame();
                } else if (enemyAttackStage) {
                    enemyAction();
                } else {
                    pauseHuman();
                    squares.getChildren().remove(text);
                    chooseAction();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 
     */
    public void startHeroAttackAnimation() {
        isHumanNormal = false;
        humanX = 1;
        humanY = 5;
        // trigger which will make the hero jump up and down
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            cycles.setText(world.getCycle() + "");
            ImageView characterImg = new ImageView(characterImage);
            characterImg.setFitWidth(75);
            characterImg.setFitHeight(75);
            if (isHumanNormal) {
                isHumanNormal = false;
                pauseHuman();
            } else {
                squares.getChildren().remove(this.charImg);
                humanX++;
                humanY--;
                squares.add(characterImg, humanX, humanY, humanX + 3, humanY + 1);
                if (humanX == 4) isHumanNormal = true;
                this.charImg = characterImg;
            }
            
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * 
     */
    public void startHeroJumpAnimation() {
        isHumanNormal = true;
        // trigger which will make the hero jump up and down
        attackTimeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            ImageView characterImg = new ImageView(characterImage);
            characterImg.setFitWidth(75);
            characterImg.setFitHeight(75);
            if (isHumanNormal) {
                squares.getChildren().remove(this.charImg);
                squares.add(characterImg, 1, 4, 4, 5);
                isHumanNormal = false;
            } else {
                squares.getChildren().remove(this.charImg);
                squares.add(characterImg, 1, 5, 4, 6);
                isHumanNormal = true;
            }
            this.charImg = characterImg;
        }));
        attackTimeline.setCycleCount(Animation.INDEFINITE);
        attackTimeline.play();
    }

    /**
     * pause the execution of the game loop
     * the human player can still drag and drop items during the game pause
     */
    public void pauseHuman(){
        printThreadingNotes("PAUSED");
        if (timeline != null)
            timeline.stop();
        if (attackTimeline != null)
            attackTimeline.stop();
        
        ImageView characterImg = new ImageView(characterImage);
        characterImg.setFitWidth(75);
        characterImg.setFitHeight(75);
        squares.getChildren().remove(this.charImg);
        squares.add(characterImg, 1, 5, 4, 6);
        isHumanNormal = true;
        this.charImg = characterImg;
    }

    /**
     * load a card from the world, and pair it with an image in the GUI
     */
    private void loadCard(String type) {
        Card Card = world.loadCard(type);
        onLoad(Card);
    }

    private void reactToEnemyDefeat(Enemy enemy){
        // react to character defeating an enemy, spawns cards and items depending on the enemy killed
        // Every item has a different drop chance for each enemy and items may not drop; card drops are also similar

        // For every enemy killed get the possible loot / item drops that can be obtained and add them to the inventory
        for (Map.Entry<String, Integer> entry : enemy.getLootDrops().entrySet()) {
            int dropChance = (new Random()).nextInt(100);
            boolean isDropped = dropChance < entry.getValue(); // Drops item if dropChance generated < value
            if (entry.getKey().equals("Sword") && isDropped) {
                loadItem("Sword");
            } else if (entry.getKey().equals("Stake") && isDropped) {
                loadItem("Stake");
            } else if (entry.getKey().equals("Staff") && isDropped) {
                loadItem("Staff");
            } else if (entry.getKey().equals("Armour") && isDropped) {
                loadItem("Armour");
            } else if (entry.getKey().equals("Shield") && isDropped) {
                loadItem("Shield");
            } else if (entry.getKey().equals("Helmet") && isDropped) {
                loadItem("Helmet");
            } else if (entry.getKey().equals("HealthPotion") && isDropped) {
                loadItem("HealthPotion");
            } else if (entry.getKey().equals("DoggieCoin") && isDropped) {
                loadItem("DoggieCoin");
            }
        }

        // Spawns building cards and adds them to card slots for the human player to use
        // Each enemy killed in battle will drop one building card
        // And each card type has an equal chance of dropping 
        // Note: since there are 7 card types, 1/7 chance for one certain card type of dropping for each enemy
        int cardToDrop = new Random().nextInt(7);
        switch (cardToDrop) {
            case 0:
                loadCard("VampireCastle");
                break;
            case 1:
                loadCard("ZombiePit");
                break;
            case 2:
                loadCard("Tower");
                break;
            case 3:
                loadCard("Village");
                break;
            case 4:
                loadCard("Barracks");
                break;
            case 5:
                loadCard("Trap");
                break;
            case 6:
                loadCard("Campfire");
                break;
            default:
                break;
        }

        // Adds character's xp and gold obtained from killing the enemy
        world.getCharacter().addExp(enemy.getXpDrop());
        world.getCharacter().addGold(enemy.getGoldDrop());
    }
    
    private void enemyAction() {
        enemyAttackStage = false;
        squares.getChildren().remove(text);
        text = new Label();
        String ret = battle.enemyAction();
        text.setText(ret + (ret.isEmpty() ? "" : "\n") + "Press 'x' to continue");
        squares.add(text, 0, 10, 8, 10);
        if (world.getCharacter().getCurHp() <= 0) switchToGame();
    }

    /**
     * Use a Health Potion, if none in inventory then do nothing
     */
    private void useHealthPotion() {
        // if no health potions in inventory do nothing
        if (!world.getCharacter().hasHealthPotion()) {
            printThreadingNotes("NO HEALTH POTIONS TO USE");
            return;
        }

        // if character is already on maximum health then do nothing
        if (world.getCharacter().isMaxHp()) {
            printThreadingNotes("CHARACTER IS ALREADY MAX HP");
            return;
        }

        // use a health potion and remove
        printThreadingNotes("RESTORED 33% OF MAX HP");
        world.getCharacter().useAHealthPotion();
        double h = (double)world.getCharacterHealth().getValue();
        double mh = (double)world.getCharacterMaxHealth().getValue();
        healthBar.setProgress(h/mh);
        healthBar.setStyle("-fx-accent: red;");
        healthText.setText(world.getCharacterHealth().get() + "/" + world.getCharacterMaxHealth().get());
    }

    /**
     * Buying items from the shop
     * @pre has more or equal to 0 Gold
     * @post has more or equal to 0 Gold
     */
    public void clickGrid(javafx.scene.input.MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != squares) {
            // click on descendant node
            int colIndex;
            int rowIndex;

            try {
                colIndex = GridPane.getColumnIndex(clickedNode);
                rowIndex = GridPane.getRowIndex(clickedNode);
            } catch (NullPointerException e) {
                return;
            }
            
            if (!selectStage) return;
            
            if (colIndex == 5 && rowIndex == 2) {
                squares.getChildren().remove(text);
                battle.setTargetEnemy(battle.getEnemies().get(0));
                startHeroAttackAnimation();
                text = new Label();
                text.setText(battle.characterAction(attack) + "\nPress 'x' to continue");
                squares.add(text, 0, 10, 8, 10);
                selectStage = false;
                enemyAttackStage = true;
            } else if (colIndex == 6 && rowIndex == 3 && battle.getEnemies().size() > 1) {
                squares.getChildren().remove(text);
                battle.setTargetEnemy(battle.getEnemies().get(1));
                startHeroAttackAnimation();
                squares.getChildren().remove(text);
                text = new Label();
                text.setText(battle.characterAction(attack) + "\nPress 'x' to continue");
                squares.add(text, 0, 10, 8, 10);
                selectStage = false;
                enemyAttackStage = true;
            } else if (colIndex == 7 && rowIndex == 4 && battle.getEnemies().size() > 2) {
                squares.getChildren().remove(text);
                battle.setTargetEnemy(battle.getEnemies().get(2));
                startHeroAttackAnimation();
                squares.getChildren().remove(text);
                text = new Label();
                text.setText(battle.characterAction(attack) + "\nPress 'x' to continue");
                squares.add(text, 0, 10, 8, 10);
                selectStage = false;
                enemyAttackStage = true;
            } else if (colIndex == 4 && rowIndex == 3 && battle.getEnemies().size() > 3) {
                squares.getChildren().remove(text);
                battle.setTargetEnemy(battle.getEnemies().get(3));
                startHeroAttackAnimation();
                squares.getChildren().remove(text);
                text = new Label();
                text.setText(battle.characterAction(attack) + "\nPress 'x' to continue");
                squares.add(text, 0, 10, 8, 10);
                selectStage = false;
                enemyAttackStage = true;
            } else if (colIndex == 5 && rowIndex == 4 && battle.getEnemies().size() > 4) {
                squares.getChildren().remove(text);
                battle.setTargetEnemy(battle.getEnemies().get(4));
                startHeroAttackAnimation();
                squares.getChildren().remove(text);
                text = new Label();
                text.setText(battle.characterAction(attack) + "\nPress 'x' to continue");
                squares.add(text, 0, 10, 8, 10);
                selectStage = false;
                enemyAttackStage = true;
            } else if (colIndex == 6 && rowIndex == 4 && battle.getEnemies().size() > 5) {
                squares.getChildren().remove(text);
                battle.setTargetEnemy(battle.getEnemies().get(5));
                startHeroAttackAnimation();
                squares.getChildren().remove(text);
                text = new Label();
                text.setText(battle.characterAction(attack) + "\nPress 'x' to continue");
                squares.add(text, 0, 10, 8, 10);
                selectStage = false;
                enemyAttackStage = true;
            }
        }
    }

    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     * @param entity backend entity to be paired with view
     * @param view frontend imageview to be paired with backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entityImages.add(view);
    }

    /**
     * load an item into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * @param item
     */
    private void onLoad(Item item) {
        ImageView view = null;
        switch (item.getType()) {
            case "Sword":
                view = new ImageView(swordImage);
                break;
            case "Stake":
                view = new ImageView(stakeImage);
                break;
            case "Staff":
                view = new ImageView(staffImage);
                break;
            case "TheOneRing":
                view = new ImageView(theOneRingImage);
                break;
            case "HealthPotion":
                view = new ImageView(healthPotionImage);
                break;
            case "Shield":
                view = new ImageView(shieldImage);
                break;
            case "Armour":
                view = new ImageView(armourImage);
                break;
            case "Helmet":
                view = new ImageView(helmetImage);
                break;
            case "DoggieCoin":
                view = new ImageView(doggieCoinImage);
                break;
            case "Anduril":
                view = new ImageView(andurilImage);
                break;
            case "TreeStump":
                view = new ImageView(treeStumpImage);
        }

        view.setFitHeight(32);
        view.setFitWidth(32);
        addEntity(item, view);
        unequippedInventory.getChildren().add(view);
    }



    /**
     * load a cards into the GUI.
     * @param Card
     */
    private void onLoad(Card card) {
        ImageView view = null;
        switch(card.getType()){
            case "Barracks":
                view = new ImageView(barracksCardImage);
                break;
            case "Campfire":
                view = new ImageView(campfireCardImage);
                break;
            case "Tower":
                view = new ImageView(towerCardImage);
                break;
            case "Trap":
                view = new ImageView(trapCardImage);
                break;
            case "VampireCastle":
                view = new ImageView(vampireCastleCardImage);
                break;
            case "ZombiePit":
                view = new ImageView(zombiePitCardImage);
                break;
            case "Village":
                view = new ImageView(villageCardImage);
                break;
        }

        // FROM https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method

        addEntity(card, view);
        cards.getChildren().add(view);
    }

    /**
     * load an inventory item from the world, and pair it with an image in the GUI
     */
    private void loadItem(String type){
        Item item = null;

        // Check if confusing mode to create a compound item if it is a rare item
        boolean isConfusingMode = false;
        if (world.getMode().getMode().equals("Confusing"))
            isConfusingMode = true;

        switch (type) {
            case "Sword":
                item = world.getCharacter().addUnequippedItem("Sword", isConfusingMode);
                break;
            case "Stake":
                item = world.getCharacter().addUnequippedItem("Stake", isConfusingMode);
                break;
            case "Staff":
                item = world.getCharacter().addUnequippedItem("Staff", isConfusingMode);
                break;
            case "TheOneRing":
                item = world.getCharacter().addUnequippedItem("TheOneRing", isConfusingMode);
                break;
            case "HealthPotion":
                item = world.getCharacter().addUnequippedItem("HealthPotion", isConfusingMode);
                break;
            case "Shield":
                item = world.getCharacter().addUnequippedItem("Shield", isConfusingMode);
                break;
            case "Armour":
                item = world.getCharacter().addUnequippedItem("Armour", isConfusingMode);
                break;
            case "Helmet":
                item = world.getCharacter().addUnequippedItem("Helmet", isConfusingMode);
                break;
            case "Anduril":
                item = world.getCharacter().addUnequippedItem("Anduril", isConfusingMode);
                break;
            case "TreeStump":
                item = world.getCharacter().addUnequippedItem("TreeStump", isConfusingMode);
                break;
            case "DoggieCoin":
                item = world.getCharacter().addUnequippedItem("DoggieCoin", isConfusingMode);
                break;
        }
        controller.onLoad(item);
        onLoad(item);
    }

    private MenuSwitcher gameSwitcher;

    public void setGameSwitcher(MenuSwitcher gameSwitcher){
        this.gameSwitcher = gameSwitcher;
    }

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGame() {
        gameSwitcher.switchMenu();
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher){
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        pauseHuman();
        mainMenuSwitcher.switchMenu();
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the world.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * 
     * note that this is put in the controller rather than the loader because we need to track positions of spawned entities such as enemy
     * or items which might need to be removed should be tracked here
     * 
     * NOTE teardown functions setup here also remove nodes from their GridPane. So it is vital this is handled in this Controller class
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        ChangeListener<Number> xListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        };
        ChangeListener<Number> yListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        };

        // if need to remove items from the equipped inventory, add code to remove from equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX = ListenerHandles.createFor(entity.x(), node)
                                               .onAttach((o, l) -> o.addListener(xListener))
                                               .onDetach((o, l) -> {
                                                    o.removeListener(xListener);
                                                    entityImages.remove(node);
                                                    squares.getChildren().remove(node);
                                                    cards.getChildren().remove(node);
                                                    equippedItems.getChildren().remove(node);
                                                    unequippedInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        ListenerHandle handleY = ListenerHandles.createFor(entity.y(), node)
                                               .onAttach((o, l) -> o.addListener(yListener))
                                               .onDetach((o, l) -> {
                                                   o.removeListener(yListener);
                                                   entityImages.remove(node);
                                                   squares.getChildren().remove(node);
                                                   cards.getChildren().remove(node);
                                                   equippedItems.getChildren().remove(node);
                                                   unequippedInventory.getChildren().remove(node);
                                                })
                                               .buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here, position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
                handleX.detach();
                handleY.detach();
            }
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is running on the application thread.
     * By running everything on the application thread, you will not need to worry about implementing locks, which is outside the scope of the course.
     * Always writing code running on the application thread will make the project easier, as long as you are not running time-consuming tasks.
     * We recommend only running code on the application thread, by using Timelines when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel){
        System.out.println("\n###########################################");
        System.out.println("current method = "+currentMethodLabel);
        System.out.println("In application thread? = "+Platform.isFxApplicationThread());
        System.out.println("Current system time = "+java.time.LocalDateTime.now().toString().replace('T', ' '));
    }
}
