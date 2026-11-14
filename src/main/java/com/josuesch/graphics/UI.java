package com.josuesch.graphics;

import com.josuesch.Game;
import com.josuesch.assets.GameState;
import com.josuesch.assets.animation.Animator;
import com.josuesch.entities.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class UI {

    public static final BufferedImage FULL_HEART =
            Game.SPRITESHEET.getSprite(16 * 0, 16 * 9, 16, 16);
    public static final BufferedImage EMPTY_HEART =
            Game.SPRITESHEET.getSprite(16 * 1, 16 * 9, 16, 16);
    public static final BufferedImage POWER_UP_SHELL =
            Game.SPRITESHEET.getSprite(16 * 6, 16 * 5, 16, 16);

    public static final int BAR_SIZE = 100;

    private final Game game;

    public UI(Game game) {
        this.game = game;
    }

    public void render(Graphics g) {
        var health = game.getPlayer().getHealth();
        for (int i = 0; i < Player.MAX_HEALTH; i++) {
            if (i < health) g.drawImage(FULL_HEART, 16 + 11 * i, 4, null);
            else g.drawImage(EMPTY_HEART, 16 + 11 * i, 4, null);
        }

        GameGraphics.drawImage(g, POWER_UP_SHELL, 1, 0);

        var powerUp = game.getPlayer().getSavedPowerUp();
        if (powerUp != null) {
            GameGraphics.drawImage(g, Animator.getIdleIcon(powerUp.getAnimationType()), 1, 0);
        }

        g.setColor(new Color(119, 65, 64));
        g.fillRect(180, 4, BAR_SIZE, 9);
        g.setColor(new Color(225, 52, 38));
        int remainingBar =
                (int)
                        ((double)
                                        (game.getWave().getRemainingEnemiesSize()
                                                + game.getEnemies().size())
                                / (double) game.getWave().getTotalEnemiesSize()
                                * BAR_SIZE);
        g.fillRect(180, 4, remainingBar, 9);

        var state = game.getGameState();
        if (state == GameState.GAME_OVER) {
            String text = "GAME OVER";
            String text2 = "PRESS ENTER";
            String text3 = "TO TRY AGAIN";

            g.setColor(new Color(255, 0, 0, 70));
            g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            g.setColor(Color.WHITE);

            drawCentered(g, text, Game.HEIGHT / 2 - 15);
            drawCentered(g, text2, Game.HEIGHT / 2);
            drawCentered(g, text3, Game.HEIGHT / 2 + 15);
        }
        if (state == GameState.WIN) {
            String text = "YOU WIN";
            String text2 = "PRESS SPACE";
            String text3 = "TO START A NEW GAME";

            g.setColor(new Color(0, 255, 0, 70));
            g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            g.setColor(Color.WHITE);

            drawCentered(g, text, Game.HEIGHT / 2 - 15);
            drawCentered(g, text2, Game.HEIGHT / 2);
            drawCentered(g, text3, Game.HEIGHT / 2 + 15);
        }
    }

    private void drawCentered(Graphics g, String text, int y) {
        int x = Game.WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2;
        g.drawString(text, x, y);
    }
}
