package com.josuesch.world;

import com.josuesch.Game;
import com.josuesch.assets.Placeble;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class World {

    private Tile[] tiles;
    private final List<Placeble> solidTiles = new ArrayList<>();
    private final List<SpawnerTile> spawnerTiles = new ArrayList<>();
    public int WIDTH, HEIGHT;

    private final Game game;

    public World(Game g, String path) {
        this.game = g;
        solidTiles.clear();
        try {
            BufferedImage map = ImageIO.read(getClass().getResource(path));
            WIDTH = map.getWidth();
            HEIGHT = map.getHeight();
            int[] pixels = new int[WIDTH * HEIGHT];
            tiles = new Tile[WIDTH * HEIGHT];
            map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
            for (int xx = 0; xx < WIDTH; xx++) {
                for (int yy = 0; yy < HEIGHT; yy++) {
                    int x = xx * 16;
                    int y = yy * 16;
                    int pixel = pixels[xx + (yy * WIDTH)];

                    if (pixel == 0xFFFFFFFF) {
                        WallTile wall = new WallTile(x, y, Tile.TILE_WALL);
                        tiles[xx + (yy * WIDTH)] = wall;
                        solidTiles.add(wall);
                        continue;
                    }
                    if (pixel == 0xFFA5A5A5) {
                        SpawnerTile spawnerTile = new SpawnerTile(x, y, Tile.TILE_SPAWNER);
                        tiles[xx + (yy * WIDTH)] = spawnerTile;
                        spawnerTiles.add(spawnerTile);
                        continue;
                    }

                    if (pixel == 0xFF0026FF) {
                        game.getPlayer().setX(x - game.getPlayer().getWidth() / 2.0);
                        game.getPlayer().setY(y);
                    }

                    tiles[xx + (yy * WIDTH)] =
                            new FloorTile(x, y, Tile.TILE_FLOOR, Game.RANDOM.nextInt(4));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        int xfinal = Math.min(WIDTH, (Game.WIDTH >> 4) + 1);
        int yfinal = Math.min(HEIGHT, (Game.HEIGHT >> 4) + 1);

        for (int xx = 0; xx < xfinal; xx++) {
            for (int yy = 0; yy < yfinal; yy++) {
                tiles[xx + yy * WIDTH].render(g);
            }
        }
    }

    public List<Placeble> getSolidTiles() {
        return solidTiles;
    }

    public List<SpawnerTile> getSpawnerTiles() {
        return spawnerTiles;
    }
}
