package com.josuesch.entities.powerups;

import com.josuesch.Game;
import com.josuesch.assets.HitboxComparator;
import com.josuesch.entities.Entity;

public class PowerUp extends Entity {

    private static final int TOTAL_LIFE_TIME = 10 * 60;

    private PowerUpType type;
    private int lifeTime;

    public PowerUp(Game game, double x, double y, PowerUpType type) {
        super(game, x, y, 14, 14, type.getAnimationType());
        this.type = type;
    }

    @Override
    public void tick() {

        var player = game.getPlayer();
        if (HitboxComparator.isColiding(this, player)) {
            player.pickUpPowerUp(type);
            dispawn();
            return;
        }
        int remainingLife = TOTAL_LIFE_TIME - lifeTime;
        if (remainingLife <= 120) {
            if (remainingLife % 30 == 0) {
                if (animator.getTransparency() == 0) {
                    animator.setTransparency(80);
                } else {
                    animator.setTransparency(0);
                }
            }
        }

        if (lifeTime >= TOTAL_LIFE_TIME) dispawn();

        lifeTime++;
    }
}
