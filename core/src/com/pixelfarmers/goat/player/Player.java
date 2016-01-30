package com.pixelfarmers.goat.player;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.PhysicalEntity;
import com.pixelfarmers.goat.level.CollisionDetection;
import com.pixelfarmers.goat.level.Level;


public class Player implements PhysicalEntity {

    private Vector2 movementDirection = new Vector2();

    private static final float COLLISION_RADIUS = 8f;
    private static final float MOVEMENT_SPEED = 100f;
    private static final float WEAPON_WIDTH = 4;
    private static final float WEAPON_HEIGHT = 16;
    private static final float SPEED_DECREASE_FACTOR = 0.8f;
    private final Circle collisionCircle;
    private final Rectangle weapon;
    private float orientationInRadians = 0;
    private Vector2 position;

    public Player(int x, int y) {
        position = new Vector2(x, y);
        collisionCircle = new Circle(position.x, position.y, COLLISION_RADIUS);
        weapon = new Rectangle(position.x, position.y, WEAPON_WIDTH, WEAPON_HEIGHT);
    }

    public void update(float delta, Level currentLevel) {
        float dx = movementDirection.x * MOVEMENT_SPEED * delta;
        float dy = movementDirection.y * MOVEMENT_SPEED * delta;

        tryMovingHorizontally(currentLevel, dx);
        tryMovingVertically(currentLevel, dy);

        movementDirection.scl(SPEED_DECREASE_FACTOR);
        collisionCircle.setPosition(position.x, position.y);
    }

    private void tryMovingVertically(Level currentLevel, float dy) {
        float yOld = position.y;
        position.y += dy;
        collisionCircle.setPosition(position.x, position.y);

        if (CollisionDetection.isCharacterCollidingWall(this, currentLevel)) {
            position.y = yOld;
        }
    }

    private void tryMovingHorizontally(Level currentLevel, float dx) {
        float xOld = position.x;
        position.x += dx;
        collisionCircle.setPosition(position.x, position.y);

        if (CollisionDetection.isCharacterCollidingWall(this, currentLevel)) {
            position.x = xOld;
        }
    }

    public void goLeft() {
        movementDirection.set(-1, movementDirection.y);
        movementDirection.nor();
    }

    public void goRight() {
        movementDirection.set(1, movementDirection.y);
        movementDirection.nor();
    }

    public void goDown() {
        movementDirection.set(movementDirection.x, -1);
        movementDirection.nor();
    }

    public void goUp() {
        movementDirection.set(movementDirection.x, 1);
        movementDirection.nor();
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(position.x, position.y, collisionCircle.radius);
        shapeRenderer.rect(position.x - WEAPON_WIDTH / 2, position.y - WEAPON_HEIGHT / 2,
                WEAPON_WIDTH / 2, WEAPON_HEIGHT / 2,
                WEAPON_WIDTH, WEAPON_HEIGHT,
                1.0f, 1.0f,
                orientationInRadians * MathUtils.radDeg);
        //Gdx.app.log("Angle", "" + orientationInRadians * MathUtils.radDeg);
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
    public float getHorizontalSpeed() {
        return movementDirection.x;
    }

    @Override
    public float getVerticalSpeed() {
        return movementDirection.y;
    }

    @Override
    public Circle getCollisionCircle() {
        return collisionCircle;
    }
}
