package game.model;

public interface Controllable extends Entity {

    /**
     * Sends signal to the controllable entity to jump
     * @return True if the controllable entity can jump, else false
     */
    boolean signalJump();

    /**
     * Sends signal to the controllable entity to move left
     * @return True if the controllable entity can move left, else false
     */
    boolean signalMoveLeft();

    /**
     * Sends signal to the controllable entity to move right
     * @return True if the controllable entity can move right, else false
     */
    boolean signalMoveRight();

    /**
     * Sends signal to the controllable entity to stop moving
     * @return True if the controllable entity is can stop moving
     *         i.e. the controllable entity must be moving prior to the function call.
     *         else false
     */
    boolean signalStopMoving();

    /**
     * Returns the number of health points the controllable entity has
     * @return The number of health points the controllable entity has
     */
    int getHealth();

    /**
     * Sends signal to the controllable entity to take damage
     */
    void takeDamage();

    /**
     * Sets the gravitational state of the controllable entity to the falling state
     */
    void initiateFall();

    /**
     * Sets the gravitational state of the controllable entity to the bouncing state
     */
    void initiateBounce();

    /**
     * Moves the controllable entity vertically by altering its yPos
     */
    void moveVertically();

    /**
     * Moves the controllable entity left by altering its xPos
     */
    void moveLeft();

    /**
     * Moves the controllable entity right by altering its xPos
     */
    void moveRight();

    /**
     * Determines if the controllable entity is currently jumping
     * @return True if the controllable entity is jumping, else false
     */
    boolean isJumping();

    /**
     * Determines if the controllable entity is currently moving right
     * @return True if the controllable entity is moving right, else false
     */
    boolean isMovingRight();

    /**
     * Determines if the controllable entity is currently moving left
     * @return True if the controllable entity is moving left, else false
     */
    boolean isMovingLeft();

    /**
     * Determines if the controllable entity is currently falling
     * @return True if the controllable entity is falling, else false
     */
    boolean isFalling();

    /**
     * Determines if the controllable entity is currently bouncing
     * @return True if the controllable entity is bouncing, else false
     */
    boolean isBouncing();

    /**
     * Sends signal to the controllable entity that its current
     * gravitational state has finished
     */
    void finishGravitationalState();

    /**
     * Returns the current y velocity of the controllable entity
     * @return Y velocity of the controllable entity
     */
    double getYVel();

    /**
     * Returns the current x velocity of the controllable entity
     * @return X velocity of the controllable entity
     */
    double getXVel();

    /**
     * Reverse the y velocity of the controllable entities current
     * gravitational state
     */
    void reverseYVel();

    /**
     * Used to communicate with the levels
     */
    void tick();

    void setHealth(int x);
}
