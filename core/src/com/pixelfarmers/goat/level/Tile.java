package com.pixelfarmers.goat.level;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Tile {
    public static final int TILE_SIZE = 16;

    public final int tilesetIndex;
    public final boolean isSolid;
    public final float x, y;
    public final Rectangle boundingBox;
    public final Vector2 center;

    public Tile(int tilesetIndex, boolean isSolid, float x, float y) {
        this.tilesetIndex = tilesetIndex;
        this.isSolid = isSolid;
        this.x = x;
        this.y = y;
        this.center = new Vector2();
        this.boundingBox = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);
        this.boundingBox.getCenter(center);
    }
}
