package com.pixelfarmers.goat.projectile;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.PhysicalEntity;

public class Projectile implements PhysicalEntity {

    private static final float MOVEMENT_SPEED = 80f;
    private static final float COLLISION_RADIUS = 3f;
    private float orientationInRadians = 0;
    private Vector2 position = new Vector2();
    private Circle collisionCircle = new Circle(position, COLLISION_RADIUS);

    private int damage = 2;

    public Projectile(Vector2 position, float orientationInRadians) {
        this.position = position;
        this.orientationInRadians = orientationInRadians;
    }

    public void update(float delta) {
        Vector2 speedVector = new Vector2(1, 1);
        speedVector.setAngleRad(orientationInRadians);
        position.x += speedVector.x * MOVEMENT_SPEED * delta;
        position.y += speedVector.y * MOVEMENT_SPEED * delta;
        collisionCircle.setPosition(position.x, position.y);
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

    @Override
    public Circle getCollisionCircle() {
        return collisionCircle;
    }

    @Override
    public float getHorizontalSpeed() {
        return 0;
    }

    @Override
    public float getVerticalSpeed() {
        return 0;
    }

    public int getDamage() {
        return damage;
    }
}
