package game.model;

import game.view.GameWindow;

import java.io.Serializable;

import static game.model.Collision.*;

/**
 * Represents the strategy used by enemies whereby the enemy
 * moves left and right randomly
 */
public class RandomEnemyMovement implements EnemyMovement {

    private double ticks = 0;
    private double still = 0.5;
    private double phaseA = 0;
    private double phaseB = 0;

    public void move(Enemy enemy, Controllable hero, boolean leftCollision, boolean rightCollision) {

        if (readyToMove()) {
            phaseA = random(3 + still, 2 + still);
            phaseB = random(3 + still, 2 + still);
        }

        if (ticks <= GameWindow.ticksPerSecond() * phaseA) {
            if (!rightCollision) {
                enemy.setXPos(enemy.getXPos() + enemy.getXVel());
            } else if (horizontalCollision(enemy, hero, enemy.getXVel())) {
                hero.takeDamage();
            } else {
                ticks = GameWindow.ticksPerSecond() * (phaseA + still);
            }
        } else if (ticks > GameWindow.ticksPerSecond() * (phaseA + still) && ticks <= GameWindow.ticksPerSecond() * (phaseA + still + phaseB)) {
            if (!leftCollision && enemy.getXPos() - enemy.getXVel() >= 0) {
                enemy.setXPos(enemy.getXPos() - enemy.getXVel());
            } else if (horizontalCollision(enemy, hero, enemy.getXVel() * -1)) {
                hero.takeDamage();
            } else {
                ticks = 0;
            }
        }

        manageTicks();
    }

    private boolean readyToMove() {
        return (this.ticks == 0);
    }

    private double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    private void manageTicks() {
        ticks += 1;
        if (ticks >= GameWindow.ticksPerSecond() * (phaseA + phaseB + still * 2)) {
            ticks = 0;
        }
    }
}
