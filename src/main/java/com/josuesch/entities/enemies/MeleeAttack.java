package com.josuesch.entities.enemies;

import com.josuesch.assets.HitboxComparator;
import com.josuesch.entities.Enemy;
import java.util.Optional;

public class MeleeAttack implements AttackBehavior {
    private int hitSpeed;
    private int damage;
    private int hitSpeedIdx;

    public MeleeAttack(int hitSpeed, int damage) {
        this.hitSpeed = hitSpeed;
        this.hitSpeedIdx = hitSpeed;
        this.damage = damage;
    }

    @Override
    public Optional<String> attack(Enemy enemy) {
        if (hitSpeedIdx < hitSpeed) {
            hitSpeedIdx++;
        }

        var player = enemy.getGame().getPlayer();

        if (HitboxComparator.getDistance(enemy, player) > 10) {
            return Optional.empty();
        }

        if (hitSpeedIdx >= hitSpeed) {
            hitSpeedIdx = 0;

            player.damage(damage);
        }

        return Optional.empty();
    }
}
