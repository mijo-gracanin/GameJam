package com.pixelfarmers.goat.intro;


import com.badlogic.gdx.graphics.Camera;

public class CompoundCinematic extends Cinematic {
    private final Cinematic cinematic1, cinematic2;

    public CompoundCinematic(Cinematic cinematic1, Cinematic cinematic2) {
        super(Math.max(cinematic1.duration, cinematic2.duration));
        this.cinematic1 = cinematic1;
        this.cinematic2 = cinematic2;
    }

    @Override
    protected void updateImpl(float delta, Camera camera) {
        cinematic1.update(delta, camera);
        cinematic2.update(delta, camera);
    }

    @Override
    public boolean isFinished() {
        return cinematic1.isFinished() && cinematic2.isFinished();
    }
}
