package com.pixelfarmers.goat.projectile;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by mijo on 30/01/16.
 */
public class Projectile implements Location<Vector2> {

    private static final float MOVEMENT_SPEED = 30f;
    private static final float COLLISION_RADIUS = 3f;
    private float orientationInRadians = 0;
    private Vector2 position = new Vector2();

    public Projectile(float orientationInRadians) {
        this.orientationInRadians = orientationInRadians;
    }

    public void update(float delta) {
        Vector2 speedVector = new Vector2(0, 1);
        speedVector.setAngleRad(orientationInRadians);
        position.x += speedVector.x * MOVEMENT_SPEED;
        position.y += speedVector.y * MOVEMENT_SPEED;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(position.x, position.y, COLLISION_RADIUS);
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientationInRadians = orientation;
    }

    @Override
    public float getOrientation() {
        return orientationInRadians;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return 0;
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return null;
    }

    @Override
    public Location<Vector2> newLocation() {
        return null;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }
}
