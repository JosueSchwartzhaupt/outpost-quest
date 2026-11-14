package com.josuesch.entities;

import com.josuesch.Game;
import com.josuesch.assets.Placeble;
import com.josuesch.assets.animation.AnimationType;
import com.josuesch.assets.animation.Animator;
import com.josuesch.graphics.GameGraphics;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public abstract class Entity implements Placeble {

    protected double x;
    protected double y;

    protected int width;
    protected int height;

    protected final Game game;

    protected final Animator animator;

    protected int xSpriteOffset;
    protected int ySpriteOffset;

    public Entity(
            Game game, double x, double y, int width, int height, AnimationType animationType) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.animator = new Animator(animationType);
    }

    public abstract void tick();

    public void dispawn() {
        game.queueRemoval(this);
    }

    public void render(Graphics g) {
        BufferedImage sprite = animator.getCurrentSprite();
        if (sprite != null) {
            GameGraphics.drawImage(
                    g, sprite, this.getX() - xSpriteOffset, this.getY() - ySpriteOffset);
            return;
        }
        g.setColor(Color.RED);
        GameGraphics.fillRect(g, x, y, width, height);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean isColidable() {
        return false;
    }

    public Game getGame() {
        return game;
    }
}
