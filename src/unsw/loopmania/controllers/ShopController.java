package unsw.loopmania.controllers;

import unsw.loopmania.*;
import unsw.loopmania.entity.*;
import unsw.loopmania.entity.notmoving.card.*;
import unsw.loopmania.entity.notmoving.item.*;
import unsw.loopmania.entity.moving.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.control.ProgressBar;
import javafx.util.converter.NumberStringConverter;

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
public class ShopController {

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
     * when we drag a card/item, the picture for whatever we're dragging is set here and we actually drag this node
     */
    private DragIcon draggedEntity;

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
    private Image andurilImage;
    private Image treeStumpImage;
    private Image healthPotionImage;
    private Image doggieCoinImage;

    private Map<String, Integer> trackingEquippedPositions;
    private Image emptyInventorySlotImage;
    private Image emptySwordImage;
    private Image emptyArmourImage;
    private Image emptyShieldImage;
    private Image emptyHelmetImage;
    private Image emptyTheOneRingImage;

    private List<Item> shopItems = new ArrayList<>();

    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;
    private LoopManiaWorldController controller;
    private LoopManiaWorld world;

    private Image stakeImage;

    private Image staffImage;
    private Label doggiePriceText;

    /**
     * @param controller controller of the game
     */
    public ShopController(LoopManiaWorldController controller) {
        this.controller = controller;
        
        entityImages = new ArrayList<>();
        vampireCastleCardImage = new Image((new File("src/images/vampire_castle.png")).toURI().toString());
        barracksCardImage = new Image((new File("src/images/barracks.jpeg")).toURI().toString());
        zombiePitCardImage = new Image((new File("src/images/zombie_pit.png")).toURI().toString());
        towerCardImage = new Image((new File("src/images/tower.png")).toURI().toString());
        villageCardImage = new Image((new File("src/images/village.jpeg")).toURI().toString());
        trapCardImage = new Image((new File("src/images/trap.png")).toURI().toString());
        campfireCardImage = new Image((new File("src/images/campfire.png")).toURI().toString());

        swordImage = new Image((new File("src/images/sword.png")).toURI().toString());
        stakeImage = new Image((new File("src/images/stake.png")).toURI().toString());
        staffImage = new Image((new File("src/images/staff.png")).toURI().toString());
        helmetImage = new Image((new File("src/images/helmet.png")).toURI().toString());
        shieldImage = new Image((new File("src/images/shield.png")).toURI().toString());
        armourImage = new Image((new File("src/images/armour.png")).toURI().toString());
        healthPotionImage = new Image((new File("src/images/healthPotion.png")).toURI().toString());
        doggieCoinImage = new Image((new File("src/images/doggiecoin_transparent_bg.png")).toURI().toString());
        theOneRingImage = new Image((new File("src/images/theOneRing.png")).toURI().toString());
        andurilImage = new Image((new File("src/images/anduril.png")).toURI().toString());
        treeStumpImage = new Image((new File("src/images/treeStump.png")).toURI().toString());

        emptyInventorySlotImage = new Image((new File("src/images/emptyInventorySlot.png")).toURI().toString());
        emptySwordImage = new Image((new File("src/images/emptySword.png")).toURI().toString());
        emptyHelmetImage = new Image((new File("src/images/emptyHelmet.png")).toURI().toString());
        emptyArmourImage = new Image((new File("src/images/emptyArmour.png")).toURI().toString());
        emptyShieldImage = new Image((new File("src/images/emptyShield.png")).toURI().toString());
        emptyTheOneRingImage = new Image((new File("src/images/emptyTheOneRing.png")).toURI().toString());

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

        NumberStringConverter converter = new NumberStringConverter();
        gold.textProperty().bindBidirectional(world.getCharacterGold(), converter);
        exp.textProperty().bindBidirectional(world.getCharacterExp(), converter);
        double h = (double)world.getCharacterHealth().getValue();
        double mh = (double)world.getCharacterMaxHealth().getValue();
        healthText.setText(world.getCharacterHealth().get() + "/" + world.getCharacterMaxHealth().get());
        healthBar.setProgress(h/mh);
        healthBar.setStyle("-fx-accent: red;"); 
        cycles.setText(world.getCycle() + "");
        level.textProperty().bindBidirectional(world.getCharacterLevel(), converter);
        Image shopImage = new Image((new File("src/images/shop_background.jpg")).toURI().toString());
        Image inventorySlotImage = new Image((new File("src/images/emptyInventorySlot.png")).toURI().toString());
        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);
        doggiePriceText = new Label();

