package com.josuesch.save.dto;

import com.josuesch.entities.enemies.EnemyType;
import java.util.List;

public record WaveSaveDTO(int waveTimer, int nextTickSpawnMark, List<EnemyType> remainingEnemies) {}
