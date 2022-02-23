package game.model;

/**
 * Represents an abstraction of each entity in the level
 */
public abstract class EntityAbstraction implements Entity {

    protected String imagePath;
    protected double xPos;
    protected double yPos;
    protected double width;
    protected double height;
    protected Layer layer;

    public EntityAbstraction(String imagePath, double xPos, double yPos, double width, double height, Layer layer) {
        this.imagePath = imagePath;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.layer = layer;
    }


    @Override
    public String getImagePath() {
        return this.imagePath;
    }

    @Override
    public void setXPos(double xPos) {
        this.xPos = xPos;
    }

    @Override
    public void setYPos(double yPos) {
        this.yPos = yPos;
    }

    @Override
    public double getXPos() {
        return this.xPos;
    }

    @Override
    public double getYPos() {
        return this.yPos;
    }

    @Override
    public double getHeight() {
        return this.height;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public boolean isTangible() {
        return (this.layer.equals(Layer.FOREGROUND));
    }

    @Override
    public boolean isIcy() {
        return false;
    }

    @Override
    public boolean isFinishFlag() {
        return false;
    }

}
