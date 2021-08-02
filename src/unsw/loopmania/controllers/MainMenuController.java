package unsw.loopmania.controllers;

import unsw.loopmania.*;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * controller for the main menu.
 */

public class MainMenuController {
    /**
     * facilitates switching to main game
     */
    private MenuSwitcher gameSwitcher;
    private MenuSwitcher instructionMenuSwitcher;
    private LoopManiaWorldController mainController;

    public MainMenuController(LoopManiaWorldController mainController) {
        this.mainController = mainController;
    }

    public void setGameSwitcher(MenuSwitcher gameSwitcher){
        this.gameSwitcher = gameSwitcher;
    }

    public void setInstructionMenuSwitcher(MenuSwitcher instructioMenuSwitcher) {
        this.instructionMenuSwitcher = instructioMenuSwitcher;
    }

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGameStandard() throws IOException {
        gameSwitcher.switchMenu();
        mainController.getWorld().setMode("Standard");
    }

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGameSurvival() throws IOException {
        gameSwitcher.switchMenu();
        mainController.getWorld().setMode("Survival");
    }

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGameBerserker() throws IOException {
        gameSwitcher.switchMenu();
        mainController.getWorld().setMode("Berserker");
    }

    /**
     * facilitates switching to main game upon button click
     * @throws IOException
     */
    @FXML
    private void switchToGameConfusing() throws IOException {
        gameSwitcher.switchMenu();
        mainController.getWorld().setMode("Confusing");
    }

    /**
     * facilitates switching to instruction menu upon button click
     * @throws IOException
     */
    @FXML
    private void switchToInstructionMenu() throws IOException {
        instructionMenuSwitcher.switchMenu();
    }
}