        // Add the ground first so it is below all other entities (inculding all the twists and turns)
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 14; y++) {
                ImageView groundView = new ImageView(shopImage);
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        Button xBut = new Button();
        xBut.setText("X");
        xBut.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                switchToGame();
            }});
        squares.add(xBut, 7, 0);

        
        
        // add the ground underneath the cards
        for (int x=0; x<8; x++){
            ImageView groundView = new ImageView(shopImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        squares.add(doggiePriceText,3,0,7,2);
        ImageView doggieCoinImageView = new ImageView(doggieCoinImage);
        squares.add(doggieCoinImageView, 2, 0, 5, 2);
        
        // add the empty slot images for the unequipped inventory
        for (int x=0; x<Character.unequippedInventoryWidth; x++){
            for (int y=0; y<Character.unequippedInventoryHeight; y++){
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                emptySlotView.setFitWidth(32);
                emptySlotView.setFitHeight(32);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        Text shopText = new Text("Shop");
        shopText.setFont(new Font(22));
        squares.add(shopText,0,0,2,2);

        Text buyText = new Text("Buy");
        buyText.setFont(new Font(14));
        squares.add(buyText,0,1,2,1);

        Text sellTextInstructions = new Text("To buy, click an item above\nTo sell, click on an inventory item");
        sellTextInstructions.setFont(new Font(13));
        squares.add(sellTextInstructions,0,4,8,5);

        // Creating slots for buy section
        for (int x = 0; x < 8; x+=2) {
            for (int y = 2; y <= 4; y+=2) {
                ImageView emptySlotView = new ImageView(new Image((new File("src/images/emptyInventorySlot.png")).toURI().toString()));
                emptySlotView.setFitWidth(32);
                emptySlotView.setFitHeight(32);
                squares.add(emptySlotView, x, y);
            }
        }

        // creating an image of the shop prices
        ImageView shopItemsView = new ImageView(new Image((new File("src/images/shop_prices.PNG")).toURI().toString()));
        shopItemsView.setFitWidth(250);
        shopItemsView.setFitHeight(200);
        squares.add(shopItemsView, 0, 7, 10, 16);
        
        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);

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
    
    // Do nothing
    public void handleKeyPress(KeyEvent event) {}

    /**
     * starts and creates the shop interface
     */
    public void start(){
        this.world = controller.getWorld();
        shopItems.clear();

        cycles.setText(world.getCycle() + "");

        for (Item item: controller.getUnequippedInventory()) {
            onLoad(item);
        }
        
        for (Card item: controller.getCardEntities()) {
            onLoad(item);
        }
        

        for (int x = 0; x < 8; x+=2) {
            for (int y = 2; y <= 4; y+=2) {
                ImageView emptySlotView = new ImageView(emptyInventorySlotImage);
                emptySlotView.setFitWidth(32);
                emptySlotView.setFitHeight(32);
                squares.add(emptySlotView, x, y);
            }
        }

        double h = (double)world.getCharacterHealth().getValue();
        double mh = (double)world.getCharacterMaxHealth().getValue();
        healthBar.setProgress(h/mh);
        healthText.setText(world.getCharacterHealth().get() + "/" + world.getCharacterMaxHealth().get());
        healthText.setFill(Color.BLACK);

        List<Item> shopItems = world.getShop(); 
        
        for (Item item: shopItems) {
            addShopItem(item);
        }

        doggiePriceText.setText("Doggie Coin Value: " + world.getDoggiePrice());
        doggiePriceText.setFont(new Font(12));

        Character character = world.getCharacter();
        Map<String, Item> equippedItemsList = character.getEquippedItems();
        equippedItems.getChildren().clear();
        
        // equipped inventory loader
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
                    } else if (item.getType().equals("Staff")) {
                        img.setImage(staffImage);
                    } else if (item.getType().equals("Stake")) {
                        img.setImage(stakeImage);
                    } else if (item.getType().equals("Anduril")) {
                        img.setImage(andurilImage);
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
                    } else if (item.getType().equals("TreeStump")) {
                        img.setImage(treeStumpImage);
                    } else {
                        img.setImage(shieldImage);
                    }
                    equippedItems.add(img, 2, 1);
                    updateTrackingEquippedPositions("Shield");
                    break;
                default:
                    break;
            }
        }

    }


    /**
     * Add items to the shop
     */
    private void addShopItem(Item item) {
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
            case "Anduril":
                view = new ImageView(andurilImage);
                break;
            case "TreeStump":
                view = new ImageView(treeStumpImage);
                break;
        }

        view.setFitHeight(32);
        view.setFitWidth(32);
        addEntity(item, view);
        shopItems.add(item);
        squares.add(view, item.getX(), item.getY());
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

            Item item = null;
            for (Item it: shopItems) {
                if (it.getX() == colIndex && it.getY() == rowIndex) item = it;
            }

            if (item != null) {
                boolean bought = world.buyItem(item);

                if (bought) {
                    loadItem(item.getType());
                    printThreadingNotes("Bought " + item.getType());
                }

                // Resetting the shop
                for (int x = 0; x < 8; x+=2) {
                    for (int y = 2; y <= 4; y+=2) {
                        ImageView emptySlotView = new ImageView(emptyInventorySlotImage);
                        emptySlotView.setFitWidth(32);
                        emptySlotView.setFitHeight(32);
                        squares.add(emptySlotView, x, y);
                    }
                }
        
                List<Item> itemList = world.getCurrShop(); 

                for (Item e: itemList) {
                    addShopItem(e);
                }
            }            
        }
    }

    /**
     * Selling items to the shop
     * @pre has the item avaliable
     * @post destroy the item and give gold accordingly
     */
    public void clickInventory(javafx.scene.input.MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();
        if (clickedNode != unequippedInventory) {
            // click on descendant node
            int colIndex = GridPane.getColumnIndex(clickedNode);
            int rowIndex = GridPane.getRowIndex(clickedNode);
            Character character = world.getCharacter();
            Item item = character.getUnequippedInventoryItemByCoordinates(colIndex, rowIndex);
            
            if (item != null) {
                character.removeUnequippedInventoryItemByCoordinates(colIndex, rowIndex);
                world.getCharacter().addGold(item.getSellPrice());
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
        if (world.getMode().equals("Confusing"))
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
