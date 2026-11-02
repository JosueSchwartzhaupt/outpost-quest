package com.josuesch.entities;

import com.josuesch.Game;
import com.josuesch.assets.HitboxComparator;
import com.josuesch.assets.Movable;
import com.josuesch.assets.animation.AnimationType;
import java.util.Optional;

public class Player extends Entity implements Movable {
    public static final double NATURAL_SPEED = 2;
    public static final int MAX_HEALTH = 3;
    public static final int MAX_SIZE = Game.WIDTH - 64;
    public static final int MAX_SPEED = 10;

    private boolean right, up, left, down;
    private boolean shootRight, shootUp, shootLeft, shootDown;

    private double speed;
    private double dx;
    private double dy;

    private int health = MAX_HEALTH;

    private int points;

    private int shootingDrawback = 20;
    private int shootingIdx = shootingDrawback;

    public Player(Game game, double x, double y) {
        super(game, x, y, 10, 16, AnimationType.PLAYER);
        speed = NATURAL_SPEED;
        xSpriteOffset = 3;
    }

    public Optional<String> move() {
        int horizontal = (right ? 1 : 0) - (left ? 1 : 0);
        int vertical = (down ? 1 : 0) - (up ? 1 : 0);
        double mag = Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical, 2));
        if (mag == 0) {
            mag = 1;
        }

        dx = speed * (horizontal / mag);
        dy = speed * (vertical / mag);

        double relativeSpeed = speed;
        var collision = HitboxComparator.processCollision(this, game.getCollidables());
        if (collision.isPresent()) relativeSpeed = collision.get().maxSpeed();
        dx = relativeSpeed * (horizontal / mag);
        dy = relativeSpeed * (vertical / mag);

        x += dx;
        y += dy;

        if (dy < 0) return Optional.of("UP");
        else if (dy > 0) return Optional.of("DOWN");
        else if (dx < 0) return Optional.of("LEFT");
        else if (dx > 0) return Optional.of("RIGHT");
        return Optional.empty();
    }

    public Optional<String> attack() {
        if (shootingIdx < shootingDrawback) {
            shootingIdx++;
        }
        if (!(shootUp || shootRight || shootLeft || shootDown)) {
            return Optional.empty();
        }

        int horizontal = (shootRight ? 1 : 0) - (shootLeft ? 1 : 0);
        int vertical = (shootDown ? 1 : 0) - (shootUp ? 1 : 0);
        double mag = Math.sqrt(Math.pow(horizontal, 2) + Math.pow(vertical, 2));
        if (mag == 0) {
            mag = 1;
        }

        double arrowDx = (horizontal / mag);
        double arrowDy = (vertical / mag);

        if (arrowDx == 0 && arrowDy == 0) return Optional.empty();

        if (shootingIdx >= shootingDrawback) {
            shootingIdx = 0;
            game.getEntities()
                    .add(
                            new Arrow(
                                    game,
                                    x + width / 2,
                                    y + height / 2,
                                    Math.toDegrees(Math.atan2(arrowDy, arrowDx))));
        }

        String direction = null;

        if (arrowDy < 0) direction = "UP_SHOOT";
        else if (arrowDy > 0) direction = "DOWN_SHOOT";
        else if (arrowDx < 0) direction = "LEFT_SHOOT";
        else if (arrowDx > 0) direction = "RIGHT_SHOOT";

        if (isMoving() && direction == null) {
            return Optional.empty();
        }

        String state = !isMoving() ? "IDLE_" + direction : direction;

        return Optional.of(state);
    }

    @Override
    public void tick() {
        String animationStatus = move().orElse("IDLE");

        animationStatus = attack().orElse(animationStatus);

        animator.setAnimation(animationStatus);

        animator.update();
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setShootDown(boolean shootDown) {
        this.shootDown = shootDown;
    }

    public void setShootLeft(boolean shootLeft) {
        this.shootLeft = shootLeft;
    }

    public void setShootUp(boolean shootUp) {
        this.shootUp = shootUp;
    }

    public void setShootRight(boolean shootRight) {
        this.shootRight = shootRight;
    }

    private boolean isMoving() {
        return dx != 0 || dy != 0;
    }

    @Override
    public double getAngle() {
        double response = Math.atan2(dy, dx);
        return response < 0 ? (response + (2 * Math.PI)) : response;
    }

    @Override
    public double getSpeed() {
        return speed;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void removePoints(int points) {
        this.points -= points;
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

    public boolean isDead() {
        return health == 0;
    }

    @Override
    public boolean isColidable() {
        return true;
    }

    public void expandPaddle(int size) {
        int increase = Math.min(MAX_SIZE - width, size);
        double gameCenter = Game.WIDTH / 2.0;
        double paddleCenter = x + width / 2.0;

        if (paddleCenter < gameCenter) {
            width += increase;
        } else if (paddleCenter > gameCenter) {
            x -= increase;
            width += increase;
        } else {
            x -= increase / 2.0;
            width += increase;
        }
    }
}
