package game.model;

public interface EnemyFactoryInterface {
    Enemy makeEnemy(String type, double xPos, double spawnHeight);
}
