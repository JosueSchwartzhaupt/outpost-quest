package com.josuesch.entities.powerups.effects;

import com.josuesch.entities.Player;
import com.josuesch.entities.powerups.Effect;
import java.util.ArrayList;

public class BombEffect implements Effect {
    private boolean done = false;

    @Override
    public void onApply(Player player) {
        for (var enemy : new ArrayList<>(player.getGame().getEnemies())) {
            enemy.damage(2);
        }
        done = true;
    }

    @Override
    public boolean isExpired() {
        return done;
    }
}
