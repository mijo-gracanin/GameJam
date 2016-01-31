package com.pixelfarmers.goat.powerup;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.TextureFilePaths;
import com.pixelfarmers.goat.level.Tile;

public class Powerup {

    public enum TYPE {

        HEALTH(TextureFilePaths.HEALTH_POWERUP),
        SPEED(TextureFilePaths.SPEED_POWERUP),
        DAMAGE(TextureFilePaths.DAMAGE_POWERUP);

        public final String textureName;

        TYPE(String textureName) {
            this.textureName = textureName;
        }
    }

    private final Vector2 position;
    private Circle collisionCircle;
    private final Texture texture;
    private final TYPE type;

    public Powerup(Vector2 position, Texture texture, TYPE type) {
        this.position = position;
        this.texture = texture;
        this.type = type;
        this.collisionCircle = new Circle(position, Tile.TILE_SIZE);
    }

    public void update(float delta) {
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
    public Circle getCollisionCircle() {
        return collisionCircle;
    }
}
