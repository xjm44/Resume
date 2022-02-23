package game.model;

import java.io.Serializable;

/**
 * Represents the state the hero enters whenever he lands on top
 * of a slime
 */
public class BouncingState implements GravitationalState {

    /**
     * The force of gravity used to determine how the hero bounces
     */
    private Gravity bouncing;

    /**
     * Creates the bouncing state
     */
    public BouncingState() {
        this.bouncing = new Gravity(-6, 0.4);
    }

    @Override
    public void moveVertically(Controllable hero) {
        hero.setYPos(hero.getYPos() + this.bouncing.getCurrentVelocity());
        this.bouncing.next();
    }

    @Override
    public Gravity getForce() {
        return this.bouncing;
    }
}
