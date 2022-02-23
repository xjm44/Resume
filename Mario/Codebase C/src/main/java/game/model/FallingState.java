package game.model;

import java.io.Serializable;

/**
 * Represents the state the controllable entity enters when
 * no floor collision is detected
 */
public class FallingState implements GravitationalState {

    /**
     * The force of gravity used to determine how the hero falls
     */
    private Gravity falling;

    /**
     * Creates the falling state
     */
    public FallingState() {
        this.falling = new Gravity(1, 0.4);
    }

    @Override
    public void moveVertically(Controllable hero) {
        hero.setYPos(hero.getYPos() + this.falling.getCurrentVelocity());
        this.falling.next();
    }

    @Override
    public Gravity getForce() {
        return this.falling;
    }
}
