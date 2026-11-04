package com.josuesch.entities.enemies;

import com.josuesch.Game;
import com.josuesch.assets.animation.AnimationType;
import com.josuesch.entities.Enemy;

public class EnemyFactory {
    public static Enemy create(Game game, EnemyType type, double x, double y) {
        MovementBehavior movement;
        AttackBehavior attack;
        AnimationType animationType;
        double speed;
        int health;

        switch (type){
            case FIREMAN:
                animationType = AnimationType.FIREMAN;
                movement = new LinearMovement();
                attack = new MeleeAttack(60,1);
                health = 3;
                speed = 1;
                break;
            case MUDLIN:
                animationType = AnimationType.MUDLIN;
                movement = new TeleportingMovement();
                attack = new MeleeAttack(60,1);
                health = 3;
                speed = 0.75;
                break;
            case null, default:return null;
        }

        Enemy enemy = new Enemy(game, type, movement, attack,health,speed, x, y, 16, 16, animationType);
        return enemy;
    }
}
