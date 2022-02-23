package game.model;

import java.io.Serializable;

/**
 * Represents a platform entity in the level
 */
public class Platform extends EntityAbstraction  {

    /**
     * Creates a platform entity with the specified image path, xPos,
     * yPos, width and height
     */
    public Platform(String imagePath, double xPos, double yPos, double width, double height) {
        super(imagePath, xPos, yPos, width, height, Layer.FOREGROUND);
    }

}
