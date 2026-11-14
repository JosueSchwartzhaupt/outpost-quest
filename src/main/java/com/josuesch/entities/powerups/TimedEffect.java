package com.josuesch.entities.powerups;

import com.josuesch.entities.Player;
import com.josuesch.save.dto.TimedEffectSaveDTO;

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

    public int getRemainingTicks() {
        return remainingTicks;
    }

    public void setRemainingTicks(int remainingTicks) {
        this.remainingTicks = remainingTicks;
    }

    public TimedEffectSaveDTO toSaveDTO() {
        return new TimedEffectSaveDTO(type(), remainingTicks);
    }

    public static Effect createEffect(TimedEffectSaveDTO dto) {
        Effect effect = dto.type().createEffect();
        if (effect instanceof TimedEffect timed) {
            timed.setRemainingTicks(dto.remainingTicks());
        }
        return effect;
    }
}
