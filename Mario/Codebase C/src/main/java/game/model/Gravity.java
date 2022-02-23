package game.model;

import java.io.Serializable;

/**
 * Represents the force of gravity
 */
 public class Gravity {

    /**
     * The initial velocity of the force
     */
    private double initialVelocity;

    /**
     * The current velocity of the force
     */
    private double currentVelocity;

    /**
     * The acceleration of the force
     */
    private double acceleration;

    /**
     * Creates a gravity object with the specified initial velocity and acceleration
     * @param initialVelocity Initial velocity of the force
     * @param acceleration Acceleration of the force
     */
    public Gravity(double initialVelocity, double acceleration) {
        this.initialVelocity = initialVelocity;
        this.currentVelocity = initialVelocity;
        this.acceleration = acceleration;
    }

    /**
     * Returns the current velocity
     * @return The current velocity
     */
    public double getCurrentVelocity() {
        return this.currentVelocity;
    }

    /**
     * Increases the current velocity by the acceleration
     */
    public void next() {
        this.currentVelocity += this.acceleration;
    }

    /**
     * Reverse the direction of the force
     */
    public void reverseForce() {
        this.currentVelocity = this.acceleration;
    }

    /**
     * Resets the current velocity to be the initial velocity
     */
    public void reset() {
        this.currentVelocity = this.initialVelocity;
    }

}
