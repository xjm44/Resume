package game.model;

import game.view.GameWindow;

import java.io.Serializable;

/**
 * Represents the hero in the game
 */
public class Hero extends MovableEntityAbstraction implements Controllable{

    /**
     * Keeps track of the hero's current gravitational state
     */
    private GravitationalState gravitationalState;

    /**
     * The hero's falling state
     */
    private GravitationalState fallingState;

    /**
     * The hero's jumping state
     */
    private GravitationalState jumpingState;

    /**
     * The hero's bouncing state
     */
    private GravitationalState bouncingState;

    /**
     * Keeps track of whether or not the hero is moving right
     */
    private boolean right;

    /**
     * Keeps track of whether or not the hero is moving left
     */
    private boolean left;

    /**
     * Keeps track of whether or not the hero is jumping
     */
    private boolean jump;

    /**
     * Keeps track of whether or not the hero is falling
     */
    private boolean fall;

    /**
     * Keeps track of whether or not the hero is bouncing
     */
    private boolean bounce;

    /**
     * Keeps track of whether or not the hero is hurt
     */
    private boolean hurt;

    /**
     * Keeps track of whether or not the hero is facingRight
     */
    private boolean facingRight;

    /**
     * Keeps track of the number of ticks that have passed whilst the
     * hero is moving right or facing right
     */
    private int rightTicks = 0;

    /**
     * Keeps track of the number of ticks that have passed whilst the
     * hero is left right or facing left
     */
    private int leftTicks = 0;

    /**
     * Keeps track of the number of ticks that have passed whilst the hero is standing still
     */
    private int notMovingTicks = 0;

    /**
     * Keeps track of the number of ticks that have passed whilst the hero is in a hurt state
     */
    private int immunityTicks = 0;

    /**
     * The hero's health in the current level
     */
    private int health = 3;

    /**
     * Creates a hero with the specified imagePath, xPos, yPos, width, height
     * and xVel
     */
    public Hero(String imagePath, double xPos, double yPos, double width, double height, double xVel) {
        super(imagePath, xPos, yPos, width, height, xVel, Layer.FOREGROUND);
        this.fallingState = new FallingState();
        this.jumpingState = new JumpingState();
        this.bouncingState = new BouncingState();
        this.gravitationalState = this.fallingState;
        this.right = false;
        this.left = false;
        this.jump = false;
        this.fall = false;
        this.bounce = false;
        this.facingRight = true;
        this.hurt = false;
    }

    @Override
    public boolean signalJump() {
        if (jump || fall || bounce) {
            return false;
        }
        jump = true;
        setGravitationalState(jumpingState);
        return true;
    }

    @Override
    public boolean signalMoveRight() {
        if (right || left) {
            return false;
        }
        facingRight = true;
        right = true;
        rightTicks = 0;
        return true;
    }

    @Override
    public boolean signalMoveLeft() {
        if (left || right) {
            return false;
        }
        facingRight = false;
        left = true;
        leftTicks = 0;
        return true;
    }

    @Override
    public boolean signalStopMoving() {
        if (right || left) {
            right = false;
            left = false;
            return true;
        }
        return false;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    public void setHealth(int x){
        this.health = x;
    }

    @Override
    public void initiateBounce() {
        bounce = true;
        setGravitationalState(bouncingState);
    }

    @Override
    public void initiateFall() {
        fall = true;
        setGravitationalState(fallingState);
    }

    @Override
    public boolean isBouncing() {
        return bounce;
    }

    @Override
    public boolean isFalling() {
        return fall;
    }

    @Override
    public boolean isJumping() {
        return jump;
    }

    @Override
    public boolean isMovingRight() {
        return right;
    }

    @Override
    public boolean isMovingLeft() {
        return left;
    }

    @Override
    public void finishGravitationalState() {
        this.jump = false;
        this.fall = false;
        this.bounce = false;
    }

    @Override
    public void moveVertically() {
        gravitationalState.moveVertically(this);
    }

    @Override
    public void moveLeft() {
        if (xPos - xVel >= 0) {
            xPos -= xVel;
        }
    }

    @Override
    public void moveRight() {
        xPos += xVel;
    }

    @Override
    public void reverseYVel() {
        this.gravitationalState.getForce().reverseForce();
    }

    @Override
    public double getYVel() {
        return this.gravitationalState.getForce().getCurrentVelocity();
    }

    @Override
    public double getXVel() {
        return this.xVel;
    }

    /**
     * Animates the hero according to his movement
     */
    private void animate() {
        int animationTimer = 1;
        int animationTicks = animationTimer * GameWindow.getTimer();
        String type = "";
        if (hurt) {
            type = "hurt/";
        }
        if (right) {
            imagePath = type + "ch_walk" + ((rightTicks / animationTicks) + 1) + ".png";
        } else if (left && (xPos - xVel >= 0)) {
            imagePath = type + "ch_walk" + ((leftTicks / animationTicks) + 5) + ".png";
        } else if (facingRight) {
            imagePath = type + "ch_stand" + ((notMovingTicks / animationTicks) + 1) + ".png";
        } else {
            imagePath = type + "ch_stand" + ((notMovingTicks / animationTicks) + 4) + ".png";
        }
        rightTicks = (rightTicks + 1) % (animationTicks * 4);
        leftTicks = (leftTicks + 1) % (animationTicks * 4);
        notMovingTicks = (notMovingTicks + 1) % (animationTicks * 3);
    }

    /**
     * Changes the current gravitational state of the hero to the specified state and resets the y velocity
     * associated with the changed state.
     * @param gravitationalState The gravitational state to change to
     */
    private void setGravitationalState(GravitationalState gravitationalState) {
        this.gravitationalState = gravitationalState;
        this.gravitationalState.getForce().reset();
    }

    @Override
    public void tick() {
        handleImmunity();
        animate();
    }

    @Override
    public void takeDamage() {
        if (!hurt) {
            this.health--;
        }
        hurt = true;
    }

    /**
     * Handles how long the hero is immune after taking damage from an enemy
     */
    private void handleImmunity() {
        if (!hurt) {
            return;
        }
        int immunityTimer = 2;
        immunityTicks += 1;
        if (immunityTicks == GameWindow.ticksPerSecond() * immunityTimer) {
            hurt = false;
            immunityTicks = 0;
        }
    }

}
