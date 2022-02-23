package game.model;

public interface Entity {

    /**
     * Returns the image path of the entity
     * @return The image path of the entity
     */
    String getImagePath();

    /**
     * Sets the xPos of the entity
     * @param xPos The new xPos of the entity
     */
    void setXPos(double xPos);

    /**
     * Sets the yPos of the entity
     * @param yPos The new yPos of the entity
     */
    void setYPos(double yPos);

    /**
     * Returns the xPos of the entity
     * @return The xPos of the entity
     */
    double getXPos();

    /**
     * Returns the yPos of the entity
     * @return The yPos of the entity
     */
    double getYPos();

    /**
     * Returns the height of the entity
     * @return The height of the entity
     */
    double getHeight();

    /**
     * Returns the width of the entity
     * @return The width of the entity
     */
    double getWidth();

    /**
     * Returns the layer of the entity
     * @return The layer of the entity
     */
    Layer getLayer();

    /**
     * Determines whether or not the entity is a tangible object
     * @return True if the entity is tangible i.e. layer is in the foreground, else false
     */
    boolean isTangible();

    /**
     * Determines whether or not the entity is an icy object
     * @return True if the entity is icy, else false
     */
    boolean isIcy();

    /**
     * Determines whether or not the entity is the finish flag
     * @return True if the entity if the finish flag, else false
     */
    boolean isFinishFlag();

    enum Layer{
        BACKGROUND, FOREGROUND, EFFECT
    }
}
