package com.josuesch.entities.enemies;

import com.josuesch.entities.Enemy;
import java.util.Optional;

public interface MovementBehavior {
    Optional<String> move(Enemy enemy);
}
