package com.josuesch.entities.enemies;

import com.josuesch.entities.Enemy;

import java.util.Optional;

public class LinearMovement implements MovementBehavior {

    @Override
    public Optional<String> move(Enemy enemy) {
        return Optional.of("IDLE");
    }
}
