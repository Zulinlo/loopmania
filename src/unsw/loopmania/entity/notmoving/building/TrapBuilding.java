package unsw.loopmania.entity.notmoving.building;


import javafx.beans.property.SimpleIntegerProperty;

/**
 * a basic form of building in the world
 */
public class TrapBuilding extends Building {

    public TrapBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.setStrategy(new PathOnly());
        this.setType("Trap");
    }

}
