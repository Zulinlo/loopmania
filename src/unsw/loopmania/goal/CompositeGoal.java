package unsw.loopmania.goal;

public class CompositeGoal extends Goal {
    private Goal goal1;
    private Goal goal2;
    private String name;

    public CompositeGoal(Goal goal1, Goal goal2) {
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    public void setGoal1(Goal goal1) {
        this.goal1 = goal1;
    }

    public void setGoal2(Goal goal2) {
        this.goal2 = goal2;
    }

    @Override
    public boolean isGoalComplete(int gold, int exp,  int cycle, boolean boss) {
        if (name == "OR") {
            return goal1.isGoalComplete(gold, exp, cycle, boss) || goal2.isGoalComplete(gold, exp, cycle, boss);
        }
        else {
            return goal1.isGoalComplete(gold, exp, cycle, boss) && goal2.isGoalComplete(gold, exp, cycle, boss);
        }
    }
}
