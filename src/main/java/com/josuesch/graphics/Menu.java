package com.josuesch.graphics;

import com.josuesch.Game;
import com.josuesch.assets.GameState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Menu {

    private static final Color MENU_OVERLAY_COLOR = new Color(0, 0, 0, 125);
    private static final Font MENU_FONT = new Font("arial", Font.BOLD, 24);

    private String[] options = {"novo jogo", "carregar jogo", "sair"};

    private int currentOption = 0;
    private int maxOption = options.length - 1;

    private boolean up, down, enter;

    private Image gameTitle;

    private Game game;

    public Menu(Game game) {
        try {
            gameTitle = ImageIO.read(getClass().getResource("/GameTitle.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.game = game;
    }

    public void tick() {
        if (up) {
            currentOption--;
            if (currentOption < 0) currentOption = maxOption;
            up = false;
        }
        if (down) {
            currentOption++;
            if (currentOption > maxOption) currentOption = 0;
            down = false;
        }
        if (enter) {
            enter = false;
            switch (currentOption) {
                case 0:
                    game.setGameState(GameState.RUNNING);
                    break;
                case 1:
                    game.loadGame();
                    break;
                case 2:
                    game.exitGame();
                    break;
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(MENU_OVERLAY_COLOR);
        g.fillRect(0, 0, Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE);

        g.drawImage(
                gameTitle,
                Game.WIDTH * Game.SCALE / 2 - gameTitle.getWidth(null) / 2 + 30,
                Game.HEIGHT * Game.SCALE / 2 - gameTitle.getHeight(null) / 2 - 100,
                null);

        // Opçoes do menu
        g.setColor(Color.WHITE);
        g.setFont(MENU_FONT);
        g.drawString(
                "Novo Jogo",
                Game.WIDTH * Game.SCALE / 2 - 70,
                Game.HEIGHT * Game.SCALE / 2 + 50 + 0);
        g.drawString(
                "Carregar Jogo",
                Game.WIDTH * Game.SCALE / 2 - 70,
                Game.HEIGHT * Game.SCALE / 2 + 50 + 40);
        g.drawString(
                "Sair", Game.WIDTH * Game.SCALE / 2 - 70, Game.HEIGHT * Game.SCALE / 2 + 50 + 80);

        g.drawString(
                ">",
                Game.WIDTH * Game.SCALE / 2 - 90,
                Game.HEIGHT * Game.SCALE / 2 + 50 + 40 * currentOption);
    }

    public void setEnter(boolean enter) {
        this.enter = enter;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setUp(boolean up) {
        this.up = up;
    }
}
