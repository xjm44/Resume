package game.model;

import java.io.Serializable;

/**
 * Represents a finish flag in the game
 */
public class FinishFlag extends EntityAbstraction {

    /**
     * Creates a finish flag with the specified image path, xPos, yPos, width and height
     */
    public FinishFlag(String imagePath, double xPos, double yPos, double width, double height) {
        super(imagePath, xPos, yPos, width, height, Layer.FOREGROUND);
    }

    @Override
    public boolean isFinishFlag() {
        return true;
    }

}
