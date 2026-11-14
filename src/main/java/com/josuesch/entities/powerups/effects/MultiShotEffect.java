package com.josuesch.entities.powerups.effects;

import com.josuesch.entities.Player;
import com.josuesch.entities.powerups.PowerUpType;
import com.josuesch.entities.powerups.TimedEffect;

public class MultiShotEffect extends TimedEffect {

    private final int ARROW_QTD = 2;

    private int appliedAdditionalArrows;

    public MultiShotEffect(int durationSeconds) {
        super(durationSeconds);
    }

    @Override
    public void onApply(Player player) {
        appliedAdditionalArrows = player.increaseArrowQtd(ARROW_QTD);
    }

    @Override
    public PowerUpType type() {
        return PowerUpType.MULTI_SHOT;
    }

    @Override
    public void onExpire(Player player) {
        player.decreaseArrowQtd(appliedAdditionalArrows);
    }
}
