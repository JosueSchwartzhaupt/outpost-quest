package com.josuesch.assets.animation;

import java.awt.image.BufferedImage;
import java.util.List;

public class Animator {

    private final AnimationData animationData;
    private String currentAnimState = "IDLE";

    private int currentFrame = 0;
    private int timer = 0;
    private int speed = 10; // Quantos ticks do jogo até mudar de frame

    public Animator(AnimationType animationType) {

        this.animationData = AnimationRegistry.get(animationType);
    }

    public void setAnimation(String animState) {
        // Se a animação mudou, reseta o ciclo para o frame 0
        if (!this.currentAnimState.equals(animState)) {
            this.currentAnimState = animState;
            this.currentFrame = 0;
            this.timer = 0;
        }
    }

    public void update() {
        List<BufferedImage> frames = animationData.getFrames(currentAnimState);
        if (frames == null || frames.isEmpty()) return;

        timer++;
        if (timer >= speed) {
            timer = 0;
            currentFrame = (currentFrame + 1) % frames.size(); // Loop contínuo
        }
    }

    public BufferedImage getCurrentSprite() {
        List<BufferedImage> frames = animationData.getFrames(currentAnimState);
        if (frames == null || frames.isEmpty()) return null;
        return frames.get(currentFrame);
    }
}
