package com.josuesch.assets;

import com.josuesch.Game;
import com.josuesch.assets.animation.WaveType;
import com.josuesch.entities.enemies.EnemyFactory;
import com.josuesch.entities.enemies.EnemyType;
import com.josuesch.world.SpawnerTile;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Wave {

    private final WaveType type;
    private final List<EnemyType> remainingEnemies;
    private int waveTimer;
    private Game game;

    private int nextTickSpawnMark;

    public Wave(Game game, WaveType type) {
        this.game = game;
        this.type = type;
        this.waveTimer = type.getWaveTime();
        this.remainingEnemies = new ArrayList<>();

        type.getEnemyConfig()
                .forEach(
                        (enemyType, count) -> {
                            for (int i = 0; i < count; i++) {
                                this.remainingEnemies.add(enemyType);
                            }
                        });

        nextTickSpawnMark = waveTimer - Game.RANDOM.nextInt(3 * 60);
    }

    public void update() {
        if (isActive()) {
            waveTimer--;
            if (waveTimer <= nextTickSpawnMark) {
                getNextSpawn().ifPresent(this::spawnEnemy);
                nextTickSpawnMark = getNextTickSpawnMark();
            }
        }
    }

    private Optional<EnemyType> getNextSpawn() {
        if (remainingEnemies.isEmpty()) return Optional.empty();
        return Optional.of(remainingEnemies.remove(Game.RANDOM.nextInt(remainingEnemies.size())));
    }

    private Optional<SpawnerTile> getRandomSpawnLocation() {
        var spawnerTiles = game.getWorld().getSpawnerTiles();
        if (spawnerTiles.isEmpty()) return Optional.empty();
        return Optional.of(spawnerTiles.remove(Game.RANDOM.nextInt(spawnerTiles.size())));
    }

    private int getNextTickSpawnMark() {
        if (remainingEnemies.isEmpty()) return 0;
        int maxTicks = waveTimer / remainingEnemies.size();
        return waveTimer - Game.RANDOM.nextInt(maxTicks) + 1;
    }

    private void spawnEnemy(EnemyType enemyType) {
        var spawnLocationOpt = getRandomSpawnLocation();
        if (spawnLocationOpt.isEmpty()) return;

        var spawnLocation = spawnLocationOpt.get();

        game.addEnemy(
                EnemyFactory.create(game, enemyType, spawnLocation.getX(), spawnLocation.getY()));
    }

    public boolean isActive() {
        return waveTimer >= 0;
    }

    public int getWaveTimer() {
        return waveTimer;
    }
}
