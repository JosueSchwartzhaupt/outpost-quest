package com.josuesch.assets.animation;

import com.josuesch.graphics.GameGraphics;
import java.awt.image.BufferedImage;
import java.util.List;

public class Animator {

    private final AnimationData animationData;
    private String currentAnimState = "IDLE";

    private int currentFrame = 0;
    private int timer = 0;
    private int speed = 10; // Quantos ticks do jogo até mudar de frame

    private boolean temporaryRedMask;
    private int redMaskFrames = 0, maxRedMaskFrames = 10;

    private boolean completedAnimation;

    public Animator(AnimationType animationType) {

        this.animationData = AnimationRegistry.get(animationType);
    }

    public void setAnimation(String animState) {
        // Se a animação mudou, reseta o ciclo para o frame 0
        if (!this.currentAnimState.equals(animState)) {
            this.currentAnimState = animState;
            this.currentFrame = 0;
            this.timer = 0;
            completedAnimation = false;
        }
    }

    public void applyTemporaryRedMask() {
        temporaryRedMask = true;
    }

    public void update() {
        if (temporaryRedMask) {
            redMaskFrames++;
            if (redMaskFrames >= maxRedMaskFrames) {
                temporaryRedMask = false;
                redMaskFrames = 0;
            }
        }

        List<BufferedImage> frames = animationData.getFrames(currentAnimState);
        if (frames == null || frames.isEmpty()) return;

        timer++;
        if (timer >= speed - 1) {
            completedAnimation = currentFrame == frames.size() - 1;
        }
        if (timer >= speed) {
            timer = 0;
            currentFrame = (currentFrame + 1) % frames.size(); // Loop contínuo
        }
    }

    public BufferedImage getCurrentSprite() {
        List<BufferedImage> frames = animationData.getFrames(currentAnimState);
        if (frames == null || frames.isEmpty()) return null;
        var sprite = frames.get(currentFrame);
        if (temporaryRedMask) sprite = GameGraphics.applyRedMask(sprite);
        return sprite;
    }

    public boolean hasCompletedAnimation() {
        return completedAnimation;
    }
}
