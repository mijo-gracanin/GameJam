package com.pixelfarmers.goat.player;


import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public interface PhysicalEntity extends Location<Vector2> {
    Vector2 getVelocity();
    void setPosition(float x, float y);
}
