package com.pixelfarmers.goat.level;


public class Level {
    private final Tile[][] tiles;
    private static final Tile nullTile = new Tile(Tile.Type.FLOOR, 0, 0);

    public Level(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height()) {
            return nullTile;
        }
        return tiles[y][x];
    }

    public int width() {
        return tiles[0].length;
    }

    public int height() {
        return tiles.length;
    }
}
