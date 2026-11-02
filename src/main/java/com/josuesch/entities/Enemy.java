package com.josuesch.entities;

import com.josuesch.Game;
import com.josuesch.assets.Movable;
import com.josuesch.assets.animation.AnimationType;
import com.josuesch.entities.enemies.AttackBehavior;
import com.josuesch.entities.enemies.EnemyType;
import com.josuesch.entities.enemies.MovementBehavior;

public class Enemy extends Entity implements Movable {

    public static final int MAX_HEALTH = 3;

    private final EnemyType type;
    private final MovementBehavior movementBehavior;
    private final AttackBehavior attackBehavior;

    private int health = MAX_HEALTH;

    public Enemy(
            Game game,
            EnemyType type,
            MovementBehavior movementBehavior,
            AttackBehavior attackBehavior,
            double x,
            double y,
            int width,
            int height,
            AnimationType animationType) {
        super(game, x, y, width, height, animationType);
        this.type = type;
        this.movementBehavior = movementBehavior;
        this.attackBehavior = attackBehavior;
    }

    @Override
    public double getAngle() {
        return 0;
    }

    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    public void tick() {}

    public EnemyType getType() {
        return type;
    }

    public void heal(int healPoints) {
        this.health = Math.min(health + healPoints, MAX_HEALTH);
    }

    public void damage(int damage) {
        this.health = Math.max(health - damage, 0);
    }

    public int getHealth() {
        return health;
    }
}
