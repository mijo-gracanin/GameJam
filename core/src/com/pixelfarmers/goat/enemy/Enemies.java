package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.behaviors.Separation;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pixelfarmers.goat.Player;

public class Enemies {

    private AssetManager assetManager;
    private Array<Enemy> enemyList;
    private Array<EnemySpawner> enemySpawners;
    private Player player;

    public Enemies(AssetManager assetManager, Player player) {
        this.assetManager = assetManager;
        this.player = player;
        enemyList = new Array<Enemy>();
        this.enemySpawners = new Array<EnemySpawner>();
    }

    public void addSpawner(EnemySpawner spawner) {
        enemySpawners.add(spawner);
    }

    public void update(float delta) {
        for (int i = 0; i < enemyList.size; i++) {
            enemyList.get(i).update(delta);
        }

        for(EnemySpawner spawner : enemySpawners) {
            spawner.update(delta);
            if(spawner.isReadyToSpawn()) {
                enemyList.addAll(spawner.spawn(player));
            }
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        for(Enemy enemy : enemyList) {
            enemy.draw(spriteBatch);
        }
    }

    public Enemy createKamikaze(Vector2 position, Location<Vector2> player) {
        Enemy enemy = new Enemy(assetManager.get(TextureFilePaths.KAMIKAZE, Texture.class), position);
        enemy.setSteeringBehavior(createKamikazeSteeringBehavior(enemy, player));
        return enemy;
    }

    private SteeringBehavior<Vector2> createKamikazeSteeringBehavior(Enemy enemy, Location<Vector2> player) {
        BlendedSteering<Vector2> kamikazeSteering = new BlendedSteering<Vector2>(enemy);
        Proximity<Vector2> proximity = new KamikazeProximity();
        proximity.setOwner(enemy);
        Separation<Vector2> separationSb = new Separation<Vector2>(enemy, proximity);
        Seek<Vector2> seekSb = new Seek<Vector2>(enemy, player);
        kamikazeSteering.add(new BlendedSteering.BehaviorAndWeight<Vector2>(separationSb, 1));
        kamikazeSteering.add(new BlendedSteering.BehaviorAndWeight<Vector2>(seekSb, 1));
        return kamikazeSteering;
    }

    private class KamikazeProximity implements Proximity<Vector2> {

        Steerable<Vector2> owner;

        @Override
        public Steerable<Vector2> getOwner() {
            return owner;
        }

        @Override
        public void setOwner(Steerable<Vector2> owner) {
            this.owner = owner;
        }

        @Override
        public int findNeighbors(ProximityCallback<Vector2> callback) {
            for(Enemy enemy : enemyList) {
                if(enemy != owner) {
                    callback.reportNeighbor(enemy);
                }
            }
            return enemyList.size;
        }
    }

}
