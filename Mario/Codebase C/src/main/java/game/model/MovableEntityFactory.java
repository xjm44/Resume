package game.model;

/**
 * Class used to create different types of movable entities
 */
public class MovableEntityFactory implements MovableEntityFactoryInterface {

    /**
     * Creates a movable entity of the type specified by the type parameter, and with
     * the specified xPos, yPos and x velocity
     * @param type Type of movable entity to create
     * @param xPos The xPos of the movable entity
     * @param yPos The yPos of the movable entity
     * @param xVel The x velocity of the movable entity
     * @return The newly created movable entity
     */
    public MovableEntity makeMovableEntity(String type, double xPos, double yPos, double xVel) {

        MovableEntity movableEntity = null;

        if (type.equals("cloud")) {
            movableEntity = new Cloud("cloud_1.png", xPos, yPos, 80, 20, xVel);
        }

        return movableEntity;
    }

}
