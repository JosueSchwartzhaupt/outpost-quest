package com.josuesch.entities;

import com.josuesch.Game;
import com.josuesch.assets.animation.AnimationType;

import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

public enum PowerUpType {
;
    private final AnimationType animationType;
    private final BiConsumer<Game, Player> effect;

    PowerUpType(AnimationType animationType, BiConsumer<Game, Player> effect) {
        this.animationType = animationType;
        this.effect = effect;
    }

    public void apply(Game game, Player player) {
        effect.accept(game, player);
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public static PowerUpType getRandomPowerUp() {
        PowerUpType[] powerUps = PowerUpType.values();
        return powerUps[Game.RANDOM.nextInt(powerUps.length)];
    }
}
