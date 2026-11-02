package com.josuesch;

import com.josuesch.assets.Placeble;
import com.josuesch.entities.Enemy;
import com.josuesch.entities.Entity;
import com.josuesch.entities.Player;
import com.josuesch.graphics.Spritesheet;
import com.josuesch.graphics.UI;
import com.josuesch.world.World;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Canvas implements Runnable, KeyListener {

    private Thread thread;
    private boolean isRunning = false;

    public static final int WIDTH = 272;
    public static final int HEIGHT = 336;
    public static final int SCALE = 3;
    public static final Spritesheet SPRITESHEET = new Spritesheet("/spritesheet.png");
    public static final Random RANDOM = new Random();

    private BufferedImage image;

    private Player player;

    private final List<Entity> entities = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();
    private World world;
    private UI ui;

    private boolean started = false;
    private boolean gameOver = false;

    private final List<Placeble> collidablesBuffer = new ArrayList<>();

    private final List<Entity> entitiesToRemove = new ArrayList<>();

    public Game() {
        addKeyListener(this);
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        initFrame();

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        player = new Player(this, 100, 180, 32, 8);
        entities.add(player);
        world = new World(this, "/map.png");

        ui = new UI(this);
    }

    private void initFrame() {
        JFrame frame = new JFrame("Outpost  Quest");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        player = new Player(this, 100, 180, 32, 8);
        entities.add(player);
        world = new World(this, "/map.png");
        gameOver = false;
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
        if (hasStarted()) {
            for (int i = 0; i < entities.size(); i++) {
                entities.get(i).tick();
            }

            if (!entitiesToRemove.isEmpty()) {
                entities.removeAll(entitiesToRemove);
                enemies.removeAll(entitiesToRemove);
                entitiesToRemove.clear();
            }
        }

        if (player.isDead() || isWin()) {
            gameOver = true;
            started = false;
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

        bs.show();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (isGameOver()) {
                restart();
            }
            if (hasNotStarted()) {
                started = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.setRight(true);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            player.setLeft(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.setRight(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            player.setLeft(false);
        }
    }

    public Player getPlayer() {
        return player;
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

    public void addBall(Enemy enemy) {
        entities.add(enemy);
        enemies.add(enemy);
    }

    public void addAllEnemies(List<Enemy> newEnemies) {
        entities.addAll(newEnemies);
        enemies.addAll(newEnemies);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isWin() {
        return false;
    }

    public boolean hasNotStarted() {
        return !started;
    }

    public boolean hasStarted() {
        return started;
    }

    public void queueRemoval(Entity entity) {
        entitiesToRemove.add(entity);
    }
}
