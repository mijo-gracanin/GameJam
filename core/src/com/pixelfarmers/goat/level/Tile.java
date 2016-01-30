package com.pixelfarmers.goat.level;


public enum Tile {
    FLOOR(false),
    WALL(true);

    public final boolean isSolid;

    Tile(boolean isSolid) {
        this.isSolid = isSolid;
    }
}
