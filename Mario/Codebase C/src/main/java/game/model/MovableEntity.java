package game.model;

public interface MovableEntity extends Entity {

    /**
     * Moves the movable entity horizontally
     */
    void moveHorizontally();

    /**
     * Returns the x velocity of the movable entity
     * @return The x velocity of the movable entity
     */
    double getXVel();

}
