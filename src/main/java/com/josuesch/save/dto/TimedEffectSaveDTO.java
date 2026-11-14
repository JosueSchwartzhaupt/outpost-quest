package com.josuesch.save.dto;

import com.josuesch.entities.powerups.PowerUpType;

public record TimedEffectSaveDTO(PowerUpType type, int remainingTicks) {}
