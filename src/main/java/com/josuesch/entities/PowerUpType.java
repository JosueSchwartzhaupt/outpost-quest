package com.josuesch.entities;

import com.josuesch.Game;

import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;

public enum PowerUpType {
;
    private final BufferedImage sprite;
    private final BiConsumer<Game, Player> effect;

    PowerUpType(BufferedImage sprite, BiConsumer<Game, Player> effect) {
        this.sprite = sprite;
        this.effect = effect;
    }

    public void apply(Game game, Player player) {
        effect.accept(game, player);
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public static PowerUpType getRandomPowerUp() {
        PowerUpType[] powerUps = PowerUpType.values();
        return powerUps[Game.RANDOM.nextInt(powerUps.length)];
    }
}
