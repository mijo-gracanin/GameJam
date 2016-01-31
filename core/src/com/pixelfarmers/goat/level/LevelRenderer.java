package com.pixelfarmers.goat.level;


import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.GameSettings;
import com.pixelfarmers.goat.MessageCode;
import com.pixelfarmers.goat.fx.BloodStain;

import java.util.ArrayList;
import java.util.List;

public class LevelRenderer implements Telegraph {
    private TextureRegion[] textures;
    private List<BloodStain> bloodStains;

    public LevelRenderer() {
        MessageManager.getInstance().addListener(this, MessageCode.ENEMY_DIED);
        Texture tileset = new Texture("tileset.png");
        int heightInTiles = tileset.getHeight() / Tile.TILE_SIZE;
        int widthInTiles = tileset.getWidth() / Tile.TILE_SIZE;
        textures = new TextureRegion[heightInTiles * widthInTiles];

        for (int row = 0; row < heightInTiles; row++) {
            for (int col = 0; col < widthInTiles; col++) {
                textures[row * widthInTiles + col] = new TextureRegion(tileset, col * Tile.TILE_SIZE, row * Tile.TILE_SIZE, Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }

        bloodStains = new ArrayList<BloodStain>();
    }

    public void render(SpriteBatch batch, Level level) {
        for (int col = 0; col < level.width(); col++) {
            for (int row = 0; row < level.height(); row++) {
                Tile tile = level.getTile(col, row);
                renderTile(batch, tile, tile.x, tile.y);
            }
        }
        for (BloodStain bloodStain : bloodStains) {
            bloodStain.render(batch);
        }
    }

    public void addBloodStain(float x, float y) {
        bloodStains.add(new BloodStain(x, y));
    }

    public void renderTile(SpriteBatch batch, Tile tile, float x, float y) {
        batch.draw(textures[tile.tilesetIndex - 1], x, y);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if(msg.message == MessageCode.ENEMY_DIED) {
            Vector2 pos = (Vector2) msg.extraInfo;
            if (GameSettings.getInstance().getBloodLevel() == GameSettings.BloodLevel.NORMAL) {
                addBloodStain(pos.x, pos.y);
            }
            return true;
        }
        return false;
    }

}
