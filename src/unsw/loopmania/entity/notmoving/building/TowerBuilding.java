package unsw.loopmania.entity.notmoving.building;


import javafx.beans.property.SimpleIntegerProperty;


/**
 * a basic form of building in the world
 */
public class TowerBuilding extends Building {
    public TowerBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setStrategy(new NotPath());
        this.setType("Tower");
    }

}
