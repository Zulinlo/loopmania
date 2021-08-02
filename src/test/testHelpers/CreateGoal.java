package test.testHelpers;



import unsw.loopmania.goal.CompositeGoal;
import unsw.loopmania.goal.Goal;
import unsw.loopmania.goal.GoalBossLeaf;
import unsw.loopmania.goal.GoalExpLeaf;
import unsw.loopmania.goal.GoalGoldLeaf;

public class CreateGoal {



    public CreateGoal() {}

    public Goal DummyGoal() {
        Goal goal = new GoalGoldLeaf(10000);
        return goal;
    }

    public Goal actualGoal() {
        Goal gold = new GoalGoldLeaf(10000);
        Goal exp = new GoalExpLeaf(406);
        Goal bosses = new GoalBossLeaf();
        Goal andGoal = new CompositeGoal(gold, exp);
        Goal finalGoal = new CompositeGoal(andGoal, bosses);
        return finalGoal;
    }

   
   
       
       
}
