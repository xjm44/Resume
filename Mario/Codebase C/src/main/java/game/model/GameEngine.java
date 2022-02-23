package game.model;

import java.time.Duration;

public interface GameEngine {

    /**
     * Returns the current level
     * @return The current level
     */
    Level getCurrentLevel();

    /**
     * Starts the current level
     */
    void startLevel();

    // Hero inputs - boolean for success (possibly for sound feedback)
    boolean jump();
    boolean moveLeft();
    boolean moveRight();
    boolean stopMoving();

    void tick();

    /**
     * Resets the current level
     */
    void resetCurrentLevel();

    /**
     * Determines whether or not the current level is finished
     * @return True if the current level is finished, else false
     */
    boolean isFinished();

    /**
     * Returns the duration of the current level
     * @return The duration of the current level
     */
    Duration getDuration();

    /**
     * Determines whether or not the game is over i.e. the hero has no more lives left
     * @return True if the game is over, else false
     */
    boolean gameOver();

    /**
     * Returns the number of lives the hero has left
     * @return The number of lives the hero has left
     */
    int getLives();

    int getCurrentLevelId();

    int getScore();

    long getTarget();

    void setScore(int i);

    int getJson();

    void nextLevel();

    void loadCurrentLevel(int x);

    void createLevels();

//    void loadScore(int x);

    void setDuration(Duration time);
}
