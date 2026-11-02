package com.josuesch.world;

import java.awt.image.BufferedImage;

public class SpawnerTile extends Tile {
    public SpawnerTile(int x, int y, BufferedImage sprite) {
        super(x, y, sprite);
    }

    @Override
    public boolean isColidable() {
        return true;
    }
}
