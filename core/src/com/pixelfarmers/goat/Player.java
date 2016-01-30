package com.pixelfarmers.goat;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by mijo on 29/01/16.
 */
public class Player implements Location<Vector2> {

    public enum Movement {
        UP, DOWN, LEFT, RIGHT, STOP
    }

    public Movement movementDirection = Movement.UP;

    private static final float COLLISION_RADIUS = 16f;
    private static final float MOVEMENT_SPEED = 4;
    private static final float WEAPON_WIDTH = 4;
    private static final float WEAPON_HEIGHT = 12;
    private final Circle collisionCircle;
    private final Rectangle weapon;
    private float orientationInRadians = 0;
    private Vector2 position = new Vector2();

    public Player() {
        collisionCircle = new Circle(position.x, position.y, COLLISION_RADIUS);
        weapon = new Rectangle(position.x, position.y, WEAPON_WIDTH, WEAPON_HEIGHT);
    }

    public void update(float delta) {
        switch (movementDirection) {
            case UP: {
                position.y += MOVEMENT_SPEED;
            }
            break;
            case DOWN: {
                position.y -= MOVEMENT_SPEED;
            }
            break;
            case LEFT: {
                position.x -= MOVEMENT_SPEED;
            }
            break;
            case RIGHT: {
                position.x += MOVEMENT_SPEED;
            }
            break;
        }
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(position.x, position.y, collisionCircle.radius);
        shapeRenderer.rect(position.x - WEAPON_WIDTH / 2, position.y - WEAPON_HEIGHT / 2,
                WEAPON_WIDTH / 2, WEAPON_HEIGHT / 2,
                WEAPON_WIDTH, WEAPON_HEIGHT,
                1.0f, 1.0f,
                orientationInRadians * MathUtils.radDeg);
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
