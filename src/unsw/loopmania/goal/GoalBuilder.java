package unsw.loopmania.goal;

import org.json.JSONArray;
import org.json.JSONObject;


public class GoalBuilder {
    private static String AND="AND";
    private static String OR="OR";
    private static String GOLD="gold";
    private static String EXP="experience";
    private static String CYCLE="cycles";
    private static GoalBuilder INSTANCE;


    private GoalBuilder() {}

    public static GoalBuilder getInstance(){
        if(INSTANCE == null){
            INSTANCE= new GoalBuilder();
        }

        return INSTANCE;
    }

    public Goal build(JSONObject obj){
        String root = obj.getString("goal");
        // System.out.println(root);

        if(isComposite(root)){
            JSONArray subg = obj.getJSONArray("subgoals");
            CompositeGoal g = buildCompositeGoal(subg);
            return g;
        } 
        else {
            if (root.equalsIgnoreCase(GOLD)) {
                return new GoalGoldLeaf(((Integer) obj.get("quantity")));
            }
            if (root.equalsIgnoreCase(EXP)) {
                return new GoalExpLeaf(((Integer) obj.get("quantity")));
            }
            if (root.equalsIgnoreCase(CYCLE)) {
                return new GoalCyclesLeaf(((Integer) obj.get("quantity")));
            }
            else {
                return new GoalBossLeaf();
            }
        }
    }

    private CompositeGoal buildCompositeGoal(JSONArray subg) {
        Goal goal1 = build(subg.getJSONObject(0));
        Goal goal2 = build(subg.getJSONObject(1));
        return new CompositeGoal(goal1, goal2);
    }

    private boolean isComposite(String root) {
       return root.equalsIgnoreCase(AND)|| root.equalsIgnoreCase(OR);
    }
}
