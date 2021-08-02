package unsw.loopmania.entity.notmoving.building;

import java.util.List;

import org.javatuples.Pair;

public interface BuildingPositionStrategy {
    public boolean validPosition(int x, int y, List<Pair<Integer, Integer>> orderedPath);
}
