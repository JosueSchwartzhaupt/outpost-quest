package com.josuesch.entities;

import com.josuesch.Game;
import com.josuesch.assets.Movable;
import com.josuesch.assets.animation.AnimationType;
import com.josuesch.assets.animation.Animator;
import com.josuesch.entities.enemies.AttackBehavior;
import com.josuesch.entities.enemies.EnemyType;
import com.josuesch.entities.enemies.MovementBehavior;

public class Enemy extends Entity implements Movable {

    public final int baseHealth;

    private final EnemyType type;
    private final MovementBehavior movementBehavior;
    private final AttackBehavior attackBehavior;

    private int health;

    private double speed;
    private double angle;

    public Enemy(
            Game game,
            EnemyType type,
            MovementBehavior movementBehavior,
            AttackBehavior attackBehavior,
            int health,
            double speed,
            double x,
            double y,
            int width,
            int height,
            AnimationType animationType) {
        super(game, x, y, width, height, animationType);
        this.type = type;
        this.movementBehavior = movementBehavior;
        this.attackBehavior = attackBehavior;
        this.speed = speed;
        this.baseHealth = health;
        this.health = baseHealth;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public double getAngle() {
        return angle;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    @Override
    public void tick() {
        if (health > 0) {
            String animationStatus = movementBehavior.move(this).orElse("IDLE");

            animationStatus = attackBehavior.attack(this).orElse(animationStatus);

            animator.setAnimation(animationStatus);
        }
        else {
            animator.setAnimation("DEAD");
            if(animator.hasCompletedAnimation()){
                this.dispawn();
                return;
            }
        }

        animator.update();
    }

    public EnemyType getType() {
        return type;
    }

    public void heal(int healPoints) {
        this.health = Math.min(health + healPoints, baseHealth);
    }

    public void damage(int damage) {
        this.health = Math.max(health - damage, 0);
        game.getPlayer().addPoints(100);
        animator.applyTemporaryRedMask();
    }

    public int getHealth() {
        return health;
    }

    public Animator getAnimator(){
        return animator;
    }
}
