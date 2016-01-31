package com.pixelfarmers.goat.intro;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class CameraPan extends Cinematic {
    private final Vector2 from, to;
    private final float speed;
    private float delay;

    private Vector2 currentPosition;

    public CameraPan(float duration, Vector2 from, Vector2 to, float speed, float delay) {
        super(duration);
        this.from = from;
        this.to = to;
        this.speed = speed;
        this.delay = delay;
        this.currentPosition = from.cpy();
    }

    @Override
    protected void updateImpl(float delta, Camera camera) {
        camera.position.set(currentPosition, 0);

        if (delay > 0) {
            delay -= delta;
            return;
        }

        currentPosition.x += (to.x - currentPosition.x) * speed;
        currentPosition.y += (to.y - currentPosition.y) * speed;
    }
}
