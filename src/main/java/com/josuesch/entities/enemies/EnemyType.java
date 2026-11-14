package com.josuesch.entities.enemies;

import com.josuesch.Game;
import com.josuesch.graphics.animation.AnimationType;
import com.josuesch.entities.Enemy;
import java.util.function.Supplier;

public enum EnemyType {
    FIREMAN(AnimationType.FIREMAN, 3, 1.0, LinearMovement::new, () -> new MeleeAttack(60, 1)),
    MUDLIN(AnimationType.MUDLIN, 4, 0.75, TeleportingMovement::new, () -> new MeleeAttack(60, 1)),
    BAT(AnimationType.BAT, 2, 1.35, 16, 9, 0, 4, FlyingMovement::new, () -> new MeleeAttack(60, 1));

    private final AnimationType animationType;
    private final int health;
    private final double speed;
    private final int width;
    private final int height;
    private final int xSpriteOffset;
    private final int ySpriteOffset;
    private final Supplier<MovementBehavior> movementSupplier;
    private final Supplier<AttackBehavior> attackSupplier;

    EnemyType(
            AnimationType animationType,
            int health,
            double speed,
            Supplier<MovementBehavior> movementSupplier,
            Supplier<AttackBehavior> attackSupplier) {
        this(animationType, health, speed, 16, 16, 0, 0, movementSupplier, attackSupplier);
    }

    EnemyType(
            AnimationType animationType,
            int health,
            double speed,
            int width,
            int height,
            int xSpriteOffset,
            int ySpriteOffset,
            Supplier<MovementBehavior> movementSupplier,
            Supplier<AttackBehavior> attackSupplier) {
        this.animationType = animationType;
        this.health = health;
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.xSpriteOffset = xSpriteOffset;
        this.ySpriteOffset = ySpriteOffset;
        this.movementSupplier = movementSupplier;
        this.attackSupplier = attackSupplier;
    }

    public Enemy instantiate(Game game, double x, double y) {
        return new Enemy(
                game,
                this,
                movementSupplier.get(),
                attackSupplier.get(),
                health,
                speed,
                x,
                y,
                width,
                height,
                animationType,
                xSpriteOffset,
                ySpriteOffset);
    }
}
