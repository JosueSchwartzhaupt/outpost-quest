package com.josuesch.entities.enemies;

import com.josuesch.Game;
import com.josuesch.assets.HitboxComparator;
import com.josuesch.entities.Enemy;
import com.josuesch.entities.Player;

import java.util.Optional;

public class LinearMovement implements MovementBehavior {

    private static final int MOVEMENT_HYSTERESIS = 3;
    private static final int RANDOM_DECISION_TIME = 30;
    private int randomTick;
    private int randomInt;
    boolean right, up, left, down;



    @Override
    public Optional<String> move(Enemy enemy) {
        var player = enemy.getGame().getPlayer();
        if(randomTick < RANDOM_DECISION_TIME)randomTick++;

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

        var collision = HitboxComparator.processCollision(enemy, enemy.getGame().getCollidables());
        if (collision.isPresent()){
            dx = collision.get().maxDxSpeed();
            dy = collision.get().maxDySpeed();
        }

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
        if(enemy.getX() > player.getX() + MOVEMENT_HYSTERESIS) {
            left = true;
        }
        else if(enemy.getX() < player.getX() - MOVEMENT_HYSTERESIS) {
            right = true;
        }
        if(enemy.getY() > player.getY() + MOVEMENT_HYSTERESIS) {
            up = true;
        }
        else if(enemy.getY() < player.getY() - MOVEMENT_HYSTERESIS) {
            down = true;
        }
        if((left || right)&&(up || down)) {
            if(randomTick >= RANDOM_DECISION_TIME) {
                randomTick=0;
                randomInt = Game.RANDOM.nextInt(3);
            }
            switch(randomInt) {
                case 1:
                    left = false;
                    right = false;
                    break;
                case 2:
                    up = false;
                    down = false;
                    break;
            }
        }
    }
}
