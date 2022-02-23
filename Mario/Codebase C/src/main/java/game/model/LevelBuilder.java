package game.model;

/**
 * Used to create a level specified by the given json path
 */
public class LevelBuilder implements LevelBuilderInterface {

    /**
     * JSON extractor used to extract all the entities from the json file
     */
    private JSONExtractor extractor;

    /**
     * The level to be built
     */
    private Level level;

    /**
     * Create the level builder object
     * @param jsonPath The json path containing the contents of the level
     */
    public LevelBuilder(String jsonPath) {
        this.extractor = new JSONExtractor(jsonPath);
        this.level = new LevelImplementation();
    }

    public LevelBuilderInterface buildTarget(){
        this.level.setTarget(extractor.getTarget());
        return this;
    }

    public LevelBuilderInterface buildHero() {
        this.level.setHero(extractor.getHero());
        return this;
    }

    @Override
    public LevelBuilderInterface buildEntities() {
        this.level.setEntities(extractor.getEntities());
        return this;
    }

    @Override
    public LevelBuilderInterface buildMovableEntities() {
        this.level.setMovableEntities(extractor.getMovableEntities());
        return this;
    }

    @Override
    public LevelBuilderInterface buildFloorHeight() {
        this.level.setFloorHeight(extractor.getFloorHeight());
        return this;
    }

    @Override
    public LevelBuilderInterface buildLevelWidth() {
        this.level.setLevelWidth(extractor.getLevelWidth());
        return this;
    }

    @Override
    public LevelBuilderInterface buildLevelHeight() {
        this.level.setLevelHeight(extractor.getLevelHeight());
        return this;
    }

    @Override
    public LevelBuilderInterface buildEnemies() {
        this.level.setEnemies(extractor.getEnemies());
        return this;
    }

    @Override
    public LevelBuilderInterface buildGround() {
        this.level.setGround(new Ground("empty.png", 0, extractor.getFloorHeight(),
                extractor.getLevelWidth(), extractor.getLevelHeight() - extractor.getFloorHeight()));
        return this;
    }

    @Override
    public Level getLevel() {
        return this.level;
    }




}
