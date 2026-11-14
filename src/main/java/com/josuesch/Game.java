package com.josuesch;

import com.josuesch.assets.GameState;
import com.josuesch.assets.Levels;
import com.josuesch.assets.Placeble;
import com.josuesch.assets.Wave;
import com.josuesch.assets.animation.AnimationRegistry;
import com.josuesch.entities.Enemy;
import com.josuesch.entities.Entity;
import com.josuesch.entities.Player;
import com.josuesch.entities.enemies.EnemyFactory;
import com.josuesch.entities.powerups.PowerUp;
import com.josuesch.graphics.Menu;
import com.josuesch.graphics.Spritesheet;
import com.josuesch.graphics.UI;
import com.josuesch.save.SaveManager;
import com.josuesch.save.dto.EnemySaveDTO;
import com.josuesch.save.dto.GameSaveDTO;
import com.josuesch.save.dto.PowerUpSaveDTO;
import com.josuesch.world.World;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener {

    private Thread thread;
    private boolean isRunning = false;

    public static final int WIDTH = 304;
    public static final int HEIGHT = 304;
    public static final int SCALE = 3;
    public static final Spritesheet SPRITESHEET = new Spritesheet("/spritesheet.png");
    public static final Random RANDOM = new Random();

    private BufferedImage image;

    private Player player;

    private final List<Entity> entities = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private World world;
    private UI ui;
    private Wave wave;
    private Menu menu;

    private final List<Placeble> collidablesBuffer = new ArrayList<>();

    private final List<Entity> entitiesToRemove = new ArrayList<>();

    private GameState gameState;

    private Levels level;
    private boolean hasStartedPlaying = false;

    public Game() {
        gameState = GameState.MENU;
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();
        AnimationRegistry.init(SPRITESHEET);

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        player = new Player(this, 100, 180);
        entities.add(player);
        level = Levels.LEVEL_1;
        world = new World(this, level.getMapPath());
        wave = new Wave(this, level.getWaveType());

        ui = new UI(this);

        menu = new Menu(this);
    }

    private void initFrame() {
        JFrame frame = new JFrame("Outpost  Quest");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(
                new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        exitGame();
                    }
                });
        frame.setVisible(true);
    }

    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        entities.clear();
        enemies.clear();
        player = new Player(this, 100, 180);
        entities.add(player);
        level = Levels.LEVEL_1;
        world = new World(this, level.getMapPath());
        wave = new Wave(this, level.getWaveType());
        hasStartedPlaying = true;
        gameState = GameState.RUNNING;
    }

    public void goToNextLevel() {
        entities.clear();
        enemies.clear();
        entities.add(player);
        level = level.getNextLevel();
        if (level == null) {
            gameState = GameState.WIN;
            return;
        }
        world = new World(this, level.getMapPath());
        wave = new Wave(this, level.getWaveType());
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        double timer = System.currentTimeMillis();
        requestFocus();
        while (isRunning) {

            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
            if (System.currentTimeMillis() - timer >= 1000) {
                timer += 1000;
            }
        }
        stop();
    }

    private void tick() {
        switch (gameState) {
            case MENU:
                menu.tick();
                break;
            case RUNNING:
                wave.update();
                for (int i = 0; i < entities.size(); i++) {
                    entities.get(i).tick();
                }

                if (!entitiesToRemove.isEmpty()) {
                    entities.removeAll(entitiesToRemove);
                    enemies.removeAll(entitiesToRemove);
                    entitiesToRemove.clear();
                }

                if (player.isDead()) {
                    gameState = GameState.GAME_OVER;
                }
                if (enemies.isEmpty() && !wave.isActive()) {
                    goToNextLevel();
                }
                break;
        }
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = image.getGraphics();

        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        world.render(g);

        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).render(g);
        }

        ui.render(g);

        g.dispose();

        g = bs.getDrawGraphics();
        // bota essa tela de fundo no buffer(escalando para a tela)
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);

        if (gameState == GameState.MENU) menu.render(g);

        bs.show();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (gameState) {
            case RUNNING:
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    player.setRight(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    player.setLeft(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    player.setUp(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    player.setDown(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player.setShootRight(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player.setShootLeft(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    player.setShootUp(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    player.setShootDown(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    player.setUsePowerUp(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    player.setRight(false);
                    player.setLeft(false);
                    player.setUp(false);
                    player.setDown(false);
                    gameState = GameState.MENU;
                }
                break;
            case MENU:
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
                    menu.setUp(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
                    menu.setDown(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    menu.setEnter(true);
                }
                break;
            default:
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    restart();
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.setRight(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            player.setLeft(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_W) {
            player.setUp(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            player.setDown(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.setShootRight(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            player.setShootLeft(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            player.setShootUp(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            player.setShootDown(false);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }

    public Wave getWave() {
        return wave;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Placeble> getCollidables() {
        collidablesBuffer.clear();
        collidablesBuffer.addAll(world.getSolidTiles());
        collidablesBuffer.addAll(entities);
        return collidablesBuffer;
    }

    public List<Placeble> getPlayerCollidables() {
        collidablesBuffer.clear();
        collidablesBuffer.addAll(world.getSolidTiles());
        collidablesBuffer.addAll(world.getSpawnerTiles());
        collidablesBuffer.addAll(entities);
        return collidablesBuffer;
    }

    public void addEnemy(Enemy enemy) {
        entities.add(enemy);
        enemies.add(enemy);
    }

    public void addAllEnemies(List<Enemy> newEnemies) {
        entities.addAll(newEnemies);
        enemies.addAll(newEnemies);
    }

    public void queueRemoval(Entity entity) {
        entitiesToRemove.add(entity);
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState state) {
        if (state == GameState.RUNNING) {
            this.hasStartedPlaying = true;
        }
        this.gameState = state;
    }

    public GameSaveDTO toSaveDTO() {
        List<EnemySaveDTO> enemyDTOs = enemies.stream().map(Enemy::toSaveDTO).toList();
        List<PowerUpSaveDTO> powerUpDTOs =
                entities.stream()
                        .filter(PowerUp.class::isInstance)
                        .map(PowerUp.class::cast)
                        .map(PowerUp::toSaveDTO)
                        .toList();
        return new GameSaveDTO(level, player.toSaveDTO(), wave.toSaveDTO(), enemyDTOs, powerUpDTOs);
    }

    public boolean saveGame() {
        if (player == null
                || player.isDead()
                || gameState == GameState.GAME_OVER
                || gameState == GameState.WIN) {
            System.out.println("Só é possível salvar durante uma partida ativa!");
            return false;
        }
        return SaveManager.save(this.toSaveDTO());
    }

    public void exitGame() {
        try {
            if (hasStartedPlaying
                    && player != null
                    && !player.isDead()
                    && gameState != GameState.GAME_OVER
                    && gameState != GameState.WIN) {
                System.out.println("Salvando o jogo antes de fechar...");
                saveGame();
            }
        } catch (Exception ex) {
            System.err.println("Erro ao salvar no fechamento: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            isRunning = false;
            System.exit(0);
        }
    }

    public boolean loadGame() {
        var saveOpt = SaveManager.load();
        if (saveOpt.isEmpty()) {
            System.out.println("Nenhum savestate para carregar!");
            return false;
        }

        GameSaveDTO save = saveOpt.get();

        entities.clear();
        enemies.clear();
        entitiesToRemove.clear();

        // Restaura fase e mundo
        this.level = save.level();
        this.world = new World(this, level.getMapPath());

        // Restaura Player
        this.player = new Player(this, save.player());
        this.entities.add(player);

        // Restaura Wave
        this.wave = new Wave(this, level.getWaveType(), save.wave());

        // Restaura Inimigos vivos
        for (EnemySaveDTO enemyDTO : save.enemies()) {
            Enemy enemy = EnemyFactory.create(this, enemyDTO);
            if (enemy != null) {
                addEnemy(enemy);
            }
        }

        for (PowerUpSaveDTO powerUpDTO : save.powerUps()) {
            this.entities.add(new PowerUp(this, powerUpDTO));
        }

        this.hasStartedPlaying = true;
        this.gameState = GameState.RUNNING;
        System.out.println("Savestate restaurado com sucesso!");
        return true;
    }
}
