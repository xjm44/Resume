package game.model;

import game.view.GameWindow;

import java.io.Serializable;

/**
 * Represents a cloud entity in the level
 */
public class Cloud extends MovableEntityAbstraction {

    /**
     * Creates a new Cloud with the given image path,
     * x position, y position, width, height and velocity
     */
    public Cloud(String imagePath, double xPos, double yPos, double width, double height, double xVel) {
        super(imagePath, xPos, yPos, width, height, xVel / GameWindow.ticksPerSecond(), Layer.BACKGROUND);
    }

}
