package com.josuesch.entities.powerups;

import com.josuesch.Game;
import com.josuesch.assets.animation.AnimationType;
import com.josuesch.entities.powerups.effects.BombEffect;
import com.josuesch.entities.powerups.effects.HealEffect;
import com.josuesch.entities.powerups.effects.RapidShotEffect;
import java.util.function.Supplier;

public enum PowerUpType {
    HEAL(AnimationType.HEALTH_POTION, HealEffect::new),
    RAPID_FIRE(AnimationType.QUIVER, () -> new RapidShotEffect(10)),
    BOMB(AnimationType.BOMB, BombEffect::new);
    private final AnimationType animationType;
    private final Supplier<Effect> effectSupplier;

    PowerUpType(AnimationType animationType, Supplier<Effect> effectSupplier) {
        this.animationType = animationType;
        this.effectSupplier = effectSupplier;
    }

    public Effect createEffect() {
        return effectSupplier.get();
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public static PowerUpType getRandomPowerUp() {
        PowerUpType[] powerUps = PowerUpType.values();
        return powerUps[Game.RANDOM.nextInt(powerUps.length)];
    }
}
