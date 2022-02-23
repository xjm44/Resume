package game.model;

/**
 * Factory used to create different Enemy types
 */
public class EnemyFactory implements EnemyFactoryInterface {

    /**
     * Creates and returns and enemy specified by the type parameter with given xPos
     * and spawn height
     * @param type The type of enemy to create
     * @param xPos The xPos of the enemy
     * @param spawnHeight The spawn yPos of the enemy
     * @return The created enemy
     */
    public Enemy makeEnemy(String type, double xPos, double spawnHeight) {

        Enemy enemy = null;

        if (type.matches("slime[R, G, B, P, Y]")) {
            enemy = new Slime(type + "a.png", xPos, spawnHeight);
        }

        return enemy;

    }

}
