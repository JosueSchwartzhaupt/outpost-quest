package com.josuesch.entities.powerups;

import com.josuesch.entities.Player;

public interface Effect {
    void onApply(Player player);

    PowerUpType type();

    default void onTick(Player player) {}

    default void onExpire(Player player) {}

    boolean isExpired();
}
