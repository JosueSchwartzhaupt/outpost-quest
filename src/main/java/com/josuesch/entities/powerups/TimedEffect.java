package com.josuesch.entities.powerups;

import com.josuesch.entities.Player;

public abstract class TimedEffect implements Effect {
    protected int remainingTicks;

    public TimedEffect(int durationSeconds) {
        this.remainingTicks = durationSeconds * 60;
    }

    @Override
    public void onTick(Player player) {
        if (remainingTicks > 0) remainingTicks--;
    }

    @Override
    public boolean isExpired() {
        return remainingTicks <= 0;
    }
}
