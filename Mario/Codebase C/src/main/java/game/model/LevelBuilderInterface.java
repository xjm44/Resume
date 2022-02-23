package game.model;

public interface LevelBuilderInterface {

    /**
     * Builder methods to construct the level
     */
    LevelBuilderInterface buildHero();
    LevelBuilderInterface buildEntities();
    LevelBuilderInterface buildMovableEntities();
    LevelBuilderInterface buildFloorHeight();
    LevelBuilderInterface buildLevelWidth();
    LevelBuilderInterface buildLevelHeight();
    LevelBuilderInterface buildEnemies();
    LevelBuilderInterface buildGround();
    Level getLevel();
    LevelBuilderInterface buildTarget();

}
