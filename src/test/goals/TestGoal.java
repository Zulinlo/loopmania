package test.goals;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;

import test.testHelpers.CreateGoal;

import org.javatuples.Pair;
import unsw.loopmania.*;
import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.entity.notmoving.building.HerosCastle;
import unsw.loopmania.goal.CompositeGoal;
import unsw.loopmania.goal.*;
import unsw.loopmania.entity.moving.*;

public class TestGoal {

    Goal goal = (new CreateGoal()).actualGoal();

    private List<Pair<Integer, Integer>> createOrderedPath() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        for (int x = 1; x < 5; x++) { // Go right 4 tiles (1 to 4)
            Pair<Integer, Integer> route = new Pair<>(x, 1);
            orderedPath.add(route);
        }
        for (int y = 1; y < 5; y++) { // Go down 4 tiles (1 to 4)
            Pair<Integer, Integer> route = new Pair<>(4, y);
            orderedPath.add(route);
        }
        for (int x = 4; x > 0; x--) { // Go left 4 tiles (4 to 1)
            Pair<Integer, Integer> route = new Pair<>(x, 4);
            orderedPath.add(route);
        }
        for (int y = 4; y > 0; y--) { // Go up 4 tiles (4 to 1)
            Pair<Integer, Integer> route = new Pair<>(1, y);
            orderedPath.add(route);
        } 

        return orderedPath;
    }

    @Test
    public void edgeCaseGoalsCompleted() throws IOException {
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setDoggieBeat();
        world.setElanBeat();
        character.setGold(10000);
        character.setExp(406);
        world.setCharacter(character);
        assertEquals(world.gameWon(), true);
    }
    
    @Test
    public void edgeCaseGoalsNotCompleted() throws IOException{
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        character.setGold(9999);
        character.setExp(405);
        world.setDoggieBeat();
        world.setElanBeat();
        world.setCharacter(character);
        assertEquals(world.gameWon(), false);
    }

    @Test
    public void goldGoalCompleted() throws IOException{
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        character.setGold(10000);
        character.setExp(405);
        world.setCharacter(character);
        assertEquals(world.gameWon(), false);
    }

    @Test
    public void expGoalCompleted() throws IOException{
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        character.setGold(9999);
        character.setExp(406);
        world.setCharacter(character);
        assertEquals(world.gameWon(), false);
    }

    @Test
    public void bossGoalCompleted() throws IOException{
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setDoggieBeat();
        world.setElanBeat();
        character.setGold(90);
        character.setExp(406);
        world.setCharacter(character);
        assertEquals(world.gameWon(), false);
    }

    @Test
    public void testGoalCycles() throws IOException{
        // Intialize path and goals
        List<Pair<Integer, Integer>> path = createOrderedPath();
        CompositeGoal compGoal = new CompositeGoal(
            new GoalCyclesLeaf(1),
            new GoalGoldLeaf(0)
        );

        // Intializing the world
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, compGoal);
        Character character = new Character(pos);
        HerosCastle herosCastle = new HerosCastle(new SimpleIntegerProperty(1), new SimpleIntegerProperty(1));
        character.setGold(90);
        character.setExp(406);
        world.setCharacter(character);
        world.setHerosCastle(herosCastle);

        assertEquals(world.getCycle(), 0);
        assertEquals(world.gameWon(), false);

        // Go around a cycle, cycle is 1 now so goal passed
        for (int i = 0; i < 15; i++) {
            world.runTickMoves();
        }
        assertEquals(world.getCycle(), 1);
        assertEquals(world.gameWon(), true);

        // Change comp Goal but this shouldnt change the initial world goal
        compGoal.setGoal1(new GoalCyclesLeaf(2));
        compGoal.setGoal1(new GoalExpLeaf(0));
        assertEquals(world.getCycle(), 1);
        assertEquals(world.gameWon(), true);
    }

    // @Test
    // public void testGoalBuilder() throws FileNotFoundException{
    //     JSONObject json = new JSONObject(new JSONTokener(new FileReader("/../worlds/" + "world_with_twists_and_turns.json")));

    //     GoalBuilder allGoals = GoalBuilder.getInstance();
    //     Goal setGoal = allGoals.build(json.getJSONObject("goal-condition"));
    //     List<Pair<Integer, Integer>> path = createOrderedPath();
    //     LoopManiaWorld world = new LoopManiaWorld(8, 14, path, setGoal);

    //     assertEquals(world.getCycle(), 0);
    //     assertEquals(world.gameWon(), false);
    // }
}
