package unsw.loopmania.controllers;

import unsw.loopmania.*;

import java.io.IOException;
import javafx.fxml.FXML;


/**
 * controller for the instructions menu page
 */
public class InstructionMenuController {
    private MenuSwitcher menuSwitcher;

    public void setMainMenuSwitcher(MenuSwitcher menuSwitcher){
        this.menuSwitcher = menuSwitcher;
    }

    @FXML
    private void switchToMenu() throws IOException {
        menuSwitcher.switchMenu();
    }
}
