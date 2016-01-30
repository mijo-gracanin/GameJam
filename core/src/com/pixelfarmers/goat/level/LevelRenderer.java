package com.pixelfarmers.goat.level;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LevelRenderer {
    private static final int TILE_SIZE = 16;

    private TextureRegion floor, wall;

    public LevelRenderer() {
        Texture tileset = new Texture("tileset.png");
        floor = new TextureRegion(tileset, 0, 0, TILE_SIZE, TILE_SIZE);
        wall = new TextureRegion(tileset, TILE_SIZE, 0, TILE_SIZE, TILE_SIZE);
    }

    public void render(SpriteBatch batch, Level level) {
        for (int x = 0; x < level.width(); x++) {
            for (int y = 0; y < level.height(); y++) {
                Tile tile = level.getTile(x, y);
                renderTile(batch, tile, TILE_SIZE * x, TILE_SIZE * y);
            }
        }
    }

    public void renderTile(SpriteBatch batch, Tile tile, int x, int y) {
        TextureRegion texture = tile == Tile.FLOOR ? floor : wall;
        batch.draw(texture, x, y);
    }
}
