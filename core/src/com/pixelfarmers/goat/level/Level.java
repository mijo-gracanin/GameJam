package com.pixelfarmers.goat.level;


public class Level {
    private final Tile[][] tiles;

    public Level(Tile[][] tiles) {
        this.tiles = tiles;
    }

    public Tile getTile(int x, int y) {
        return tiles[y][x];
    }

    public int width() {
        return tiles[0].length;
    }

    public int height() {
        return tiles.length;
    }
}
