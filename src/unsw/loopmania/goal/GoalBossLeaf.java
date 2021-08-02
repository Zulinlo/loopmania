package unsw.loopmania.goal;

public class GoalBossLeaf extends Goal {

    public GoalBossLeaf() {
    }

    public boolean isGoalComplete(int gold, int exp, int cycle, boolean boss) {
        return boss;
    }
}