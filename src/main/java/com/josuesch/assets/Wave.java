package com.josuesch.assets;

import com.josuesch.entities.Enemy;
import com.josuesch.entities.enemies.EnemyType;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum Wave {
    ;

    private final int waveTime;
    private final Map<EnemyType, List<Enemy>> enemies;

    Wave(int waveTime, List<Enemy>... enemies) {
        this.waveTime = waveTime;
        this.enemies = Arrays.stream(enemies)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Enemy::getType));
    }
}
