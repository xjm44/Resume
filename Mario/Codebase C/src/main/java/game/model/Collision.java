package game.model;

/**
 * This is the class used to detect collisions between
 * two tangible entities
 */
public class Collision {

    /**
     * Determines whether two entities are intersecting with each other
     * @param entityA The first entity
     * @param entityB The second entity
     * @return True if the entities intersect, else false
     */
    public static boolean aabbintersect(Entity entityA, Entity entityB) {
        return (entityA.getXPos() < (entityB.getXPos() + entityB.getWidth()) &&
                (entityA.getXPos() + entityA.getWidth()) > entityB.getXPos() &&
                (entityA.getYPos() < (entityB.getYPos() + entityB.getHeight())) &&
                (entityA.getYPos() + entityA.getHeight()) > entityB.getYPos());
    }

    /**
     * Determines whether an entity is going to intersect with another
     * entity based on its current horizontal velocity
     * @param entityA The moving entity
     * @param entityB The entity that the moving entity may potentially collide with
     * @param xVel The current horizontal velocity of the moving entity
     * @return True if the moving entity is going to hit the other entity, else false
     */
    public static boolean horizontalCollision(Entity entityA, Entity entityB, double xVel) {
        if (entityA == entityB) {
            return false;
        }
        entityA.setXPos(entityA.getXPos() + xVel);
        boolean collision = aabbintersect(entityA, entityB);
        entityA.setXPos(entityA.getXPos() - xVel);
        return collision;
    }

    /**
     * Determines whether an entity is going to intersect with another
     * entity based on its current vertical velocity
     * @param entityA The moving entity
     * @param entityB The entity that the moving entity may potentially collide with
     * @param yVel The current vertical velocity of the moving entity
     * @return True if the moving entity is going to hit the other entity, else false
     */
    public static boolean verticalCollision(Entity entityA, Entity entityB, double yVel) {
        if (entityA == entityB) {
            return false;
        }
        entityA.setYPos(entityA.getYPos() + yVel);
        boolean collision = aabbintersect(entityA, entityB);
        entityA.setYPos(entityA.getYPos() - yVel);
        return collision;
    }

}
