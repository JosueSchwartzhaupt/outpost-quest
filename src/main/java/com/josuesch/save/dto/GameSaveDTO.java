package com.josuesch.save.dto;

import com.josuesch.level.Levels;
import java.util.List;

public record GameSaveDTO(
        Levels level,
        PlayerSaveDTO player,
        WaveSaveDTO wave,
        List<EnemySaveDTO> enemies,
        List<PowerUpSaveDTO> powerUps) {}
