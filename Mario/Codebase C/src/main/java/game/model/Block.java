package game.model;

import java.io.Serializable;

/**
 * Represents a block entity in the level
 */

public class Block extends EntityAbstraction  {

    /**
     * Creates a new Block with the given image path,
     * x position, y position, width and height
     */
    public Block(String imagePath, double xPos, double yPos, double width, double height) {
        super(imagePath, xPos, yPos, width, height, Layer.FOREGROUND);
    }

}
