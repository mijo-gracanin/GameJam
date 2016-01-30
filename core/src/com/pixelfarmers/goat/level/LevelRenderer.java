package com.pixelfarmers.goat.level;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LevelRenderer {
    private TextureRegion[] textures;

    public LevelRenderer() {
        Texture tileset = new Texture("tileset.png");
        int heightInTiles = tileset.getHeight() / Tile.TILE_SIZE;
        int widthInTiles = tileset.getWidth() / Tile.TILE_SIZE;
        textures = new TextureRegion[heightInTiles * widthInTiles];

        for (int row = 0; row < heightInTiles; row++) {
            for (int col = 0; col < widthInTiles; col++) {
                textures[row * widthInTiles + col] = new TextureRegion(tileset, col * Tile.TILE_SIZE, row * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }
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
        batch.draw(textures[tile.tilesetIndex - 1], x, y);
    }
}
