package com.josuesch.entities;

import com.josuesch.Game;
import com.josuesch.assets.Movable;
import com.josuesch.entities.enemies.EnemyType;

public class Enemy extends Entity implements Movable {

    private final EnemyType type;

    public Enemy(Game game, EnemyType type, double x, double y, int width, int height) {
        super(game, x, y, width, height);
        this.type = type;
    }

    @Override
    public double getAngle() {
        return 0;
    }

    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    public void tick() {

    }

    public EnemyType getType() {
        return type;
    }
}
