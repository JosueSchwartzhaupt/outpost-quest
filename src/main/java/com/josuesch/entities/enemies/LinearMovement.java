package com.josuesch.entities.enemies;

import com.josuesch.entities.Enemy;

public class LinearMovement implements MovementBehavior {

    @Override
    public String move(Enemy enemy) {
        return "IDLE";
    }
}
