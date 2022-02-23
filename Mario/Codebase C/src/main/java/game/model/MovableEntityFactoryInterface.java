package game.model;

public interface MovableEntityFactoryInterface {
    MovableEntity makeMovableEntity(String type, double xPos, double yPos, double xVel);
}
