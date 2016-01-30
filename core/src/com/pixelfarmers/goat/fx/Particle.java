package com.pixelfarmers.goat.fx;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Particle {
    void update(float delta);
    void render(SpriteBatch batch);
    boolean isAlive();
}
