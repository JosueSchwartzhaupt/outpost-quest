package com.josuesch.entities.enemies;

import com.josuesch.Game;
import com.josuesch.entities.Enemy;
import com.josuesch.entities.Player;
import java.util.Optional;

public class FlyingMovement implements MovementBehavior {

    private static final double MOVEMENT_HYSTERESIS = 1;
    private static final int RANDOM_DECISION_TIME = 30;

    private int randomTick;
    boolean right, up, left, down;
    int randomInt;

    @Override
    public Optional<String> move(Enemy enemy) {
        var player = enemy.getGame().getPlayer();
        if (randomTick < RANDOM_DECISION_TIME) randomTick++;

        chooseRandomDirectionToPlayer(enemy, player);

        int horizontal = (right ? 1 : 0) - (left ? 1 : 0);
        int vertical = (down ? 1 : 0) - (up ? 1 : 0);
        double mag = Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical, 2));
        if (mag == 0) {
            mag = 1;
        }

        double dx = enemy.getSpeed() * (horizontal / mag);
        double dy = enemy.getSpeed() * (vertical / mag);
        double response = Math.atan2(dy, dx);
        double angle = response < 0 ? (response + (2 * Math.PI)) : response;

        enemy.setAngle(angle);

        enemy.setX(enemy.getX() + dx);
        enemy.setY(enemy.getY() + dy);

        if (dy < 0) return Optional.of("UP");
        else if (dy > 0) return Optional.of("DOWN");
        else if (dx < 0) return Optional.of("LEFT");
        else if (dx > 0) return Optional.of("RIGHT");
        return Optional.empty();
    }

    private void chooseRandomDirectionToPlayer(Enemy enemy, Player player) {
        left = false;
        right = false;
        up = false;
        down = false;

        double enemyCenterX = enemy.getX() + (enemy.getWidth() / 2.0);
        double playerCenterX = player.getX() + (player.getWidth() / 2.0);
        double enemyCenterY = enemy.getY() + (enemy.getHeight() / 2.0);
        double playerCenterY = player.getY() + (player.getHeight() / 2.0);

        if (enemyCenterX > playerCenterX + MOVEMENT_HYSTERESIS) {
            left = true;
        } else if (enemyCenterX < playerCenterX - MOVEMENT_HYSTERESIS) {
            right = true;
        }
        if (enemyCenterY > playerCenterY + MOVEMENT_HYSTERESIS) {
            up = true;
        } else if (enemyCenterY < playerCenterY - MOVEMENT_HYSTERESIS) {
            down = true;
        }

        if ((left || right) && (up || down)) {
            if (randomTick >= RANDOM_DECISION_TIME) {
                randomTick = 0;
                randomInt = Game.RANDOM.nextInt(6);
            }
            switch (randomInt) {
                case 1: // Just Vertical
                    left = false;
                    right = false;
                    break;
                case 2: // Just Horizontal
                    up = false;
                    down = false;
                    break;
            }
        }
    }
}
