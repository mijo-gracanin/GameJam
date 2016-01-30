package com.pixelfarmers.goat.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.PhysicalEntity;

/**
 * Created by mijo on 30/01/16.
 */
public class Sword implements PhysicalEntity {

    private boolean isActive = false;
    private static final float STRETCHING_SPEED = 50f;
    private static final float MAX_SWORD_LENGTH = 18;
    private static final float COLLISION_RADIUS = 3f;
    private Vector2 position = new Vector2();
    private Circle collisionCircle = new Circle(position, COLLISION_RADIUS);;
    private boolean isGrowing = true;
    private float swordLength = 0;

    public Sword(Vector2 position) {
        this.position = position.cpy();
    }

    public void update(Vector2 playerPosition, float playerOrientation, float delta) {
        if (!isActive()) return;

        Vector2 speedVector = new Vector2(1, 1);
        speedVector.setAngleRad(playerOrientation);
        float direction = isGrowing ? 1 : -1;
        swordLength += STRETCHING_SPEED * delta * direction;
        position.x = speedVector.x * swordLength + playerPosition.x ;
        position.y = speedVector.y * swordLength + playerPosition.y;

        if (swordLength > MAX_SWORD_LENGTH) {
            isGrowing = false;
        }
        else if (swordLength < 0.1 && !isGrowing) {
            hideSword();
        }

        //Gdx.app.log("Distance", "" + swordLength);
        collisionCircle.setPosition(position.x, position.y);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        if (!isActive()) return;
        shapeRenderer.circle(position.x, position.y, COLLISION_RADIUS);
    }

    @Override
    public void setOrientation(float orientation) {
        return;
    }

    @Override
    public float getOrientation() {
        return 0;
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

    public boolean isActive() {
        return isActive;
    }

    private void hideSword() {
        isActive = false;
        isGrowing = false;
        swordLength = 0;
    }

    public void castSword(Vector2 startPosition) {
        position = startPosition.cpy();
        isActive = true;
        isGrowing = true;
    }

    public int getDamage() {
        return 3;
    }
}
