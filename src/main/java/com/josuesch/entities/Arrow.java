package com.josuesch.entities;

import com.josuesch.Game;
import com.josuesch.assets.HitboxComparator;
import com.josuesch.assets.Movable;
import com.josuesch.assets.animation.AnimationType;
import com.josuesch.graphics.GameGraphics;
import com.josuesch.world.SpawnerTile;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Arrow extends Entity implements Movable {

    protected double ang;
    protected double spriteCorrection;
    protected double speed = 2.5;
    protected boolean isTraveling;

    public Arrow(Game game, double x, double y, double ang) {
        super(game, x, y, 16, 16, AnimationType.ARROW);
        this.ang = Math.toRadians(ang);
        setupHitboxAndOffsets(ang);

        if (isDiagonal(ang)) {
            animator.setAnimation("ROTATED");
            spriteCorrection = Math.toRadians(45);
        }

        isTraveling = true;
    }

    public void tick() {
        if (x > game.getWorld().WIDTH * 16 + 10
                || x < -16
                || y < -16
                || y > game.getWorld().HEIGHT * 16 + 10) {
            isTraveling = false;
            this.dispawn();
        }
        if (isTraveling) {
            x += Math.cos(ang) * speed;
            y += Math.sin(ang) * speed;
            final var hit =
                    game.getCollidables().stream()
                            .filter(c -> HitboxComparator.isColiding(this, c))
                            .filter(
                                    c ->
                                            !(c instanceof Player)
                                                    && !(c instanceof Arrow)
                                                    && !(c instanceof SpawnerTile))
                            .findFirst();
            hit.ifPresent(
                    e -> {
                        if (e instanceof Enemy) {
                            if (((Enemy) e).getHealth() > 0) {
                                ((Enemy) e).damage(1);
                                this.dispawn();
                            }
                        }
                        if (e.isColidable()) {
                            this.dispawn();
                        }
                    });
        }
    }

    public void render(Graphics g) {
        BufferedImage sprite = animator.getCurrentSprite();
        if (sprite != null) {
            GameGraphics.drawRotadedImage(
                    g, sprite, x - xSpriteOffset, y - ySpriteOffset, ang + spriteCorrection);
            return;
        }
        g.setColor(Color.RED);
        GameGraphics.fillRect(g, x, y, width, height);
    }

    @Override
    public double getAngle() {
        return 0;
    }

    @Override
    public double getSpeed() {
        return 0;
    }

    private static boolean isDiagonal(double angle) {
        double normalizedAngle = (angle % 360 + 360) % 360;

        int octante = (int) Math.floor((normalizedAngle + 22.5) / 45.0) % 8;

        // Octantes ímpares (1, 3, 5, 7) = Diagonais | Octantes pares (0, 2, 4, 6) = Retos
        return octante % 2 != 0;
    }

    private void setupHitboxAndOffsets(double angDegrees) {
        double normalized = (angDegrees % 360 + 360) % 360;
        int octant = (int) Math.floor((normalized + 22.5) / 45.0) % 8;

        switch (octant) {
            case 0 -> { // Direita (0°)
                this.width = 3;
                this.height = 5;
                this.xSpriteOffset = 11; // Ponta na extrema direita
                this.ySpriteOffset = 6; // Centralizado verticalmente
            }
            case 4 -> { // Esquerda (180°)
                this.width = 3;
                this.height = 5;
                this.xSpriteOffset = 2; // Ponta na extrema esquerda
                this.ySpriteOffset = 5;
            }
            case 2 -> { // Baixo (90°)
                this.width = 5;
                this.height = 3;
                this.xSpriteOffset = 5; // Centralizado horizontalmente
                this.ySpriteOffset = 11; // Ponta na extrema inferior
            }
            case 6 -> { // Cima (270°)
                this.width = 5;
                this.height = 3;
                this.xSpriteOffset = 6;
                this.ySpriteOffset = 2; // Ponta na extrema superior
            }
            case 1 -> { // Baixo-Direita (45°)
                this.width = 5;
                this.height = 5;
                this.xSpriteOffset = 8;
                this.ySpriteOffset = 8;
            }
            case 3 -> { // Baixo-Esquerda (135°)
                this.width = 5;
                this.height = 5;
                this.xSpriteOffset = 3;
                this.ySpriteOffset = 8;
            }
            case 5 -> { // Cima-Esquerda (225°)
                this.width = 5;
                this.height = 5;
                this.xSpriteOffset = 3;
                this.ySpriteOffset = 3;
            }
            case 7 -> { // Cima-Direita (315°)
                this.width = 5;
                this.height = 5;
                this.xSpriteOffset = 8;
                this.ySpriteOffset = 3;
            }
        }
        x += xSpriteOffset - 8;
        y += ySpriteOffset - 8;
    }
}
