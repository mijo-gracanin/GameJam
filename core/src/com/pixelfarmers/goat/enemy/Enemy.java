package com.pixelfarmers.goat.enemy;


import com.badlogic.gdx.ai.steer.SteerableAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy extends SteerableAdapter<Vector2> {
    public abstract boolean onHit(int damage);

    public abstract void update(float delta);

    public abstract void draw(Batch batch);
}
