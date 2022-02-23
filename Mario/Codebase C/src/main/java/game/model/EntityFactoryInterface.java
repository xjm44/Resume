package game.model;

public interface EntityFactoryInterface {
    Entity makeEntity(String type, double xPos, double yPos);
}
