package com.pixelfarmers.goat.fx;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BloodParticle implements Particle {
    private float secondsToLive;
    private Vector2 direction;
    private float speed;
    private float gravity;
    private Texture texture;

    private float x, y;

    public BloodParticle(float x, float y) {
        this.x = x;
        this.y = y;
        this.texture = TextureRegistry.getInstance().getTexture("blood_particle");

        secondsToLive = 1f + 2*MathUtils.random();
        direction = new Vector2(-0.5f + MathUtils.random(), -0.5f + MathUtils.random()).nor();
        speed = 8f + 4*MathUtils.random();
        gravity = -0.05f - (MathUtils.random() / 10);
    }

    @Override
    public void update(float delta) {
        secondsToLive -= delta;
        x += direction.x * delta * speed;
        y += direction.y * delta * speed;
        direction.y -= delta * gravity;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    @Override
    public boolean isAlive() {
        return secondsToLive > 0;
    }
}
