package com.josuesch.entities.enemies;

import com.josuesch.entities.Enemy;

import java.util.Optional;

public interface AttackBehavior {
    Optional<String> attack(Enemy enemy);
}
