package com.josuesch.save.dto;

import com.josuesch.entities.enemies.EnemyType;

public record EnemySaveDTO(EnemyType type, double x, double y, int health) {}
