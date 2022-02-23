package game.model;

public abstract class MovableEntityAbstraction extends EntityAbstraction implements MovableEntity {

    /**
     * The movable entity's x velocity
     */
    protected double xVel;

    public MovableEntityAbstraction(String imagePath, double xPos, double yPos, double width, double height, double xVel, Layer layer) {
        super(imagePath, xPos, yPos, width, height, layer);
        this.xVel = xVel;
    }

    @Override
    public void moveHorizontally() {
        this.xPos += this.xVel;
    }

    @Override
    public double getXVel() {
        return this.xVel;
    }

}
