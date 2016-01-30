package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class EnemyFactory {

    private AssetManager assetManager;

    public EnemyFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public Enemy createKamikaze(Vector2 position, Location<Vector2> player) {
        Enemy enemy = new Enemy(assetManager.get(TextureFilePaths.KAMIKAZE, Texture.class), position);
        Seek<Vector2> seekSb = new Seek<Vector2>(enemy, player);
        enemy.setSteeringBehavior(seekSb);
        return enemy;
    }

}
