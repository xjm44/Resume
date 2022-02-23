package game.model;

public interface GravitationalState {

    /**
     * Moves the controllable entity vertically
     * @param hero The controllable entity to be moved
     */
    void moveVertically(Controllable hero);

    /**
     * Returns the force associated with the gravitational state
     * @return The force associated with the gravitational state
     */
    Gravity getForce();

}
