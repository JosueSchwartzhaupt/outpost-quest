package com.josuesch.entities.powerups.effects;

import com.josuesch.entities.Player;
import com.josuesch.entities.powerups.Effect;

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
}
