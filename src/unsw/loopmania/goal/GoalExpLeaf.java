package unsw.loopmania.goal;

public class GoalExpLeaf extends Goal{
    private int expGoal;

    public GoalExpLeaf(int expGoal) {
        this.expGoal = expGoal;
    }

    @Override
    public boolean isGoalComplete(int gold, int exp, int cycles, boolean boss) {
        return exp >= expGoal;
    }
}
