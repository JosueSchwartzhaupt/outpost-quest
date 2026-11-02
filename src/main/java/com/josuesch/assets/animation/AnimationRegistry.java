package com.josuesch.assets.animation;

import com.josuesch.graphics.Spritesheet;

import java.util.EnumMap;
import java.util.Map;

public class AnimationRegistry {
    private static final Map<AnimationType, AnimationData> CACHE = new EnumMap<>(AnimationType.class);

    // Chamado UMA ÚNICA VEZ na inicialização do jogo (ex: Game.init())
    public static void init(Spritesheet spritesheet) {
        // --- FIREMAN ---
        AnimationData fireman = new AnimationData();
        fireman.addAnimation("IDLE",  spritesheet.getSprites(0, 7, 16, 1));
        fireman.addAnimation("DOWN",  spritesheet.getSprites(0, 7, 16, 2));
        fireman.addAnimation("RIGHT", spritesheet.getSprites(2, 7, 16, 2));
        fireman.addAnimation("UP",    spritesheet.getSprites(4, 7, 16, 2));
        fireman.addAnimation("LEFT",  spritesheet.getSprites(6, 7, 16, 2));
        fireman.addAnimation("DEAD",  spritesheet.getSprites(0, 8, 16, 4));
        putInCache(AnimationType.FIREMAN, fireman);


        // --- MUDLIN ---
        AnimationData mudlin = new AnimationData();
        fireman.addAnimation("IDLE",  spritesheet.getSprites(0, 7, 16, 1));
        mudlin.addAnimation("DOWN", spritesheet.getSprites(0, 10, 16, 5));
        mudlin.addAnimation("HIDE", spritesheet.getSprites(0, 11, 16, 5));
        putInCache(AnimationType.MUDLIN, mudlin);

        // --- PLAYER ---
        AnimationData player = new AnimationData();
        fireman.addAnimation("IDLE",  spritesheet.getSprites(0, 0, 16, 1));
        fireman.addAnimation("DOWN",  spritesheet.getSprites(0, 0, 16, 4));
        fireman.addAnimation("RIGHT", spritesheet.getSprites(0, 2, 16, 4));
        fireman.addAnimation("UP",    spritesheet.getSprites(0, 3, 16, 4));
        fireman.addAnimation("LEFT",  spritesheet.getSprites(0, 1, 16, 4));
        fireman.addAnimation("IDLE_SHOOT",  spritesheet.getSprites(4, 0, 16, 1));
        fireman.addAnimation("DOWN_SHOOT",  spritesheet.getSprites(4, 0, 16, 4));
        fireman.addAnimation("RIGHT_SHOOT", spritesheet.getSprites(4, 2, 16, 4));
        fireman.addAnimation("UP_SHOOT",    spritesheet.getSprites(4, 3, 16, 4));
        fireman.addAnimation("LEFT_SHOOT",  spritesheet.getSprites(4, 1, 16, 4));
        putInCache(AnimationType.PLAYER, player);
    }

    public static AnimationData get(AnimationType type) {
        return CACHE.get(type) != null? CACHE.get(type) : new AnimationData();
    }

    private static void putInCache(AnimationType type,AnimationData data){
        if(!CACHE.containsKey(type)){
            CACHE.put(type, data);
        }
    }
}
