package game.model;

import java.io.Serializable;

import static game.model.Collision.*;

/**
 * Represents the strategy used by enemies whereby the enemy
 * follows the controllable entity
 */
public class FollowHeroMovement implements EnemyMovement {

    @Override
    public void move(Enemy enemy, Controllable hero, boolean leftCollision, boolean rightCollision) {
        double heroCenter = hero.getXPos() + hero.getWidth() / 2;
        double enemyCenter = enemy.getXPos() + enemy.getWidth() / 2;
        if (heroCenter <= enemyCenter) {
            if (horizontalCollision(enemy, hero, enemy.getXVel() * -1)) {
                hero.takeDamage();
            }
            if (!leftCollision && heroCenter < enemyCenter - enemy.getXVel()) {
                enemy.setXPos(enemy.getXPos() - enemy.getXVel());
            }
        } else if (heroCenter >= enemyCenter) {
            if (horizontalCollision(enemy, hero, enemy.getXVel())) {
                hero.takeDamage();
            }
            if (!rightCollision && heroCenter > enemyCenter + enemy.getXVel()) {
                enemy.setXPos(enemy.getXPos() + enemy.getXVel());
            }
        }
    }

}
