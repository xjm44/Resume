package game.model;

import game.view.GameWindow;

import java.io.Serializable;

/**
 * Represents a slime enemy in the level
 */
public class Slime extends EnemyAbstraction  {

    /**
     * Colour of the slime
     */
    private String colour;

    /**
     * Movement strategy of the slime
     */
    private EnemyMovement movementStrategy;

    private int step_slime_score = 100;

    /**
     * Creates a slime enemy with the specified imagePath, xPos and spawn height
     */
    public Slime(String imagePath, double xPos, double spawnHeight) {
        super(imagePath, xPos, spawnHeight - 79.5, 106, 79.5, 2.5, Layer.FOREGROUND);
        this.spawnHeight = spawnHeight;
        this.colour = String.valueOf(imagePath.charAt(5));
        if (this.colour.equals("Y")) {
            this.movementStrategy = new FollowHeroMovement();
        } else {
            this.movementStrategy = new RandomEnemyMovement();
        }
        adjustSlime(this.colour);
    }

    /**
     * Adjusts the slimes width, height and yPos based on its colour
     * @param colour Colour of the slime
     */
    private void adjustSlime(String colour) {
        switch (colour) {
            case "Y":
                width *= 0.4;
                height *= 0.4;
                break;
            case "B":
                width *= 0.5;
                height *= 0.5;
                break;
            case "G":
                width *= 0.6;
                height *= 0.6;
                break;
            case "P":
                width *= 0.7;
                height *= 0.7;
                break;
            case "R":
                width *= 0.8;
                height *= 0.8;
                break;
        }
        this.yPos = this.spawnHeight - this.height;
    }

    @Override
    public void moveHorizontally(Controllable hero, boolean leftCollision, boolean rightCollision) {
        movementStrategy.move(this, hero, leftCollision, rightCollision);
    }

    @Override
    public void animate() {
        if (ticks / GameWindow.getTimer() <= animationTimer) {
            this.imagePath = "slime" + this.colour + "a.png";
        } else {
            this.imagePath = "slime" + this.colour + "b.png";
        }
        manageTicks();
    }

    /**
     * Manages the ticks that are used to animate the slime
     */
    private void manageTicks() {
        ticks += 1;
        if (ticks / GameWindow.getTimer() > animationTimer * 2) {
            ticks = 0;
        }
    }

    public int give_score(){
        if(isMarkedForDeletion()){
            return step_slime_score;
        }
        return 0;
    }

}
