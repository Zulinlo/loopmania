package unsw.loopmania.goal;

public class GoalCyclesLeaf extends Goal {
    private int cycleGoal;

    public GoalCyclesLeaf(int cycleGoal) {
        this.cycleGoal = cycleGoal;
    }

    @Override
    public boolean isGoalComplete(int gold, int exp, int cycle, boolean boss) {
        return cycle >= cycleGoal;
    }
}
