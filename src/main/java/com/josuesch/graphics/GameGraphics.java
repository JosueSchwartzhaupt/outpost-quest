package com.josuesch.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
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

    public static BufferedImage applyRedMask(BufferedImage originalSprite) {
        BufferedImage newSprite = new BufferedImage(
                originalSprite.getWidth(), originalSprite.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = newSprite.createGraphics();
        g2d.drawImage(originalSprite, 0, 0, null);

        // Configura para misturar as cores preservando o corte do sprite
        g2d.setComposite(AlphaComposite.SrcAtop);

        // Altere o último número (0.5f) para ajustar a intensidade do vermelho (0.0f a 1.0f)
        g2d.setColor(new Color(1.0f, 0.0f, 0.0f, 0.5f));
        g2d.fillRect(0, 0, newSprite.getWidth(), newSprite.getHeight());

        g2d.dispose();
        return newSprite;
    }
}
