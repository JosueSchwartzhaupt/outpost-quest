package com.josuesch.entities;

import com.josuesch.Game;
import com.josuesch.assets.HitboxComparator;
import com.josuesch.assets.Movable;
import com.josuesch.assets.animation.AnimationType;
import com.josuesch.entities.powerups.Effect;
import com.josuesch.entities.powerups.PowerUpType;
import com.josuesch.save.dto.PlayerSaveDTO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Player extends Entity implements Movable {
    public static final double NATURAL_SPEED = 2;
    public static final int MAX_HEALTH = 3;
    public static final int MAX_SIZE = Game.WIDTH - 64;
    public static final int MAX_SPEED = 10;
    public static final int MAX_SHOOTING_DRAWBACK = 20;
    public static final int MIN_SHOOTING_DRAWBACK = 1;

    private boolean right, up, left, down;
    private boolean shootRight, shootUp, shootLeft, shootDown;
    private boolean usePowerUp;

    private double speed;
    private double dx;
    private double dy;

    private int health = MAX_HEALTH;

    private int shootingDrawback = MAX_SHOOTING_DRAWBACK;
    private int shootingIdx = shootingDrawback;

    private PowerUpType savedPowerUp;

    private final List<Effect> activeEffects = new ArrayList<>();

    public Player(Game game, double x, double y) {
        super(game, x, y, 10, 16, AnimationType.PLAYER);
        speed = NATURAL_SPEED;
        xSpriteOffset = 3;
    }

    public Player(Game game, PlayerSaveDTO dto) {
        this(game, dto.x(), dto.y());
        this.health = Math.max(0, Math.min(dto.health(), MAX_HEALTH));
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

        var collision = HitboxComparator.processCollision(this, game.getPlayerCollidables());
        if (collision.isPresent()) {
            dx = collision.get().maxDxSpeed();
            dy = collision.get().maxDySpeed();
        }

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
        processPowerUps();

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

    public void setUsePowerUp(boolean usePowerUp) {
        this.usePowerUp = usePowerUp;
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

    public void heal(int healPoints) {
        this.health = Math.min(health + healPoints, MAX_HEALTH);
    }

    public void damage(int damage) {
        this.health = Math.max(health - damage, 0);
        animator.applyTemporaryRedMask();
    }

    public int getHealth() {
        return health;
    }

    public PowerUpType getSavedPowerUp() {
        return savedPowerUp;
    }

    public PlayerSaveDTO toSaveDTO() {
        return new PlayerSaveDTO(x, y, health);
    }

    public boolean isDead() {
        return health == 0;
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

    public int decreaseShootingSpeed(int ticks) {
        int shootingDrawbackAnt = shootingDrawback;
        shootingDrawback = Math.max(shootingDrawback - ticks, MIN_SHOOTING_DRAWBACK);
        return shootingDrawbackAnt - shootingDrawback;
    }

    public int increaseShootingSpeed(int ticks) {
        int shootingDrawbackAnt = shootingDrawback;
        shootingDrawback = Math.min(shootingDrawback + ticks, MAX_SHOOTING_DRAWBACK);
        return shootingDrawback - shootingDrawbackAnt;
    }

    public void pickUpPowerUp(PowerUpType type) {
        if (savedPowerUp == null) {
            savedPowerUp = type;
            return;
        }
        applyPowerUp(type);
    }

    private void applyPowerUp(PowerUpType type) {
        var effect = type.createEffect();
        effect.onApply(this);
        if (!effect.isExpired()) {
            activeEffects.add(effect);
        }
    }

    private void tickEffects() {
        Iterator<Effect> it = activeEffects.iterator();
        while (it.hasNext()) {
            Effect effect = it.next();
            effect.onTick(this);
            if (effect.isExpired()) {
                effect.onExpire(this);
                it.remove();
            }
        }
    }

    private void processPowerUps() {
        if (usePowerUp) {
            if (savedPowerUp != null) applyPowerUp(savedPowerUp);
            savedPowerUp = null;
            usePowerUp = false;
        }

        tickEffects();
    }
}
