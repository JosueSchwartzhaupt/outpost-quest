package com.josuesch.level;

import com.josuesch.Game;
import com.josuesch.entities.enemies.EnemyFactory;
import com.josuesch.entities.enemies.EnemyType;
import com.josuesch.save.dto.WaveSaveDTO;
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

    public Wave(Game game, WaveType type, WaveSaveDTO dto) {
        this.game = game;
        this.type = type;
        this.waveTimer = dto.waveTimer();
        this.nextTickSpawnMark = dto.nextTickSpawnMark();
        this.remainingEnemies = new ArrayList<>(dto.remainingEnemies());
    }

    public void update() {
        if (isActive()) {
            waveTimer--;
            if (waveTimer <= nextTickSpawnMark) {
                getNextSpawn().ifPresent(this::spawnEnemy);
                nextTickSpawnMark = calculateNextTickSpawnMark();
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
        return Optional.of(spawnerTiles.get(Game.RANDOM.nextInt(spawnerTiles.size())));
    }

    private int calculateNextTickSpawnMark() {
        if (remainingEnemies.size() <= 1) return 0;

        int interval = waveTimer / remainingEnemies.size();

        int delay = Game.RANDOM.nextInt(Math.max(1, interval * 2));

        return Math.max(0, waveTimer - delay);
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

    public int getRemainingEnemiesSize() {
        return remainingEnemies.size();
    }

    public int getTotalEnemiesSize() {
        return type.getWaveSize();
    }

    public WaveSaveDTO toSaveDTO() {
        return new WaveSaveDTO(waveTimer, nextTickSpawnMark, new ArrayList<>(remainingEnemies));
    }
}
