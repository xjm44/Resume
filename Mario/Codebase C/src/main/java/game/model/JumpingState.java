package game.model;

import java.io.Serializable;

/**
 * Represents the state the controllable entity enters whenever it jumps
 */
public class JumpingState implements GravitationalState {


    /**
     * The force of gravity used to determine how the hero jumps
     */
    private Gravity gravity;

    /**
     * Creates the jumping state
     */
    public JumpingState() {
        this.gravity = new Gravity(-10, 0.4);
    }

    @Override
    public void moveVertically(Controllable hero) {
        hero.setYPos(hero.getYPos() + this.gravity.getCurrentVelocity());
        this.gravity.next();
    }

    @Override
    public Gravity getForce() {
        return this.gravity;
    }
}
