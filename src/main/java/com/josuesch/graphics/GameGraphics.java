package com.josuesch.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GameGraphics {

    public static void fillRect(Graphics g, double x, double y, int width, int height) {
        g.fillRect((int) Math.round(x), (int) Math.round(y), width, height);
    }

    public static void drawImage(Graphics g, BufferedImage sprite, double x, double y) {
        g.drawImage(sprite, (int) Math.round(x), (int) Math.round(y), null);
    }

    public static void drawRotadedImage(
            Graphics g, BufferedImage sprite, double x, double y, double rotation) {
        Graphics2D g2 = (Graphics2D) g.create();

        int ix = (int) Math.round(x);
        int iy = (int) Math.round(y);

        double centerX = ix + (sprite.getWidth() / 2.0);
        double centerY = iy + (sprite.getHeight() / 2.0);

        g2.translate(centerX, centerY);
        g2.rotate(rotation);
        g2.drawImage(sprite, -sprite.getWidth() / 2, -sprite.getHeight() / 2, null);

        g2.dispose();
    }
}
