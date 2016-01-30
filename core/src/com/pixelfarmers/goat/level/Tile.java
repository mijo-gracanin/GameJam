package com.pixelfarmers.goat.level;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Tile {
    public static final int TILE_SIZE = 16;

    public enum Type {
        FLOOR(false),
        WALL(true);

        public final boolean isSolid;

        Type(boolean isSolid) {
            this.isSolid = isSolid;
        }
    }

    public boolean isSolid() {
        return type.isSolid;
    }

    public final Type type;
    public final float x, y;
    public final Rectangle boundingBox;
    public final Vector2 center;

    public Tile(Type type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.boundingBox = new Rectangle(x, y, TILE_SIZE, TILE_SIZE);
        this.center = new Vector2();

        boundingBox.getCenter(center);
    }
}
