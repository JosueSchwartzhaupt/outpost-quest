package com.josuesch.entities.enemies;

import com.josuesch.assets.HitboxComparator;
import com.josuesch.entities.Enemy;

import java.util.Optional;

public class MeleeAttack implements AttackBehavior {
    @Override
    public Optional<String> attack(Enemy enemy) {
        var player = enemy.getGame().getPlayer();

        if(HitboxComparator.getDistance(enemy, player) < 10){
            player.damage(1);
        }

        return Optional.empty();
    }
}
