package com.pixelfarmers.goat.fx;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BloodStain {
    private final float x, y;
    private Texture texture;

    public BloodStain(float x, float y) {
        this.x = x;
        this.y = y;
        this.texture = TextureRegistry.getInstance().getTexture("blood_stain");
    }

    public void update(float delta) {

    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }
}
