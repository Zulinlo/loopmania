package test.goals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import test.testHelpers.CreateGoal;

import org.javatuples.Pair;
import unsw.loopmania.*;
import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.goal.Goal;
import unsw.loopmania.entity.moving.*;

public class TestLevels {

    Goal goal = (new CreateGoal()).DummyGoal();

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
    public void Level1() throws IOException {
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setCharacter(character);
        character.setExp(3);
        world.levelCheck();
        assertEquals(character.getLevel(), 1);
        character.setExp(4);
        world.levelCheck();
        assertEquals(character.getLevel(), 1);
    }

    @Test
    public void Level2() throws IOException {
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setCharacter(character);
        character.setExp(5);
        world.levelCheck();
        assertEquals(character.getLevel(), 2);
        character.setExp(9);
        world.levelCheck();
        assertEquals(character.getLevel(), 2);
        character.setExp(12);
        world.levelCheck();
        assertEquals(character.getLevel(), 2);
    }

    @Test
    public void Level3() throws IOException {
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setCharacter(character);
        character.setExp(13);
        world.levelCheck();
        assertEquals(character.getLevel(), 3);
        character.setExp(20);
        world.levelCheck();
        assertEquals(character.getLevel(), 3);
        character.setExp(24);
        world.levelCheck();
        assertEquals(character.getLevel(), 3);
    }

    @Test
    public void Level4() throws IOException {
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setCharacter(character);
        character.setExp(25);
        world.levelCheck();
        assertEquals(character.getLevel(), 4);
        character.setExp(35);
        world.levelCheck();
        assertEquals(character.getLevel(), 4);
        character.setExp(42);
        world.levelCheck();
        assertEquals(character.getLevel(), 4);
    }

    @Test
    public void Level5() throws IOException {
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setCharacter(character);
        character.setExp(43);
        world.levelCheck();
        assertEquals(character.getLevel(), 5);
        character.setExp(60);
        world.levelCheck();
        assertEquals(character.getLevel(), 5);
        character.setExp(69);
        world.levelCheck();
        assertEquals(character.getLevel(), 5);
    }
    
    @Test
    public void Level6() throws IOException {
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setCharacter(character);
        character.setExp(70);
        world.levelCheck();
        assertEquals(character.getLevel(), 6);
        character.setExp(90);
        world.levelCheck();
        assertEquals(character.getLevel(), 6);
        character.setExp(110);
        world.levelCheck();
        assertEquals(character.getLevel(), 6);
    }

    @Test
    public void Level7() throws IOException {
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setCharacter(character);
        character.setExp(111);
        world.levelCheck();
        assertEquals(character.getLevel(), 7);
        character.setExp(135);
        world.levelCheck();
        assertEquals(character.getLevel(), 7);
        character.setExp(172);
        world.levelCheck();
        assertEquals(character.getLevel(), 7);
    }
   
    @Test
    public void Level8() throws IOException {
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setCharacter(character);
        character.setExp(173);
        world.levelCheck();
        assertEquals(character.getLevel(), 8);
        character.setExp(225);
        world.levelCheck();
        assertEquals(character.getLevel(), 8);
        character.setExp(265);
        world.levelCheck();
        assertEquals(character.getLevel(), 8);
    }

    @Test
    public void Level9() throws IOException {
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setCharacter(character);
        character.setExp(266);
        world.levelCheck();
        assertEquals(character.getLevel(), 9);
        character.setExp(335);
        world.levelCheck();
        assertEquals(character.getLevel(), 9);
        character.setExp(405);
        world.levelCheck();
        assertEquals(character.getLevel(), 9);
    }

    @Test
    public void Level10() throws IOException {
        List<Pair<Integer, Integer>> path = createOrderedPath();
        PathPosition pos = new PathPosition(0, path);
        LoopManiaWorld world = new LoopManiaWorld(8, 14, path, goal);
        Character character = new Character(pos);
        world.setCharacter(character);
        character.setExp(406);
        world.levelCheck();
        assertEquals(character.getLevel(), 10);
        character.setExp(407);
        world.levelCheck();
        assertEquals(character.getLevel(), 10);
        character.setExp(450);
        world.levelCheck();
        assertEquals(character.getLevel(), 10);
    }
}
