package game.model;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class GameEngineImplementation implements GameEngine{

    /**
     * The height of the game engine
     */
    private double height;

    /**
     * The current level
     */
    private Level currentLevel;

    /**
     * Map of all the levels
     */
    private Map<Integer, Level> levels;

    /**
     * Used to create distinct level id's for each level
     */
    private int levelId;

    /**
     * Level id of the current level
     */
    private int currentLevelId;

    /**
     * Json path to the level configuration file
     */
    private String jsonPath;

    /**
     * Used to keep track of how long it takes the user to complete the game
     */
    private Instant start;

    /**
     * Used to keep track of how long it takes the user to complete the game
     */
    private Duration interval;

    /**
     * The number of lives the hero has
     */
    private int lives;



    /**
     * Creates the game engine using the specified json configuration file and height
     * @param jsonPath The path to the json configuration file containing the level information
     * @param height The height of the game engine's window
     */
    public GameEngineImplementation(String jsonPath, double height) {
        this.jsonPath = jsonPath;
        this.height = height;
        this.levels = new HashMap<>();
        this.levelId = 1;
        this.currentLevelId = 1;
        this.lives = 3;
        createLevels();

        startLevel();
    }


    public void nextLevel(){
        nextLevelId();
        nextJsonPath();
        LevelBuilder levelBuilder = new LevelBuilder(this.jsonPath);
        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        this.levels.put(this.levelId, levelDirector.getLevel());
        levelId += 1;
        startLevel();
    }

    /**
     * Creates the levels associated with the json file
     */
    public void createLevels() {
        LevelBuilder levelBuilder = new LevelBuilder(this.jsonPath);
        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        this.levels.put(this.levelId, levelDirector.getLevel());
        levelId += 1;


    }

    @Override
    public void startLevel() {
        this.currentLevel = levels.get(currentLevelId);
        start = Instant.now();
    }

    @Override
    public Level getCurrentLevel() {
        return this.currentLevel;
    }

    public void loadCurrentLevel(int x){

        this.currentLevel = levels.get(x-1);
        start = Instant.now();
    }

    @Override
    public boolean jump() {
        return this.currentLevel.jump();
    }

    @Override
    public boolean moveLeft() {
        return this.currentLevel.moveLeft();
    }

    @Override
    public boolean moveRight() {
        return this.currentLevel.moveRight();
    }

    @Override
    public boolean stopMoving() {
        return this.currentLevel.stopMoving();
    }

    @Override
    public void tick() {
        this.currentLevel.tick();
        interval = Duration.between(start, Instant.now());

    }

    @Override
    public void resetCurrentLevel() {
        this.lives--;
        if (this.lives == 0) {
            return;
        }
        LevelBuilder levelBuilder = new LevelBuilder(this.jsonPath);
        LevelDirector levelDirector = new LevelDirector(levelBuilder);
        levelDirector.buildLevel();
        this.levels.put(this.currentLevelId, levelDirector.getLevel());
        startLevel();
    }

    @Override
    public boolean isFinished() {
        return currentLevel.isFinished();
    }

    @Override
    public Duration getDuration() {
        return interval;
    }

    public void setDuration(Duration x){
        interval = x;
    }

    @Override
    public boolean gameOver() {
        return this.lives == 0;
    }

    @Override
    public int getLives() {
        return this.lives;
    }

    public int getCurrentLevelId(){
        return currentLevelId;
    }
    public void setCurrentLevelId(int x){
        currentLevelId = x;
    }

    public int getScore(){
        return currentLevel.getScore();
    }
    public long getTarget(){
        return currentLevel.getTarget();
    }

    public void setScore(int x){
        int temp = currentLevel.getScore() + x;
        currentLevel.setScore(temp);
    }


    public void nextJsonPath(){
        this.jsonPath = "level_"+currentLevelId+".json";
    }
    public int getJson(){
        return levelId;
    }
    public void nextLevelId(){
        this.currentLevelId++;
    }


}

