package com.josuesch.assets;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class HitboxComparator {

    public static boolean isColiding(Placeble p1, Placeble p2) {
        return isColiding(
                p1.getX(), p1.getY(),
                p1.getWidth(), p1.getHeight(),
                p2.getX(), p2.getY(),
                p2.getWidth(), p2.getHeight());
    }

    public static boolean willColide(Movable m, Placeble p) {
        double newX = m.getX() + m.getSpeed() * Math.cos(m.getAngle());
        double newY = m.getY() + m.getSpeed() * Math.sin(m.getAngle());

        return isColiding(
                newX,
                newY,
                m.getWidth(),
                m.getHeight(),
                p.getX(),
                p.getY(),
                p.getWidth(),
                p.getHeight());
    }

    private static boolean isColiding(
            double tx,
            double ty,
            double tw,
            double th,
            double rx,
            double ry,
            double rw,
            double rh) {

        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }

        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx)
                && (rh < ry || rh > ty)
                && (tw < tx || tw > rx)
                && (th < ty || th > ry));
    }

    public static Collision getCollision(Movable m, Placeble p) {
        double dx = m.getSpeed() * Math.cos(m.getAngle());
        double dy = m.getSpeed() * Math.sin(m.getAngle());

        double maxDx = dx;
        double maxDy = dy;

        // 1. Checa colisão individual em X e Y
        boolean collideX = isColiding(
                m.getX() + dx, m.getY(),
                m.getWidth(), m.getHeight(),
                p.getX(), p.getY(), p.getWidth(), p.getHeight()
        );

        boolean collideY = isColiding(
                m.getX(), m.getY() + dy,
                m.getWidth(), m.getHeight(),
                p.getX(), p.getY(), p.getWidth(), p.getHeight()
        );

        if (collideX) {
            maxDx = (dx > 0)
                    ? Math.max(0, p.getX() - (m.getX() + m.getWidth())) // Direita
                    : -Math.max(0, m.getX() - (p.getX() + p.getWidth()));
        }

        if (collideY) {
            maxDy = (dy > 0)
                    ? Math.max(0, p.getY() - (m.getY() + m.getHeight())) // Baixo
                    : -Math.max(0, m.getY() - (p.getY() + p.getHeight()));
        }

        // 2. Colisão exclusiva na quina exata (ambos limpos isoladamente, mas juntos colidem)
        if (!collideX && !collideY) {
            double distX = (dx > 0) ? p.getX() - (m.getX() + m.getWidth()) : m.getX() - (p.getX() + p.getWidth());
            double distY = (dy > 0) ? p.getY() - (m.getY() + m.getHeight()) : m.getY() - (p.getY() + p.getHeight());

            if (distX < distY) {
                maxDx = (dx > 0) ? Math.max(0, distX) : -Math.max(0, distX);
            } else {
                maxDy = (dy > 0) ? Math.max(0, distY) : -Math.max(0, distY);
            }
        }

        if(Math.abs(maxDy)<0.000001)maxDy=0;
        if(Math.abs(maxDx)<0.000001)maxDx=0;

        return new Collision(p, maxDx, maxDy);
    }

    public static Optional<Collision> processCollision(Movable m, List<Placeble> placebles) {
        return placebles.stream()
                .filter(Placeble::isColidable)
                .filter(p -> !p.equals(m))
                .filter(p -> willColide(m, p))
                .map(p -> getCollision(m, p))
                .reduce((c1, c2) -> new Collision(
                        c2.hit(),
                        Math.abs(c1.maxDxSpeed()) < Math.abs(c2.maxDxSpeed()) ? c1.maxDxSpeed() : c2.maxDxSpeed(),
                        Math.abs(c1.maxDySpeed()) < Math.abs(c2.maxDySpeed()) ? c1.maxDySpeed() : c2.maxDySpeed()
                ));
    }

    public static double getDistance(Placeble p1, Placeble p2) {
        double p1X = p1.getX() + p1.getWidth() / 2.0;
        double p1Y = p1.getY() + p1.getHeight() / 2.0;
        double p2X = p2.getX() + p2.getWidth() / 2.0;
        double p2Y = p2.getY() + p2.getHeight() / 2.0;

        return Math.sqrt(Math.pow(p1X - p2X, 2) + Math.pow(p1Y - p2Y, 2));
    }
}
