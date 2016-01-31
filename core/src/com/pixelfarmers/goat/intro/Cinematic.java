package com.pixelfarmers.goat.intro;


import com.badlogic.gdx.graphics.Camera;

public abstract class Cinematic {
    protected final float duration;
    private float timeLeft;

    public Cinematic(float duration) {
        this.duration = duration;
        this.timeLeft = duration;
    }

    public void update(float delta, Camera camera) {
        if (isFinished()) {
            return;
        }

        timeLeft -= delta;
        updateImpl(delta, camera);
        camera.update();
    }

    protected abstract void updateImpl(float delta, Camera camera);

    public boolean isFinished() {
        return timeLeft < 0;
    }
}
