package game.model;

/**
 * Level director controls how the level is created
 */
public class LevelDirector {

    /**
     * Uses level builder object to build the level
     */
    private LevelBuilderInterface levelBuilder;

    /**
     * The level to be created
     */
    private Level level;

    /**
     * Creates a level director object which can be used
     * to create the level
     * @param levelBuilder Level builder object used to construct the level
     */
    public LevelDirector(LevelBuilderInterface levelBuilder) {
        this.levelBuilder = levelBuilder;
    }

    /**
     * Directs the builder to build the level
     */
    public void buildLevel() {
        this.levelBuilder = levelBuilder.buildLevelWidth()
                                        .buildLevelHeight()
                                        .buildFloorHeight()
                                        .buildEntities()
                                        .buildGround()
                                        .buildEnemies()
                                        .buildMovableEntities()
                                        .buildHero()
                                        .buildTarget()
        ;


        this.level = this.levelBuilder.getLevel();
    }


    /**
     * Returns the constructed level
     * @return The constructed level
     */
    public Level getLevel() {
        return this.level;
    }

}