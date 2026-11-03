package com.josuesch.entities.enemies;

import com.josuesch.Game;
import com.josuesch.assets.HitboxComparator;
import com.josuesch.entities.Enemy;

import java.util.Optional;

public class TeleportingMovement extends LinearMovement{

    private static final int TELEPORT_COOLDOWN = 600;
    private int teleportIdx = 0;

    private enum TeleportStatus{
        NORMAL,
        HIDING,
        APPEARING
    }

    TeleportStatus status = TeleportStatus.NORMAL;

    @Override
    public Optional<String> move(Enemy enemy) {

        var player = enemy.getGame().getPlayer();

        if(teleportIdx>0)teleportIdx--;


        if (teleportIdx<=0 &&HitboxComparator.getDistance(enemy, player) > 30 && Game.RANDOM.nextInt(100)==0) {
            switch (status){
                case NORMAL:
                    enemy.getAnimator().setAnimation("HIDE");
                    status = TeleportStatus.HIDING;
                    break;
                case HIDING:
                    if(enemy.getAnimator().hasCompletedAnimation()){
                        enemy.getAnimator().setAnimation("APPEAR");
                        status = TeleportStatus.APPEARING;
                    }
                    break;
                case APPEARING:
                    if(enemy.getAnimator().hasCompletedAnimation()){
                        teleportIdx = TELEPORT_COOLDOWN;
                        var newLocation = enemy.getGame().getWorld().getRandomFloorTile();
                        enemy.setX(newLocation.getX());
                        enemy.setY(newLocation.getY());
                        status = TeleportStatus.NORMAL;
                    }
                    break;
            }
            return Optional.empty();
        }



        return super.move(enemy);
    }
}
