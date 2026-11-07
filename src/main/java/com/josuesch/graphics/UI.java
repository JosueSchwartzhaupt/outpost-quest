package com.josuesch.graphics;

import com.josuesch.Game;
import com.josuesch.assets.GameState;
import com.josuesch.entities.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class UI {

    public static final BufferedImage FULL_HEART =
            Game.SPRITESHEET.getSprite(16 * 0, 16 * 9, 16, 16);
    public static final BufferedImage EMPTY_HEART =
            Game.SPRITESHEET.getSprite(16 * 1, 16 * 9, 16, 16);

    public static final int BAR_SIZE = 100;

    private final Game game;

    public UI(Game game) {
        this.game = game;
    }

    public void render(Graphics g) {
        var health = game.getPlayer().getHealth();
        for (int i = 0; i < Player.MAX_HEALTH; i++) {
            if (i < health) g.drawImage(FULL_HEART, 4 + 11 * i, 4, null);
            else g.drawImage(EMPTY_HEART, 4 + 11 * i, 4, null);
        }
        g.setColor(new Color(119, 65, 64));
        g.fillRect(180, 4,BAR_SIZE, 9);
        g.setColor(new Color(225, 52, 38));
        int remainingBar = (int)((double)(game.getWave().getRemainingEnemiesSize() + game.getEnemies().size()) / (double)game.getWave().getTotalEnemiesSize() * BAR_SIZE);
        g.fillRect(180, 4,remainingBar, 9);

        var state = game.getGameState();
        if (state == GameState.GAME_OVER) {
            g.setColor(new Color(255, 0, 0, 50));
            g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            g.setColor(new Color(0, 0, 0));
            g.drawString("YOU DIED", 75, 90);
            g.drawString("PRESS ENTER", 60, 150);
            g.drawString("TO TRY AGAIN", 62, 165);
        }
        if (state == GameState.WIN) {
            g.setColor(new Color(0, 255, 0, 50));
            g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
            g.setColor(new Color(0, 0, 0));
            g.drawString("YOU WIN", 75, 90);
            g.drawString("PRESS ENTER", 60, 150);
            g.drawString("TO START AGAIN", 62, 165);
        }
    }

    private void drawCentered(Graphics g, String text, int y) {
        int x = Game.WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2;
        g.drawString(text, x, y);
    }
}
