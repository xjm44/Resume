package game.model;

/**
 * Used to create different types of entities
 */
public class EntityFactory implements EntityFactoryInterface {

    /**
     * Creates an entity of the type specified by the type parameter, and with
     * the specified xPos and yPos
     * @param type Type of entity to create
     * @param xPos The xPos of the entity
     * @param yPos The yPos of the entity
     * @return The newly created entity
     */
    public Entity makeEntity(String type, double xPos, double yPos) {

        Entity entity = null;

        if (type.equals("platform")) {
            entity = new Platform("platform.png", xPos, yPos, 150, 22.1636);
        } else if (type.equals("ice_platform")) {
            entity = new IcePlatform("ice_platform.png", xPos, yPos, 150, 22.1636);
        } else if (type.equals("block")) {
            entity = new Block("block.png", xPos, yPos, 30, 30);
        } else if (type.equals("finish_flag")) {
            entity = new FinishFlag("flag.png", xPos, yPos, 66.1, 132.5);
        }

        return entity;

    }
}
