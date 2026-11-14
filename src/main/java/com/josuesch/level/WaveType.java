package com.josuesch.level;

import static com.josuesch.entities.enemies.EnemyType.BAT;
import static com.josuesch.entities.enemies.EnemyType.FIREMAN;
import static com.josuesch.entities.enemies.EnemyType.MUDLIN;

import com.josuesch.entities.enemies.EnemyType;
import java.util.Collections;
import java.util.Map;

public enum WaveType {
    FIRST(
            40 * 60,
            Map.of(
                    FIREMAN, 15,
                    MUDLIN, 5)),
    SECOND(
            70 * 60,
            Map.of(
                    FIREMAN, 25,
                    MUDLIN, 10,
                    BAT, 10)),
    THIRD(70 * 60, Map.of(FIREMAN, 20, MUDLIN, 25, BAT, 15));

    private final int waveTime;
    private final Map<EnemyType, Integer> enemyConfig;

    WaveType(int waveTime, Map<EnemyType, Integer> enemyConfig) {
        this.waveTime = waveTime;
        this.enemyConfig = Collections.unmodifiableMap(enemyConfig);
    }

    public int getWaveTime() {
        return waveTime;
    }

    public Map<EnemyType, Integer> getEnemyConfig() {
        return enemyConfig;
    }

    public int getWaveSize() {
        return enemyConfig.values().stream().mapToInt(e -> e).sum();
    }
}
