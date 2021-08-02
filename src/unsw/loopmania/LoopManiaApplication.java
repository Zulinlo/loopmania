package unsw.loopmania;

import unsw.loopmania.controllers.*;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * the main application
 * run main method from this class
 */
public class LoopManiaApplication extends Application {
    /**
     * the controller for the game. Stored as a field so can terminate it when click exit button
     */
    private LoopManiaWorldController mainController;


    @Override
    public void start(Stage primaryStage) throws IOException {

        // set title on top of window bar
        primaryStage.setTitle("Loop Mania");

        // prevent human player resizing game window (since otherwise would see white space)
        // alternatively, you could allow rescaling of the game (you'd have to program resizing of the JavaFX nodes)
        primaryStage.setResizable(false);

        // load the main game
        LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader("world_with_twists_and_turns.json");
        mainController = loopManiaLoader.loadController();
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("view/LoopManiaView.fxml"));
        gameLoader.setController(mainController);
        Parent gameRoot = gameLoader.load();

        // load the main menu
        MainMenuController mainMenuController = new MainMenuController(mainController);
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("view/MainMenuView.fxml"));
        menuLoader.setController(mainMenuController);
        Parent mainMenuRoot = menuLoader.load();

        // load the shop
        ShopController shopController = new ShopController(mainController);
        FXMLLoader shopLoader = new FXMLLoader(getClass().getResource("view/ShopView.fxml"));
        shopLoader.setController(shopController);
        Parent shopRoot = shopLoader.load();

        // load the Battle
        BattleController battleController = new BattleController(mainController);
        FXMLLoader battleLoader = new FXMLLoader(getClass().getResource("view/BattleView.fxml"));
        battleLoader.setController(battleController);
        Parent battleRoot = battleLoader.load();

        // load Congratulations
        CongratulationController congratulationController = new CongratulationController();
        FXMLLoader congratulationLoader = new FXMLLoader(getClass().getResource("view/CongratulationsView.fxml"));
        congratulationLoader.setController(congratulationController);
        Parent congratulationRoot = congratulationLoader.load();

        // load Character Died window
        CharacterDiedController characterDiedController = new CharacterDiedController();
        FXMLLoader characterDiedLoader = new FXMLLoader(getClass().getResource("view/CharacterDiedView.fxml"));
        characterDiedLoader.setController(characterDiedController);
        Parent characterDiedRoot = characterDiedLoader.load();

        // load Instruction Menu window
        InstructionMenuController instructionMenuController = new InstructionMenuController();
        FXMLLoader instructionMenuLoader = new FXMLLoader(getClass().getResource("view/InstructionMenuView.fxml"));
        instructionMenuLoader.setController(instructionMenuController);
        Parent instructionMenuRoot = instructionMenuLoader.load();


        // create new scene with the main menu (so we start with the main menu)
        Scene scene = new Scene(mainMenuRoot);
        
        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main menu
        mainController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
        mainMenuController.setInstructionMenuSwitcher(() -> {switchToRoot(scene, instructionMenuRoot, primaryStage);});
        mainMenuController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });
        
        shopController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
        mainController.setShopSwitcher(() -> {
            switchToRoot(scene, shopRoot, primaryStage);
            shopController.start();
        });

        battleController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
        mainController.setBattleSwitcher(() -> {
            switchToRoot(scene, battleRoot, primaryStage);
            battleController.start();
        });
        battleController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });
        
        mainController.setCongratulationSwitcher(() -> {switchToRoot(scene, congratulationRoot, primaryStage);});
        mainController.setCharacterDiedSwitcher(() -> {switchToRoot(scene, characterDiedRoot, primaryStage);});
        congratulationController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});
        characterDiedController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});

        instructionMenuController.setMainMenuSwitcher(() -> {switchToRoot(scene, mainMenuRoot, primaryStage);});

        shopController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });

        // deploy the main onto the stage
        gameRoot.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    @Override
    public void stop(){
        // wrap up activities when exit program
        mainController.terminate();
    }

    /**
     * switch to a different Root
     */
    private void switchToRoot(Scene scene, Parent root, Stage stage){
        scene.setRoot(root);
        root.requestFocus();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
