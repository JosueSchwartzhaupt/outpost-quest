package com.josuesch.save.dto;

import com.josuesch.entities.powerups.PowerUpType;

public record PowerUpSaveDTO(PowerUpType type, double x, double y, int lifeTime) {}
