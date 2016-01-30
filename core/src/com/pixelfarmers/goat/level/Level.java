package com.pixelfarmers.goat.level;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Level {

    private final Tile[][] tiles;
    private static final Tile nullTile = new Tile(1, true, 0, 0);
    private World world;

    public Level(Tile[][] tiles) {
        this.tiles = tiles;

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        world = new World(Vector2.Zero, true);
        for (int w = 0; w < width(); w++) {
            for (int h = 0; h < height(); h++) {
                Tile tile = tiles[h][w];
                if(tile.isSolid) {
                    groundBodyDef.position.set(new Vector2(tile.x, tile.y));
                    Body body = world.createBody(groundBodyDef);
                    PolygonShape groundBox = new PolygonShape();
                    groundBox.setAsBox(Tile.TILE_SIZE / 2, Tile.TILE_SIZE / 2);
                    body.createFixture(groundBox, 0.0f);
                }
            }
        }
    }

    public World getWorld() {
        return world;
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
