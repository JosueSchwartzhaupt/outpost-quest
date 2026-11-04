package com.josuesch.entities.enemies;

import com.josuesch.Game;
import com.josuesch.assets.HitboxComparator;
import com.josuesch.entities.Enemy;

import java.util.Optional;

public class TeleportingMovement extends LinearMovement{

    private static final int TELEPORT_COOLDOWN = 300;
    private int teleportIdx = 150;

    private enum TeleportStatus{
        NORMAL,
        HIDING,
        APPEARING
    }

    TeleportStatus status = TeleportStatus.NORMAL;

    @Override
    public Optional<String> move(Enemy enemy) {
        Optional<String> r = Optional.empty();


        var player = enemy.getGame().getPlayer();

        if(teleportIdx>0)teleportIdx--;


        if (teleportIdx<=0 &&HitboxComparator.getDistance(enemy, player) > 30 &&  (status != TeleportStatus.NORMAL ||Game.RANDOM.nextInt(100)==0)) {
            switch (status){
                case NORMAL:
                    r = Optional.of("HIDE");
                    status = TeleportStatus.HIDING;
                    break;
                case HIDING:
                    r = Optional.of("HIDE");
                    if(enemy.getAnimator().hasCompletedAnimation()){
                        r = Optional.of("APPEAR");
                        var newLocation = enemy.getGame().getWorld().getRandomFloorTile();
                        enemy.setX(newLocation.getX());
                        enemy.setY(newLocation.getY());                        
                        status = TeleportStatus.APPEARING;
                    }
                    break;
                case APPEARING:
                    r = Optional.of("APPEAR");
                    if(enemy.getAnimator().hasCompletedAnimation()){
                        teleportIdx = TELEPORT_COOLDOWN;
                        status = TeleportStatus.NORMAL;
                    }
                    break;
            }
            return r;
        }



        return super.move(enemy);
    }
}
