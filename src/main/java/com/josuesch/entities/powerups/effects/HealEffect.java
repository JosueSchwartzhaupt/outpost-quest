package com.josuesch.entities.powerups.effects;

import com.josuesch.entities.Player;
import com.josuesch.entities.powerups.Effect;
import com.josuesch.entities.powerups.PowerUpType;

public class HealEffect implements Effect {
    private boolean done = false;

    @Override
    public void onApply(Player player) {
        player.heal(1);
        done = true;
    }

    @Override
    public boolean isExpired() {
        return done;
    }

    @Override
    public PowerUpType type() {
        return PowerUpType.HEAL;
    }
}
