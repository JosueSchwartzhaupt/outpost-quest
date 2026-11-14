package com.josuesch.entities.powerups.effects;

import com.josuesch.entities.Player;
import com.josuesch.entities.powerups.TimedEffect;

public class RapidShotEffect extends TimedEffect {

    private final int BOOST = 5;

    private int appliedBoost;

    public RapidShotEffect(int durationSeconds) {
        super(durationSeconds);
    }

    @Override
    public void onApply(Player player) {
        appliedBoost = player.decreaseShootingSpeed(BOOST);
    }

    @Override
    public void onExpire(Player player) {
        player.increaseShootingSpeed(appliedBoost);
    }
}
