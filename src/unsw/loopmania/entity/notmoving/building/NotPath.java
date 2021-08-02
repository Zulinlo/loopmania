package unsw.loopmania.entity.notmoving.building;

import java.util.List;

import org.javatuples.Pair;

public class NotPath implements BuildingPositionStrategy {

    @Override
    public boolean validPosition(int x, int y, List<Pair<Integer, Integer>> orderedPath) {
        Pair<Integer,Integer> pos = new Pair<Integer,Integer>(x, y);
        if (orderedPath.contains(pos)) return true;
        return false;
    }
    
}
