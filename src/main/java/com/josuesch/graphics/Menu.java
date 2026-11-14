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

    private static final String[] INITIAL_OPTIONS = {"Novo Jogo", "Carregar Jogo", "Sair"};
    private static final String[] PAUSE_OPTIONS = {"Continuar", "Novo Jogo", "Salvar e Sair"};

    private int currentOption = 0;

    private boolean up, down, enter;

    private Image gameTitle;

    private final Game game;

    public Menu(Game game) {
        try {
            gameTitle = ImageIO.read(getClass().getResource("/GameTitle.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.game = game;
    }

    private String[] getCurrentOptions() {
        return game.hasStartedPlaying() ? PAUSE_OPTIONS : INITIAL_OPTIONS;
    }

    public void tick() {
        String[] options = getCurrentOptions();
        int maxOption = options.length - 1;

        if (currentOption > maxOption) {
            currentOption = 0;
        }

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
            handleOptionSelect(options);
        }
    }

    private void handleOptionSelect(String[] options) {
        if (game.hasStartedPlaying()) {
            switch (currentOption) {
                case 0: // Continuar
                    game.setGameState(GameState.RUNNING);
                    break;
                case 1: // Novo Jogo
                    game.restart();
                    break;
                case 2: // Salvar e Sair
                    game.exitGame();
                    break;
            }
        } else {
            switch (currentOption) {
                case 0: // Novo Jogo
                    game.setGameState(GameState.RUNNING);
                    break;
                case 1: // Carregar Jogo
                    game.loadGame();
                    break;
                case 2: // Sair
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

        // Opções do menu
        g.setColor(Color.WHITE);
        g.setFont(MENU_FONT);

        String[] options = getCurrentOptions();
        for (int i = 0; i < options.length; i++) {
            g.drawString(
                    options[i],
                    Game.WIDTH * Game.SCALE / 2 - 70,
                    Game.HEIGHT * Game.SCALE / 2 + 50 + (i * 40));
        }

        g.drawString(
                ">",
                Game.WIDTH * Game.SCALE / 2 - 90,
                Game.HEIGHT * Game.SCALE / 2 + 50 + (40 * currentOption));
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
