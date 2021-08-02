package unsw.loopmania.entity.notmoving.building;

import unsw.loopmania.entity.notmoving.StaticEntity;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;

/**
 * a Building in the world
 * which doesn't move
 */
public abstract class Building extends StaticEntity {
    private BuildingPositionStrategy strategy;
    private String type;

    public Building(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public boolean canPlace(List<Pair<Integer, Integer>> orderedPath) {
        return strategy.validPosition(getX(), getY(), orderedPath);
    }

    public String getType() {
        return type;
    }

    protected void setType(String type) {
        this.type = type;
    }

    protected void setStrategy(BuildingPositionStrategy strategy) {
        this.strategy = strategy;
    }

}
