package com.pixelfarmers.goat.fx;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BloodParticle implements Particle {
    private float secondsToLive;
    private Vector2 direction;
    private float gravity;
    private Texture texture;
    private float delay;

    private float x, y;
    private float width, height;

    public BloodParticle(float x, float y) {
        this.x = x;
        this.y = y;
        this.texture = TextureRegistry.getInstance().getTexture("blood_particle");

        secondsToLive = 0.4f + 0.1f*MathUtils.random();
        delay = 0.1f*MathUtils.random();
        width = 6f + 2*MathUtils.random();
        height = 6f + 2*MathUtils.random();
        direction = new Vector2(100*(-0.5f + MathUtils.random()), 200);
        gravity = 900f;
    }

    @Override
    public void update(float delta) {
        if (delay < 0) {
            secondsToLive -= delta;
            x += direction.x * delta;
            y += direction.y * delta;
            direction.y -= delta * gravity;
        } else {
            delay -= delta;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (delay < 0) {
            batch.draw(texture, x, y, width, height);
        }
    }

    @Override
    public boolean isAlive() {
        return secondsToLive > 0;
    }
}
