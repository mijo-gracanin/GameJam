package com.pixelfarmers.goat.level;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LevelRenderer {
    private TextureRegion floor, wall;

    public LevelRenderer() {
        Texture tileset = new Texture("tileset.png");
        floor = new TextureRegion(tileset, 0, 0, Tile.TILE_SIZE, Tile.TILE_SIZE);
        wall = new TextureRegion(tileset, Tile.TILE_SIZE, 0, Tile.TILE_SIZE, Tile.TILE_SIZE);
    }

    public void render(SpriteBatch batch, Level level) {
        for (int col = 0; col < level.width(); col++) {
            for (int row = 0; row < level.height(); row++) {
                Tile tile = level.getTile(col, row);
                renderTile(batch, tile, tile.x, tile.y);
            }
        }
    }

    public void renderTile(SpriteBatch batch, Tile tile, float x, float y) {
        TextureRegion texture = tile.type == Tile.Type.FLOOR ? floor : wall;
        batch.draw(texture, x, y);
    }
}
