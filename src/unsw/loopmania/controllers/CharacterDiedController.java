package unsw.loopmania.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import unsw.loopmania.MenuSwitcher;

public class CharacterDiedController {
    private MenuSwitcher MenuSwitcher;

    public void setMainMenuSwitcher(MenuSwitcher MenuSwitcher){
        this.MenuSwitcher = MenuSwitcher;
    }

    @FXML
    private void switchToMenu() throws IOException {
        MenuSwitcher.switchMenu();
    }
}
