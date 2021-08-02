package unsw.loopmania.controllers;

import unsw.loopmania.*;
import unsw.loopmania.entity.*;
import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.entity.moving.enemy.Enemy;
import unsw.loopmania.entity.notmoving.GoldDrop;
import unsw.loopmania.entity.notmoving.HealthPotionDrop;
import unsw.loopmania.entity.notmoving.building.*;
import unsw.loopmania.entity.notmoving.card.*;
import unsw.loopmania.entity.notmoving.item.*;

import java.util.ArrayList;
import java.util.List;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;


import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.io.File;
import java.io.IOException;


/**
 * the draggable types.
 * If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE{
    CARD,
    ITEM
}

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
public class LoopManiaWorldController {

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

    @FXML 
    private Text gold;

    @FXML 
    private Text exp;

    @FXML 
    private Text cycles;

    @FXML 
    private Text healthText = new Text();

    @FXML 
    private ProgressBar healthBar = new ProgressBar();


    @FXML 
    private Text level;

    @FXML
    private GridPane allies;
    


    // all image views including tiles, character, enemies, cards... even though cards in separate gridpane...
    private List<ImageView> entityImages;

    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here and we actually drag this node
     */
    private DragIcon draggedEntity;

    private boolean isPaused;
    private LoopManiaWorld world;

    /**
     * runs the periodic game logic - second-by-second moving of character through maze, as well as enemies, and running of battles
     */
    private Timeline timeline;

    private Image vampireCastleCardImage;
    private Image zombiePitCardImage;
    private Image towerCardImage;
    private Image villageCardImage;
    private Image trapCardImage;
    private Image campfireCardImage;
    private Image barracksCardImage;

    private Image vampireCastleBuildingImage;
    private Image barracksBuildingImage;
    private Image zombiePitBuildingImage;
    private Image towerBuildingImage;
    private Image villageBuildingImage;
    private Image trapBuildingImage;
    private Image campfireBuildingImage;

    private Image slugEnemyImage;
    private Image vampireEnemyImage;
    private Image zombieEnemyImage;
    private Image doggieEnemyImage;
    private Image elanMuskeEnemyImage;

    private Image emptyInventorySlotImage;
    private Image emptySwordImage;
    private Image emptyArmourImage;
    private Image emptyShieldImage;
    private Image emptyHelmetImage;
    private Image emptyTheOneRingImage;
    private Image swordImage;
    private Image stakeImage;
    private Image staffImage;
    private Image armourImage;
    private Image shieldImage;
    private Image helmetImage;
    private Image theOneRingImage;
    private Image andurilImage;
    private Image treeStumpImage;
    private Image healthPotionImage;
    private Image doggieCoinImage;

    private Image healthPotionDropImage;
    private Image goldDropImage;
    
    private Map<String, Integer> trackingEquippedPositions;

    private boolean hasFirstCycleOccurred = false;

    /**
     * the image currently being dragged, if there is one, otherwise null.
     * Holding the ImageView being dragged allows us to spawn it again in the drop location if appropriate.
     */
    private ImageView currentlyDraggedImage;
    
    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped over its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged over the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dropped in the background
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged into the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered when the draggable type is dragged outside of the boundaries of its appropriate gridpane
     */
    private EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;
    private ShopSwitcher shopSwitcher;
    private BattleSwitcher battleSwitcher;
    private MenuSwitcher congratulationSwitcher;
    private MenuSwitcher characterDiedSwitcher;

    private double speed;

    /**
     * @param world world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        entityImages = new ArrayList<>(initialEntities);
        vampireCastleCardImage = new Image((new File("src/images/vampire_castle.png")).toURI().toString());
        barracksCardImage = new Image((new File("src/images/barracks.jpeg")).toURI().toString());
        zombiePitCardImage = new Image((new File("src/images/zombie_pit.png")).toURI().toString());
        towerCardImage = new Image((new File("src/images/tower.png")).toURI().toString());
        villageCardImage = new Image((new File("src/images/village.jpeg")).toURI().toString());
        trapCardImage = new Image((new File("src/images/trap.png")).toURI().toString());
        campfireCardImage = new Image((new File("src/images/campfire.png")).toURI().toString());

        vampireCastleBuildingImage = new Image((new File("src/images/vampire_castle.png")).toURI().toString());
        barracksBuildingImage = new Image((new File("src/images/barracks.jpeg")).toURI().toString());
        zombiePitBuildingImage = new Image((new File("src/images/zombie_pit.png")).toURI().toString());
        towerBuildingImage = new Image((new File("src/images/tower.png")).toURI().toString());
        villageBuildingImage = new Image((new File("src/images/village.jpeg")).toURI().toString());
        trapBuildingImage = new Image((new File("src/images/trap.png")).toURI().toString());
        campfireBuildingImage = new Image((new File("src/images/campfire.png")).toURI().toString());

        slugEnemyImage = new Image((new File("src/images/slug.png")).toURI().toString());
        zombieEnemyImage = new Image((new File("src/images/zombie.png")).toURI().toString());
        vampireEnemyImage = new Image((new File("src/images/vampire.png")).toURI().toString());
        doggieEnemyImage = new Image((new File("src/images/doggie.png")).toURI().toString());
        elanMuskeEnemyImage = new Image((new File("src/images/ElanMuske.png")).toURI().toString());

        emptyInventorySlotImage = new Image((new File("src/images/emptyInventorySlot.png")).toURI().toString());
        emptySwordImage = new Image((new File("src/images/emptySword.png")).toURI().toString());
        emptyHelmetImage = new Image((new File("src/images/emptyHelmet.png")).toURI().toString());
        emptyArmourImage = new Image((new File("src/images/emptyArmour.png")).toURI().toString());
        emptyShieldImage = new Image((new File("src/images/emptyShield.png")).toURI().toString());
        emptyTheOneRingImage = new Image((new File("src/images/emptyTheOneRing.png")).toURI().toString());

        healthPotionDropImage = new Image((new File("src/images/healthPotionDrop.png")).toURI().toString());
        goldDropImage = new Image((new File("src/images/goldDrop.png")).toURI().toString());
        
        swordImage = new Image((new File("src/images/sword.png")).toURI().toString());
        stakeImage = new Image((new File("src/images/stake.png")).toURI().toString());
        staffImage = new Image((new File("src/images/staff.png")).toURI().toString());
        helmetImage = new Image((new File("src/images/helmet.png")).toURI().toString());
        shieldImage = new Image((new File("src/images/shield.png")).toURI().toString());
        armourImage = new Image((new File("src/images/armour.png")).toURI().toString());
        doggieCoinImage = new Image((new File("src/images/doggiecoin.png")).toURI().toString());
        healthPotionImage = new Image((new File("src/images/healthPotion.png")).toURI().toString());
        theOneRingImage = new Image((new File("src/images/theOneRing.png")).toURI().toString());
        andurilImage = new Image((new File("src/images/anduril.png")).toURI().toString());
        treeStumpImage = new Image((new File("src/images/treeStump.png")).toURI().toString());
        currentlyDraggedImage = null;
        currentlyDraggedType = null;
        trackingEquippedPositions = new HashMap<String, Integer>();
        trackingEquippedPositions.put("TheOneRing", Integer.valueOf(0));
        trackingEquippedPositions.put("Helmet", Integer.valueOf(1));
        trackingEquippedPositions.put("Weapon", Integer.valueOf(2));
        trackingEquippedPositions.put("Armour", Integer.valueOf(3));
        trackingEquippedPositions.put("Shield", Integer.valueOf(4));

        speed = 0.3;
        

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>>(DRAGGABLE_TYPE.class);
    }

    @FXML
    public void initialize() {
        Image grassImage = new Image((new File("src/images/grass.png")).toURI().toString());

        NumberStringConverter converter = new NumberStringConverter();
        gold.textProperty().bindBidirectional(world.getCharacterGold(), converter);
        exp.textProperty().bindBidirectional(world.getCharacterExp(), converter);
        level.textProperty().bindBidirectional(world.getCharacterLevel(), converter);
        double h = (double)world.getCharacterHealth().getValue();
        double mh = (double)world.getCharacterMaxHealth().getValue();
        healthBar.setProgress(h/mh);
        healthBar.setStyle("-fx-accent: red;"); 
        healthText.setText(world.getCharacterHealth().get() + "/" + world.getCharacterMaxHealth().get());
        healthText.setFill(Color.BLACK);
      

        // Add the ground first so it is below all other entities (inculding all the twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(grassImage);
                groundView.setFitHeight(32);
                groundView.setFitWidth(32);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages){
            squares.getChildren().add(entity);
        }
        
        // add the ground underneath the cards
        for (int x=0; x<world.getWidth(); x++){
            ImageView groundView = new ImageView(grassImage);
            groundView.setFitHeight(32);
            groundView.setFitWidth(32);
            cards.add(groundView, x, 0);
        }

        // add the empty slot images for the unequipped inventory
        for (int x = 0; x < Character.unequippedInventoryWidth; x++){
            for (int y = 0; y < Character.unequippedInventoryHeight; y++){
                ImageView emptySlotView = new ImageView(emptyInventorySlotImage);
                emptySlotView.setFitHeight(32);
                emptySlotView.setFitWidth(32);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);

    }

    /**
     * create and run the timer
     */
    public void startTimer() {
        isPaused = false;
        // trigger adding code to process main game logic to queue. JavaFX will target framerate of 0.3 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(speed), event -> {
            for (Card item: getCardEntities()) {
                onLoad(item);
            }

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

            double h = (double)world.getCharacterHealth().getValue();
            double mh = (double)world.getCharacterMaxHealth().getValue();
            healthBar.setProgress(h/mh);
            healthText.setText(world.getCharacterHealth().get() + "/" + world.getCharacterMaxHealth().get());
            cycles.setText(world.getCycle() + "");

            // Check if player won
            if (world.gameWon()) {
                pause();
                hasFirstCycleOccurred = false;
                congratulationSwitcher.switchMenu();
                trackingEquippedPositions.put("TheOneRing", Integer.valueOf(0));
                trackingEquippedPositions.put("Helmet", Integer.valueOf(1));
                trackingEquippedPositions.put("Weapon", Integer.valueOf(2));
                trackingEquippedPositions.put("Armour", Integer.valueOf(3));
                trackingEquippedPositions.put("Shield", Integer.valueOf(4));

                equippedItems.getChildren().clear();

                ImageView emptyTheOneRingView = new ImageView(new Image((new File("src/images/emptyTheOneRing.png")).toURI().toString()));
                emptyTheOneRingView.setFitHeight(32);
                emptyTheOneRingView.setFitWidth(32);
                equippedItems.add(emptyTheOneRingView, 0, 0);

                ImageView emptyHelmetView = new ImageView(new Image((new File("src/images/emptyHelmet.png")).toURI().toString()));
                emptyHelmetView.setFitHeight(32);
                emptyHelmetView.setFitWidth(32);
                equippedItems.add(emptyHelmetView, 1, 0);

                ImageView emptyWeaponView = new ImageView(new Image((new File("src/images/emptySword.png")).toURI().toString()));
                emptyWeaponView.setFitHeight(32);
                emptyWeaponView.setFitWidth(32);
                equippedItems.add(emptyWeaponView, 0, 1);

                ImageView emptyArmourView = new ImageView(new Image((new File("src/images/emptyArmour.png")).toURI().toString()));
                emptyArmourView.setFitHeight(32);
                emptyArmourView.setFitWidth(32);
                equippedItems.add(emptyArmourView, 1, 1);

                ImageView emptyShieldView = new ImageView(new Image((new File("src/images/emptyShield.png")).toURI().toString()));
                emptyShieldView.setFitHeight(32);
                emptyShieldView.setFitWidth(32);
                equippedItems.add(emptyShieldView, 2, 1);


                world.reset();
            }

            // Check if player died
            if (world.getCharacter().getCurHp() <= 0) {
                Item usedRing = world.getCharacter().useTheOneRing();
                // Check if player has theOneRing equipped
                if (usedRing == null) {
                    pause();
                    hasFirstCycleOccurred = false;
                    characterDiedSwitcher.switchMenu();

                    // Resetting state of equipped items (frontend)
                    trackingEquippedPositions.put("TheOneRing", Integer.valueOf(0));
                    trackingEquippedPositions.put("Helmet", Integer.valueOf(1));
                    trackingEquippedPositions.put("Weapon", Integer.valueOf(2));
                    trackingEquippedPositions.put("Armour", Integer.valueOf(3));
                    trackingEquippedPositions.put("Shield", Integer.valueOf(4));

                    equippedItems.getChildren().clear();

                    ImageView emptyTheOneRingView = new ImageView(new Image((new File("src/images/emptyTheOneRing.png")).toURI().toString()));
                    emptyTheOneRingView.setFitHeight(32);
                    emptyTheOneRingView.setFitWidth(32);
                    equippedItems.add(emptyTheOneRingView, 0, 0);

                    ImageView emptyHelmetView = new ImageView(new Image((new File("src/images/emptyHelmet.png")).toURI().toString()));
                    emptyHelmetView.setFitHeight(32);
                    emptyHelmetView.setFitWidth(32);
                    equippedItems.add(emptyHelmetView, 1, 0);

                    ImageView emptyWeaponView = new ImageView(new Image((new File("src/images/emptySword.png")).toURI().toString()));
                    emptyWeaponView.setFitHeight(32);
                    emptyWeaponView.setFitWidth(32);
                    equippedItems.add(emptyWeaponView, 0, 1);

                    ImageView emptyArmourView = new ImageView(new Image((new File("src/images/emptyArmour.png")).toURI().toString()));
                    emptyArmourView.setFitHeight(32);
                    emptyArmourView.setFitWidth(32);
                    equippedItems.add(emptyArmourView, 1, 1);

                    ImageView emptyShieldView = new ImageView(new Image((new File("src/images/emptyShield.png")).toURI().toString()));
                    emptyShieldView.setFitHeight(32);
                    emptyShieldView.setFitWidth(32);
                    equippedItems.add(emptyShieldView, 2, 1);


                    world.reset();
                } else {
                    // If player has TheOneRing equipped then revive with full hp
                    world.getCharacter().addHealth(9999);

                    // Update empty slot to include confusion mode where other rare items can have use ring effect
                    ImageView emptyImage = null;
                    switch (usedRing.getType()) {
                        case "TheOneRing":
                            emptyImage = new ImageView(emptyTheOneRingImage);
                            emptyImage.setFitHeight(32);
                            emptyImage.setFitWidth(32);

                            equippedItems.add(emptyImage, 0, 0);

                            updateTrackingEquippedPositions("TheOneRing");
                            break;
                        case "Anduril":
                            emptyImage = new ImageView(emptySwordImage);
                            emptyImage.setFitHeight(32);
                            emptyImage.setFitWidth(32);

                            equippedItems.add(emptyImage, 0, 1);

                            updateTrackingEquippedPositions("Weapon");
                            break;
                        case "TreeStump":
                            emptyImage = new ImageView(emptyShieldImage);
                            emptyImage.setFitHeight(32);
                            emptyImage.setFitWidth(32);

                            equippedItems.add(emptyImage, 1, 2);

                            updateTrackingEquippedPositions("Shield");
                            break;
                    }
                }
                // Clears allied soldier if game over
                allies.getChildren().clear();
                world.getCharacter().getAlliedSoldiers().clear();
            }

            int charX = world.getCharacter().getX();
            int charY = world.getCharacter().getY();
            
            

            if (world.newBattle()) {
                switchToBattle();
            }

            // Check if character has walked past dropped Gold or HealthPotion
            // HealthPotion spawns 1-2 times on walkable path (prioritised over gold)
            // 5-10 Gold spawns 1-3 times on walkable path
            for (HealthPotionDrop healthPotion : world.getHealthPotionDrops()) {
                if (healthPotion.getX() == charX && healthPotion.getY() == charY) {
                    loadItem("HealthPotion");

                    world.getHealthPotionDrops().remove(healthPotion);
                    healthPotion.destroy();
                    break;
                }
            }

            for (GoldDrop gold : world.getGoldDrops()) {
                if (gold.getX() == charX && gold.getY() == charY) {
                    world.getCharacter().addGold(gold.getGoldAmount());

                    world.getGoldDrops().remove(gold);
                    gold.destroy();
                    break;
                }
            }

            if (world.isNewCycle() && isTriangularNumber(world.getCycle())) {
                switchToShop();
            }

            if (world.isNewCycle() || !hasFirstCycleOccurred) {
                printThreadingNotes("CYCLE: " + world.getCycle());
                hasFirstCycleOccurred = true;
                
                List<Enemy> newEnemies = world.possiblySpawnEnemies();
                for (Enemy newEnemy: newEnemies){
                    onLoad(newEnemy);
                }
                
                Random random = new Random();

                for (int i = 0; i < random.nextInt(1) + 1; ++i) {
                    ImageView view = new ImageView(healthPotionDropImage);
                    view.setFitHeight(32);
                    view.setFitWidth(32);

                    HealthPotionDrop drop = world.spawnHealthPotionDropOnGround();
                    if (drop == null)
                        continue;

                    addEntity(drop, view);
                    squares.getChildren().add(view);
                }

                for (int i = 0; i < random.nextInt(2) + 1; ++i) {
                    ImageView view = new ImageView(goldDropImage);
                    view.setFitHeight(32);
                    view.setFitWidth(32);

                    GoldDrop drop = world.spawnGoldDropOnGround();
                    if (drop == null)
                        continue;
                    
                    addEntity(drop, view);
                    squares.getChildren().add(view);
                }
            }

            world.runTickMoves();

            printThreadingNotes("HANDLED TIMER");
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }


    public static boolean isTriangularNumber(int num) {
        int comp = 8*num+1;
        int t = (int) Math.sqrt(comp);
        if (t*t==comp) {
           return true;
        }
        return false;
   }

    /**
     * pause the execution of the game loop
     * the human player can still drag and drop items during the game pause
     */
    public void pause(){
        isPaused = true;
        printThreadingNotes("PAUSED");
        if (timeline != null)
            timeline.stop();
    }

    public void terminate(){
        pause();
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
     * load a card from the world, and pair it with an image in the GUI
     */
    private void loadCard(String type) {
        Card Card = world.loadCard(type);
        onLoad(Card);
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
            case "TheOneRing":
                item = world.getCharacter().addUnequippedItem("TheOneRing", isConfusingMode);
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

        onLoad(item);
    }

    /**
     * load a building card into the GUI depending on the type of card being passed.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the cards GridPane.
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
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(card, view);
        cards.getChildren().add(view);
    }

    /**
     * load an item into the GUI.
     * Particularly, we must connect to the drag detection event handler,
     * and load the image into the unequippedInventory GridPane.
     * @param item
     */
    public void onLoad(Item item) {
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
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        addEntity(item, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * load an enemy into the GUI
     * @param enemy
     */
    private void onLoad(Enemy enemy) {
        ImageView view = null;
        switch(enemy.getEnemyType()) {
            case "Slug":
                view = new ImageView(slugEnemyImage);
                break;
            case "Zombie":
                view = new ImageView(zombieEnemyImage);
                break;
            case "Vampire":
                view = new ImageView(vampireEnemyImage);
                break;
            case "Doggie":
                view = new ImageView(doggieEnemyImage);
                break;
            case "ElanMuske":
                view = new ImageView(elanMuskeEnemyImage);
                break;
            default: // defaults to slug image for easier testing
                view = new ImageView(slugEnemyImage);
                break;
        }
        addEntity(enemy, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     * @param building
     */
    private void onLoad(Building building){
        ImageView view = null;
        switch(building.getType()){
            case "Barracks":
                view = new ImageView(barracksBuildingImage);
                break;
            case "Campfire":
                view = new ImageView(campfireBuildingImage);
                break;
            case "Tower":
                view = new ImageView(towerBuildingImage);
                break;
            case "Trap":
                view = new ImageView(trapBuildingImage);
                break;
            case "VampireCastle":
                view = new ImageView(vampireCastleBuildingImage);
                break;
            case "ZombiePit":
                view = new ImageView(zombiePitBuildingImage);
                break;
            case "Village":
                view = new ImageView(villageBuildingImage);
                break;
        }
        addEntity(building, view);
        squares.getChildren().add(view);
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
        healthText.setText(world.getCharacterHealth().get() + "/" + world.getCharacterMaxHealth().get());
    }


    /**
     * when an item is equipped record all item's new positions
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
     * add drag event handlers for dropping into gridpanes, dragging over the background, dropping over the background.
     * These are not attached to invidual items such as swords/cards.
     * @param draggableType the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers(DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        gridPaneSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                /*
                 *you might want to design the application so dropping at an invalid location drops at the most recent valid location hovered over,
                 * or simply allow the card/item to return to its slot (the latter is easier, as you won't have to store the last valid drop location!)
                 */
                if (currentlyDraggedType == draggableType){
                    // https://bugs.openjdk.java.net/browse/JDK-8117019
                    // putting drop completed at start not making complete on VLAB...

                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != targetGridPane && db.hasImage()){

                        Integer cIndex = GridPane.getColumnIndex(node);
                        Integer rIndex = GridPane.getRowIndex(node);
                        int x = cIndex == null ? 0 : cIndex;
                        int y = rIndex == null ? 0 : rIndex;
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        ImageView image = new ImageView(db.getImage());

                        int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                        int nodeY = GridPane.getRowIndex(currentlyDraggedImage);
                        switch (draggableType){
                            case CARD:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);
                                Building newBuilding = convertCardToBuildingByCoordinates(nodeX, nodeY, x, y);
                                if (world.buildingPositionValid(newBuilding)) {
                                    String type = newBuilding.getType();
                                    loadCard(type); 
                                    draggedEntity.setVisible(false);
                                    draggedEntity.setMouseTransparent(false);
                                    // remove drag event handlers before setting currently dragged image to null
                                    currentlyDraggedImage = null;
                                    currentlyDraggedType = null;
                                    printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                                    event.setDropCompleted(true);
                                    event.consume();
                                    return;
                                }
                                onLoad(newBuilding);
                                break;
                            case ITEM:
                                removeDraggableDragEventHandlers(draggableType, targetGridPane);

                                if (world.getCharacter().getIsInBattle()) {
                                    draggedEntity.setVisible(false);
                                    draggedEntity.setMouseTransparent(false);
                                    printThreadingNotes("UNABLE TO EQUIP DURING BATTLE");
                                    event.setDropCompleted(true);
                                    event.consume();
                                    currentlyDraggedImage.setVisible(true);
                                    return;
                                }

                                // Player equips item, swaps if existing one is there
                                if (targetGridPane.equals(equippedItems) && sourceGridPane.equals(unequippedInventory)) {
                                    
                                    // Check if item from source matches the target equippable slot
                                    String equippedItemSlot = world.getCharacter().getEquippedItemSlotByCoordinates(x, y);
                                    String unequippedItemType = world.getCharacter().getUnequippedInventoryItemByCoordinates(nodeX, nodeY).getType();
                                    if (!(equippedItemSlot.equals(unequippedItemType) || (equippedItemSlot.equals("Shield") && unequippedItemType.equals("TreeStump")) || (equippedItemSlot.equals("Weapon") && (unequippedItemType.equals("Sword") || unequippedItemType.equals("Stake") || unequippedItemType.equals("Staff") || unequippedItemType.equals("Anduril"))))) {
                                        draggedEntity.setVisible(false);
                                        draggedEntity.setMouseTransparent(false);
                                        printThreadingNotes("UNABLE TO EQUIP ITEM IN THIS SLOT");
                                        event.setDropCompleted(true);
                                        event.consume();
                                        currentlyDraggedImage.setVisible(true);
                                        return;
                                    }

                                    // Remove item from inventory
                                    String sourceItemType = world.getCharacter().removeUnequippedInventoryItemByCoordinates(nodeX, nodeY);

                                    // If item is currently existing then add to inventory
                                    Item targetItem = world.getCharacter().getEquippedItemByCoordinates(x, y);
                                    if (targetItem != null) {
                                        // Move equipped item to inventory
                                        loadItem(targetItem.getType());
                                        targetItem.destroy();
                                    } else {
                                        // If empty slot then remove the empty slot image before placing into equipped
                                        // Remove item based on its position (not GridPane column, row index)
                                        equippedItems.getChildren().remove(Integer.parseInt(trackingEquippedPositions.get(equippedItemSlot).toString()));
                                        /*switch (equippedItemSlot) {
                                            case "Weapon":
                                                equippedItems.getChildren().remove(2);
                                                break;
                                            case "TheOneRing":
                                                equippedItems.getChildren().remove(0);
                                                break;
                                            case "Helmet":
                                                equippedItems.getChildren().remove(1);
                                                break;
                                            case "Armour":
                                                equippedItems.getChildren().remove(3);
                                                break;
                                            case "Shield":
                                                equippedItems.getChildren().remove(4);
                                                break;
                                        }*/
                                    }

                                    // Add removed item from inventory into equipped
                                    Item sourceItem = null;
                                    if (world.getMode().getMode().equals("Confusing")) {
                                        sourceItem = world.getCharacter().equipItemByType(sourceItemType, true);
                                    } else {
                                        sourceItem = world.getCharacter().equipItemByType(sourceItemType, false);
                                    }
                                    image.setFitWidth(32);
                                    image.setFitHeight(32);
                                    addDragEventHandlers(image, DRAGGABLE_TYPE.ITEM, equippedItems, unequippedInventory);
                                    addEntity(sourceItem, image);

                                    // Remove node before adding new node (so no overlap, important because of transparent images)
                                    switch (equippedItemSlot) {
                                        case "Weapon":
                                            equippedItems.add(image, 0, 1);
                                            updateTrackingEquippedPositions("Weapon");
                                            break;
                                        case "TheOneRing":
                                            equippedItems.add(image, 0, 0);
                                            updateTrackingEquippedPositions("TheOneRing");
                                            break;
                                        case "Helmet":
                                            equippedItems.add(image, 1, 0);
                                            updateTrackingEquippedPositions("Helmet");
                                            break;
                                        case "Armour":
                                            equippedItems.add(image, 1, 1);
                                            updateTrackingEquippedPositions("Armour");
                                            break;
                                        case "Shield":
                                            equippedItems.add(image, 2, 1);
                                            updateTrackingEquippedPositions("Shield");
                                            break;
                                    }

                                } else if (targetGridPane.equals(unequippedInventory) && sourceGridPane.equals(equippedItems)) {
                                    // If player is trying to unequip (drag equippableSlot to inventoryItems)
                                    // Remove item from equippedItems
                                    // Check if equippable slot is empty

                                    Item sourceItem = world.getCharacter().getEquippedItemByCoordinates(nodeX, nodeY);
                                    world.getCharacter().unequipItem(sourceItem);
                                    ImageView emptyImage = new ImageView(emptyInventorySlotImage);
                                    emptyImage.setFitHeight(32);
                                    emptyImage.setFitWidth(32);
                                    
                                    switch (world.getCharacter().getEquippedItemSlotByCoordinates(nodeX, nodeY)) {
                                        case "Weapon":
                                            emptyImage.setImage(emptySwordImage);
                                            equippedItems.add(emptyImage, 0, 1);
                                            updateTrackingEquippedPositions("Weapon");
                                            break;
                                        case "TheOneRing":
                                            emptyImage.setImage(emptyTheOneRingImage);
                                            equippedItems.add(emptyImage, 0, 0);
                                            updateTrackingEquippedPositions("TheOneRing");
                                            break;
                                        case "Helmet":
                                            emptyImage.setImage(emptyHelmetImage);
                                            equippedItems.add(emptyImage, 1, 0);
                                            updateTrackingEquippedPositions("Helmet");
                                            break;
                                        case "Armour":
                                            emptyImage.setImage(emptyArmourImage);
                                            equippedItems.add(emptyImage, 1, 1);
                                            updateTrackingEquippedPositions("Armour");
                                            break;
                                        case "Shield":
                                            emptyImage.setImage(emptyShieldImage);
                                            equippedItems.add(emptyImage, 2, 1);
                                            updateTrackingEquippedPositions("Shield");
                                            break;
                                    }

                                    // Add removed equippedItem to inventory
                                    loadItem(sourceItem.getType());
                                }
                                break;

                            default:
                                break;
                        }
                        
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                        printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                    }
                }
                event.setDropCompleted(true);
                // consuming prevents the propagation of the event to the anchorPaneRoot (as a sub-node of anchorPaneRoot, GridPane is prioritized)
                // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
                // to understand this in full detail, ask your tutor or read https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
                event.consume();
            }
        });

        // this doesn't fire when we drag over GridPane because in the event handler for dragging over GridPanes, we consume the event
        anchorPaneRootSetOnDragOver.put(draggableType, new EventHandler<DragEvent>(){
            // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
            @Override
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    if(event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()){
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
                if (currentlyDraggedType != null){
                    draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                event.consume();
            }
        });

        // this doesn't fire when we drop over GridPane because in the event handler for dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                if (currentlyDraggedType == draggableType){
                    //Data dropped
                    //If there is an image on the dragboard, read it and use it
                    Dragboard db = event.getDragboard();
                    Node node = event.getPickResult().getIntersectedNode();
                    if(node != anchorPaneRoot && db.hasImage()){
                        //Places at 0,0 - will need to take coordinates once that is implemented
                        currentlyDraggedImage.setVisible(true);
                        draggedEntity.setVisible(false);
                        draggedEntity.setMouseTransparent(false);
                        // remove drag event handlers before setting currently dragged image to null
                        removeDraggableDragEventHandlers(draggableType, targetGridPane);
                        
                        currentlyDraggedImage = null;
                        currentlyDraggedType = null;
                    }
                }
                //let the source know whether the image was successfully transferred and used
                event.setDropCompleted(true);
                event.consume();
            }
        });
    }

    /**
     * remove the card from the world, and spawn and return a building instead where the card was dropped
     * @param cardNodeX the x coordinate of the card which was dragged, from 0 to width-1
     * @param cardNodeY the y coordinate of the card which was dragged (in starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card, where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card, where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private Building convertCardToBuildingByCoordinates(int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        return world.convertCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * add drag event handlers to an ImageView
     * @param view the view to attach drag event handlers to
     * @param draggableType the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be dragged
     * @param targetGridPane the relevant gridpane to which the entity would be dragged to
     */
    private void addDragEventHandlers(ImageView view, DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane){
        view.setOnDragDetected(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can detect it...
                currentlyDraggedType = draggableType;

                //Drag was detected, start drap-and-drop gesture
                //Allow any transfer node
                Dragboard db = view.startDragAndDrop(TransferMode.MOVE);
    
                //Put ImageView on dragboard
                ClipboardContent cbContent = new ClipboardContent();
                cbContent.putImage(view.getImage());
                db.setContent(cbContent);
                view.setVisible(false);

                buildNonEntityDragHandlers(draggableType, sourceGridPane, targetGridPane);

                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                switch (draggableType){
                    case CARD:
                        draggedEntity.setImage(view.getImage());
                        break;
                    case ITEM:      
                        draggedEntity.setImage(view.getImage());
                        draggedEntity.setFitHeight(32);
                        draggedEntity.setFitWidth(32);
                        break;
                    default:
                        break;
                }
                
                draggedEntity.setVisible(true);
                draggedEntity.setMouseTransparent(true);
                draggedEntity.toFront();

                // IMPORTANT!!!
                // to be able to remove event handlers, need to use addEventHandler
                // https://stackoverflow.com/a/67283792
                targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
                anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

                for (Node n: targetGridPane.getChildren()){
                    // events for entering and exiting are attached to squares children because that impacts opacity change
                    // these do not affect visibility of original image...
                    // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
                    gridPaneNodeSetOnDragEntered.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = be more selective about whether highlighting changes - if it cannot be dropped in the location, the location shouldn't be highlighted!
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                            //The drag-and-drop gesture entered the target
                            //show the user that it is an actual gesture target
                                if(event.getGestureSource() != n && event.getDragboard().hasImage()){
                                    n.setOpacity(0.7);
                                }
                            }
                            event.consume();
                        }
                    });
                    gridPaneNodeSetOnDragExited.put(draggableType, new EventHandler<DragEvent>() {
                        // TODO = since being more selective about whether highlighting changes, you could program the game so if the new highlight location is invalid the highlighting doesn't change, or leave this as-is
                        public void handle(DragEvent event) {
                            if (currentlyDraggedType == draggableType){
                                n.setOpacity(1);
                            }
                
                            event.consume();
                        }
                    });
                    n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                    n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
                }
                event.consume();
            }
            
        });
    }

    /**
     * remove drag event handlers so that we don't process redundant events
     * this is particularly important for slower machines such as over VLAB.
     * @param draggableType either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane){
        // remove event handlers from nodes in children squares, from anchorPaneRoot, and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n: targetGridPane.getChildren()){
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
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
        case SPACE:
            if (isPaused){
                startTimer();
            }
            else{
                pause();
            }
            break;
        case H:
            useHealthPotion();
            break;
        case X:
            if (speed == 0.3) {
                speed = 0.07;
            } else {
                speed = 0.3;
            }
            pause();
            startTimer();
        default:
            break;
        }
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher){
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    public void setCongratulationSwitcher(MenuSwitcher congratulationSwitcher){
        this.congratulationSwitcher = congratulationSwitcher;
    }

    public void setCharacterDiedSwitcher(MenuSwitcher characterDiedSwitcher){
        this.characterDiedSwitcher = characterDiedSwitcher;
    }

    
    /**
     * this method is triggered when click button to go to main menu in FXML
     * @throws IOException
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        pause();
        mainMenuSwitcher.switchMenu();
    }

    public void setShopSwitcher(ShopSwitcher shopSwitcher){
        this.shopSwitcher = shopSwitcher;
    }

    public void setBattleSwitcher(BattleSwitcher battleSwitcher){
        this.battleSwitcher = battleSwitcher;
    }

    private void switchToShop() {
        pause();
        shopSwitcher.switchShop();
    }

    private void switchToBattle() {
        pause();
        battleSwitcher.switchBattle();
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

    public LoopManiaWorld getWorld() {
        return this.world;
    }

    public List<Item> getUnequippedInventory() {
        return this.world.getUnequippedItems();
    }

    public List<Card> getCardEntities() {
        return this.world.getCardEntities();
    }

    public Map<String, Integer> getTrackingEquippedPositions() {
        return trackingEquippedPositions;
    }


}
