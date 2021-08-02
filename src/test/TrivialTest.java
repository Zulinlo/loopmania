package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import test.testHelpers.CreateGoal;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.goal.Goal;


/**
 * this class is a dummy class demonstrating how to setup tests for the project
 * you should setup additional test classes in a similar fashion, aiming to achieve high coverage.
 * A clickable "Run Test" link should appear if you have installed the Java Extension Pack properly.
 */
public class TrivialTest {
    @Test
    public void blahTest(){
        assertEquals("a", "a");
    }
    
    @Test
    public void blahTest2(){
        Goal goal = (new CreateGoal()).DummyGoal();
        LoopManiaWorld d = new LoopManiaWorld(1, 2, new ArrayList<>(), goal);
        assertEquals(d.getWidth(), 1);
    }
}
