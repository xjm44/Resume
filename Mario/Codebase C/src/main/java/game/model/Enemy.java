package game.model;

public interface Enemy extends Entity {

    /**
     * Marks the enemy for deletion
     */
    void markForDeletion();

    /**
     * Determines if the enemy has been marked for deletion
     * @return True if the enemy has been marked for deletion, else false
     */
    boolean isMarkedForDeletion();

    /**
     * Returns the current x velocity of the enemy
     * @return X velocity of the enemy
     */
    double getXVel();

    /**
     * Moves the enemy horizontally based on controllable entity and whether or
     * not its next movement will result in a collision
     * @param hero The controllable entity of the game
     * @param leftCollision Whether or not the enemy moving left will result in a collision
     * @param rightCollision Whether or not the enemy moving right will result in a collision
     */
    void moveHorizontally(Controllable hero, boolean leftCollision, boolean rightCollision);

    /**
     * Animates the enemy
     */
    void animate();

    int give_score();
}
