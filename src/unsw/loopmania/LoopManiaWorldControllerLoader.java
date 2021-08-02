package unsw.loopmania;

import unsw.loopmania.controllers.*;
import unsw.loopmania.entity.*;
import unsw.loopmania.entity.moving.Character;
import unsw.loopmania.entity.notmoving.PathTile;
import unsw.loopmania.entity.notmoving.building.Building;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.File;

/**
 * A LoopManiaLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * 
 * this should NOT be used to load any entities which spawn, or might be removed (use controller for that)
 * since this doesnt add listeners or teardown functions (so it will be very hacky to remove event handlers)
 */
public class LoopManiaWorldControllerLoader extends LoopManiaWorldLoader {

    private List<ImageView> entities;

    //Images
    private Image characterImage;
    private Image pathTilesImage;
    private Image buildingImage;

    public LoopManiaWorldControllerLoader(String filename)
            throws FileNotFoundException {
        super(filename);
        entities = new ArrayList<>();
        characterImage = new Image((new File("src/images/human.png")).toURI().toString());
        pathTilesImage = new Image((new File("src/images/path.png")).toURI().toString());
        buildingImage = new Image((new File("src/images/heros_castle.png")).toURI().toString());
    }

    @Override
    public void onLoad(Character character) {
        ImageView view = new ImageView(characterImage);
        addEntity(character, view);
    }

    /**
     * load path tile ImageView based on configuration in file.
     * Note how src/images/32x32GrassAndDirtPath.png has 8 images within it
     * x and y values we produce here are the coordinates of the top left of our sub-image, taken from the top-left of the pathTilesImage
     */
    @Override
    public void onLoad(PathTile pathTile, PathTile.Direction into, PathTile.Direction out) {
        // note https://stackoverflow.com/a/58041962
        // we need to find the offset within the rectangle, we can do this from adjacencies
        ImageView view = new ImageView(pathTilesImage);
        view.setFitHeight(32);
        view.setFitWidth(32);
        addEntity(pathTile, view);
    }


    /**
     * pair the  backendentity and view, so the view tracks the coordinates of the entity
     * @param entity backend entity
     * @param view frontend image to be paired with the backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPositionOfNonSpawningEntities(entity, view);
        entities.add(view);
    }


    /**
     * Track the position of entities which don't spawn or require removal.
     * We only setup the node to follow the coordinates of the backend entity.<br>
     * Items which potentially need to be removed should be spawned by controller, and have listener handles and teardown functions added.
     * @param entity backend entity
     * @param node frontend image to track the coordinates of the backend entity
     */
    private static void trackPositionOfNonSpawningEntities(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());
        entity.x().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        });
        entity.y().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        });

    }

    /**
     * Create a controller that can be attached to the LoopManiaView with all the
     * loaded entities.
     * @return
     * @throws FileNotFoundException
     */
    public LoopManiaWorldController loadController() throws FileNotFoundException {
        return new LoopManiaWorldController(load(), entities);        
    }

    @Override
    public void onLoad(Building building) {
        ImageView view = new ImageView(buildingImage);
        addEntity(building, view);
    }


}
