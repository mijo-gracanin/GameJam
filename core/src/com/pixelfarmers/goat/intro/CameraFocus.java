package com.pixelfarmers.goat.intro;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

public class CameraFocus extends Cinematic {
    private final Vector2 position;

    public CameraFocus(float duration, float x, float y) {
        super(duration);
        this.position = new Vector2(x, y);
    }

    @Override
    protected void updateImpl(float delta, Camera camera) {
        camera.position.set(position, 0);
    }
}