package unsw.loopmania.goal;

public class GoalGoldLeaf extends Goal {
    private int goldGoal;

    public GoalGoldLeaf(int goldGoal) {
        this.goldGoal = goldGoal;
    }

    public boolean isGoalComplete(int gold, int exp,  int cycle, boolean boss) {
        return gold >= goldGoal;
    }

}
