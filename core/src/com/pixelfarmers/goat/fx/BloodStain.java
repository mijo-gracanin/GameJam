package com.pixelfarmers.goat.fx;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class BloodStain {
    private final float x, y;
    private final Texture texture;

    private static Texture[] textures = {
            TextureRegistry.getInstance().getTexture("blood_stain_1"),
            TextureRegistry.getInstance().getTexture("blood_stain_2")
    };

    public BloodStain(float x, float y) {
        this.x = x;
        this.y = y;
        this.texture = textures[MathUtils.random(1)];
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }
}
