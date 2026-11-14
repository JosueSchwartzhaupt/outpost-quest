package com.josuesch.entities.powerups;

import com.josuesch.entities.Player;

public interface Effect {
    void onApply(Player player);

    default void onTick(Player player) {}

    default void onExpire(Player player) {}

    boolean isExpired();
}
