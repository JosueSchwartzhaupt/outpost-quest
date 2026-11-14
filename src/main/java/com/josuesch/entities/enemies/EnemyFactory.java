package com.josuesch.entities.enemies;

import com.josuesch.Game;
import com.josuesch.entities.Enemy;
import com.josuesch.save.dto.EnemySaveDTO;

public class EnemyFactory {

    public static Enemy create(Game game, EnemyType type, double x, double y) {
        if (type == null) {
            return null;
        }
        return type.instantiate(game, x, y);
    }

    public static Enemy create(Game game, EnemySaveDTO dto) {
        if (dto == null) {
            return null;
        }
        Enemy enemy = create(game, dto.type(), dto.x(), dto.y());
        if (enemy != null) {
            enemy.setHealth(dto.health());
        }
        return enemy;
    }
}
