package game.model;

public interface EnemyMovement {

    /**
     * Algorithm to move the enemy based on the controllable entity's position
     * in the level and whether or not the enemy will collide with another entity
     * when it moves left or right
     * @param enemy The enemy to move
     * @param hero The controllable entity of the level
     * @param leftCollision Whether or not moving the enemy left will cause a collision
     * @param rightCollision Whether or not moving the enemy right will cause a collision
     */
    void move(Enemy enemy, Controllable hero, boolean leftCollision, boolean rightCollision);

}
