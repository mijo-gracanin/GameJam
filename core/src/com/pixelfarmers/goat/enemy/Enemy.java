package com.pixelfarmers.goat.enemy;


import com.badlogic.gdx.ai.steer.SteerableAdapter;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy extends SteerableAdapter<Vector2> {

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());

    Vector2 position;
    float orientation;
    Vector2 linearVelocity = new Vector2(0, 0);
    float angularVelocity = 0;

    Circle collisionCircle;

    float stateTime = 0;

    boolean isActive = true;

    protected SteeringBehavior<Vector2> steeringBehavior;

    public Enemy(Vector2 startingPosition) {
        this.position = new Vector2(startingPosition.x, startingPosition.y);
        this.collisionCircle = new Circle(position, getBoundingRadius());
    }

    public void update(float delta) {
        steeringBehavior.calculateSteering(steeringOutput);
        applySteering(steeringOutput, delta);
        collisionCircle.setPosition(position.x, position.y);
        stateTime += delta;
    }

    public abstract boolean onHit(int damage);

    public void draw(Batch batch) {
        batch.draw(getTexure(), position.x - getBoundingRadius(), position.y - getBoundingRadius());
    }

    protected abstract TextureRegion getTexure();

    public void drawDebug(ShapeRenderer shapeRenderer) {
        //shapeRenderer.circle(position.x, position.y, BOUNDING_RADIUS);
    }

    public Circle getCollisionCircle() {
        return collisionCircle;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    private void applySteering(SteeringAcceleration<Vector2> steering, float time) {
        this.position.mulAdd(linearVelocity, time);
        this.linearVelocity.mulAdd(steering.linear, time).limit(this.getMaxLinearSpeed());
        this.orientation += angularVelocity * time;
        this.angularVelocity += steering.angular * time;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public float vectorToAngle (Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector (Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    public abstract int getDamage();
}
