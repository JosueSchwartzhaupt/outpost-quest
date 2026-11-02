package com.josuesch.world;

import com.josuesch.graphics.GameGraphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class FloorTile extends Tile {
    private double rotation;

    public FloorTile(int x, int y, BufferedImage sprite, int rotation) {
        super(x, y, sprite);
        this.rotation = Math.toRadians(rotation*90);
    }

    public void render(Graphics g) {
        GameGraphics.drawRotadedImage(g, sprite, x, y, rotation);
    }

    @Override
    public boolean isColidable() {
        return false;
    }
}
