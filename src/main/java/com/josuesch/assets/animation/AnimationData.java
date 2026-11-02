package com.josuesch.assets.animation;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimationData {

    private final Map<String, List<BufferedImage>> animations = new HashMap<>();

    public void addAnimation(String stateName, List<BufferedImage> frames) {
        animations.put(stateName, frames);
    }

    public List<BufferedImage> getFrames(String stateName) {
        return animations.get(stateName);
    }
}
