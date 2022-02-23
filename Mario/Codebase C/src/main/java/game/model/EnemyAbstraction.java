package game.model;

/**
 * Represents an abstraction of all enemy entities in the game
 */
public abstract class EnemyAbstraction extends MovableEntityAbstraction implements Enemy {

    /**
     * Used to keep track of whether or not the enemy has been marked for deletion
     */
    private boolean delete = false;

    /**
     * The initial spawn yPos of the enemy
     */
    protected double spawnHeight;

    /**
     * Used for animation purposes
     */
    protected double ticks = 0;

    /**
     * Used for animation purposes
     */
    protected double animationTimer = Math.random() + 1;

    /**
     * Creates an enemy abstraction with the specified image path, xPos,
     * yPos, width, height, xVel and layer
     */
    public EnemyAbstraction(String imagePath, double xPos, double yPos, double width, double height, double xVel, Layer layer) {
        super(imagePath, xPos, yPos, width, height, xVel, layer);
    }

    @Override
    public void markForDeletion() {
        delete = true;
    }

    @Override
    public boolean isMarkedForDeletion() {
        return delete;
    }

}
