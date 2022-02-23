package game.model;

import java.util.List;

public interface Level {

    /**
     * Returns all the entities associated with the level
     * @return All entities in the level
     */
    List<Entity> getEntities();

    /**
     * Returns the height of the level
     * @return The height of the level
     */
    double getHeight();

    /**
     * Returns the width of the level
     * @return The width of the level
     */
    double getWidth();

    /**
     * Used to communicate with the model and the controllable entity
     */
    void tick();

    /**
     * Returns the floor height of the level
     * @return The floor height of the level
     */
    double getFloorHeight();

    /**
     * Returns the xPos of the hero
     * @return The xPos of the hero
     */
    double getHeroX();

    /**
     * Returns the yPos of the hero
     * @return The yPos of the hero
     */
    double getHeroY();

    /**
     * Returns the amount of health the hero has
     * @return The amount of health the hero has
     */
    int getHeroHealth();

    /**
     * Direct pass through signals the hero to jump
     * @return True if the hero's jump method is successful, else false
     */
    boolean jump();

    /**
     * Direct pass through signals the hero to move left
     * @return True if the hero's move left method is successful, else false
     */
    boolean moveLeft();

    /**
     * Direct pass through signals the hero to move right
     * @return True if the hero's move right method is successful, else false
     */
    boolean moveRight();

    /**
     * Direct pass through signals the hero to stop moving
     * @return True if the hero's stop moving method is successful, else false
     */
    boolean stopMoving();

    /**
     * Returns whether or not the current level has been finished by the hero
     * @return True if the level is finished, else false
     */
    boolean isFinished();

    /**
     * Sets the entities for the level
     * @param entities The entities in the level
     */
    void setEntities(List<Entity> entities);

    /**
     * Sets the movable entities for the level
     * @param movableEntities The movable entities in the level
     */
    void setMovableEntities(List<MovableEntity> movableEntities);

    /**
     * Sets the enemies for the level
     * @param enemies The enemies in the level
     */
    void setEnemies(List<Enemy> enemies);

    /**
     * Sets the controllable entity for the level
     * @param hero The controllable entity for the level
     */
    void setHero(Controllable hero);

    /**
     * Sets the floor height for the level
     * @param floorHeight The floor height of th level
     */
    void setFloorHeight(double floorHeight);

    /**
     * Sets the width of the level
     * @param width The width of the level
     */
    void setLevelWidth(double width);

    /**
     * Sets the height of the level
     * @param height The height of the level
     */
    void setLevelHeight(double height);

    /**
     * Sets the ground entity of the level
     * @param ground The ground entity of the level
     */
    void setGround(Entity ground);

    int getScore();

    void setTarget(long target);

    long getTarget();

    void setScore(int temp);

    void setHeroHealth(int x);

    Object clone() throws CloneNotSupportedException;


}
