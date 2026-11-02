package com.josuesch.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class Spritesheet {
    private BufferedImage spritesheet;

    public Spritesheet(String path) {
        try {
            spritesheet = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getSprite(int x, int y, int width, int height) {
        return spritesheet.getSubimage(x, y, width, height);
    }

    public List<BufferedImage> getSprites(int startTileX, int tileY, int tileSize, int count) {
        List<BufferedImage> sprites = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            sprites.add(
                    getSprite((startTileX + i) * tileSize, tileY * tileSize, tileSize, tileSize));
        }
        return sprites;
    }
}
