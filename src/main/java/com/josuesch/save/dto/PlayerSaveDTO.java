package com.josuesch.save.dto;

import com.josuesch.entities.powerups.PowerUpType;
import java.util.List;

public record PlayerSaveDTO(
        double x,
        double y,
        int health,
        PowerUpType savedPowerUp,
        List<TimedEffectSaveDTO> effectsSaveDTO) {}
