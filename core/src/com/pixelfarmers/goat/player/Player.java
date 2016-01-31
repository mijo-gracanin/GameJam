package com.pixelfarmers.goat.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.pixelfarmers.goat.GameSettings;
import com.pixelfarmers.goat.PhysicalEntity;
import com.pixelfarmers.goat.TextureFilePaths;
import com.pixelfarmers.goat.level.CollisionDetection;
import com.pixelfarmers.goat.level.Level;
import com.pixelfarmers.goat.weapon.Sword;


public class Player implements PhysicalEntity, Steerable<Vector2> {

    public interface OnHitListener {
        void onHit(int newHitPoints);
    }

    public final Sword sword;

    private static final float COLLISION_RADIUS = 8f;
    private static final float MOVEMENT_SPEED = 100f;
    private static final float WEAPON_WIDTH = 4;
    private static final float WEAPON_HEIGHT = 16;
    private static final float SPEED_DECREASE_FACTOR = 0.8f;
    private static float INVINCIBILITY_DURATION = 1.2f;
    private static float STUN_DURATION = INVINCIBILITY_DURATION * 0.5f;
    private final Circle collisionCircle;

    private float orientationInRadians = 0;
    private Vector2 movementDirection = new Vector2();
    private Vector2 position;
    private OnHitListener onHitListener;

    private int maxHitPoints;
    private int hitPoints;

    private Animation walkingAnimation;
    float animationStateTime;
    private Animation idleAnimation;

    private boolean isInvincible = false;
    private boolean isStunned = false;

    AssetManager assetManager;

    public Player(AssetManager assetManager, Vector2 startingPosition, OnHitListener onHitListener) {
        this.assetManager = assetManager;
        this.onHitListener = onHitListener;
        position = startingPosition;
        collisionCircle = new Circle(position.x, position.y, COLLISION_RADIUS);
        sword = new Sword(position.cpy(), assetManager);
        setupAnimations();

        maxHitPoints = GameSettings.getInstance().getDifficulty() == GameSettings.Difficulty.NORMAL ? 10 : 6;
        hitPoints = maxHitPoints;
    }

    private void setupAnimations() {
        TextureRegion[][] textureRegions = TextureRegion.split(assetManager.get(TextureFilePaths.CHARACTER, Texture.class), 16, 32);
        walkingAnimation = new Animation(0.1f, textureRegions[1]);
        idleAnimation = new Animation(0.8f, textureRegions[0]);
        animationStateTime = 0;
    }

    public void update(float delta, Level currentLevel) {
        float dx = movementDirection.x * MOVEMENT_SPEED * delta;
        float dy = movementDirection.y * MOVEMENT_SPEED * delta;

        tryMovingHorizontally(currentLevel, dx);
        tryMovingVertically(currentLevel, dy);

        sword.update(position.cpy(), orientationInRadians - (MathUtils.PI / 2), delta);

        movementDirection.scl(SPEED_DECREASE_FACTOR);
        collisionCircle.setPosition(position.x, position.y);
    }

    public boolean onHit(int damage, Vector2 forceDirection) {
        if (isInvincible) {
            return false;
        }

        isInvincible = true;
        isStunned = true;
        movementDirection.set(forceDirection.x, forceDirection.y).nor();

        setInvincibilityTimer();
        setStunTimer();

        hitPoints -= damage;
        onHitListener.onHit(hitPoints);
        return hitPoints <= 0;
    }

    private void setInvincibilityTimer() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isInvincible = false;
            }
        }, INVINCIBILITY_DURATION);
    }

    private void setStunTimer() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isStunned = false;
            }
        }, STUN_DURATION);
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public boolean isAlive() {
        return hitPoints > 0;
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

    public void castSword() {
        if (!sword.isActive()) {
            sword.castSword(position);
        }
    }

    public void goLeft() {
        if (!isStunned) {
            movementDirection.set(-1, movementDirection.y);
            movementDirection.nor();
        }
    }

    public void goRight() {
        if (!isStunned) {
            movementDirection.set(1, movementDirection.y);
            movementDirection.nor();
        }
    }

    public void goDown() {
        if (!isStunned) {
            movementDirection.set(movementDirection.x, -1);
            movementDirection.nor();
        }
    }

    public void goUp() {
        if (!isStunned) {
            movementDirection.set(movementDirection.x, 1);
            movementDirection.nor();
        }
    }

    public void draw(Batch batch) {
        sword.draw(batch);
        animationStateTime += Gdx.graphics.getDeltaTime();
        if (!isInvincible || shouldShowWhileInvincible()) {
            batch.draw(getCurrentTexture(), position.x - COLLISION_RADIUS, position.y - COLLISION_RADIUS);
        }
    }

    private boolean shouldShowWhileInvincible() {
        float msec = animationStateTime - (int) animationStateTime;
        return msec < 0.25f || msec >= 0.5f && msec < 0.75f;
    }

    private TextureRegion getCurrentTexture() {
        if (isMoving()) {
            TextureRegion textureRegion = walkingAnimation.getKeyFrame(animationStateTime, true);
            if (movementDirection.x < 0 && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            } else if (movementDirection.x > 0 && textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
            return textureRegion;
        } else {
            return idleAnimation.getKeyFrame(animationStateTime, true);
        }
    }

    private boolean isMoving() {
        return movementDirection.len2() > 0.1;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        /*shapeRenderer.circle(position.x, position.y, collisionCircle.radius);
        shapeRenderer.rect(position.x - WEAPON_WIDTH / 2, position.y - WEAPON_HEIGHT / 2,
                WEAPON_WIDTH / 2, WEAPON_HEIGHT / 2,
                WEAPON_WIDTH, WEAPON_HEIGHT,
                1.0f, 1.0f,
                orientationInRadians * MathUtils.radDeg);*/
        sword.drawDebug(shapeRenderer);
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


    @Override
    public Vector2 getLinearVelocity() {
        return new Vector2(movementDirection.x * MOVEMENT_SPEED, movementDirection.y * MOVEMENT_SPEED);
    }

    @Override
    public float getAngularVelocity() {
        return 0;
    }

    @Override
    public float getBoundingRadius() {
        return COLLISION_RADIUS;
    }

    @Override
    public boolean isTagged() {
        return false;
    }

    @Override
    public void setTagged(boolean tagged) {

    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return 0;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {

    }

    @Override
    public float getMaxLinearAcceleration() {
        return 0;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {

    }

    @Override
    public float getMaxAngularSpeed() {
        return 0;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {

    }

    @Override
    public float getMaxAngularAcceleration() {
        return 0;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {

    }
}
