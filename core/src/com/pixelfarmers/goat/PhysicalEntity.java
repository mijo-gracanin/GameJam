package com.pixelfarmers.goat;


import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public interface PhysicalEntity extends Location<Vector2> {
    float getHorizontalSpeed();
    float getVerticalSpeed();
    void setVelocity(float x, float y);
    Circle getCollisionCircle();
}
