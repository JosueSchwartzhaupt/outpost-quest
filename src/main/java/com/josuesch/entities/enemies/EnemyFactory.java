package com.josuesch.entities.enemies;

import com.josuesch.Game;
import com.josuesch.assets.animation.AnimationType;
import com.josuesch.entities.Enemy;

public class EnemyFactory {
    public static Enemy create(Game game, EnemyType type, double x, double y) {
        // Assemble behaviors based on type
        MovementBehavior movement =
                switch (type) {
                    case FIREMAN -> new LinearMovement();
                    case MUDLIN -> new LinearMovement();
                };

        AnimationType animationType =
                switch (type) {
                    case FIREMAN -> AnimationType.FIREMAN;
                    case MUDLIN -> AnimationType.MUDLIN;
                };

        AttackBehavior attack =
                switch (type) {
                    default -> new MeleeAttack(60);
                };

        Enemy enemy = new Enemy(game, type, movement, attack, x, y, 16, 16, animationType);
        return enemy;
    }
}
