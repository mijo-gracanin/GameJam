package com.pixelfarmers.goat.enemy.spawner;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pixelfarmers.goat.enemy.Enemy;
import com.pixelfarmers.goat.enemy.EnemyManager;
import com.pixelfarmers.goat.player.Player;

public abstract class EnemySpawner {

    private static final float INNER_SPAWN_RADIUS_2 = 222 * 222;
    private static final float OUTER_SPAWN_RADIUS_2 = 512 * 512;

    EnemyManager enemyManager;

    Vector2 position;
    int spawnedSoFar = 0;
    int totalEnemiesToSpawn;
    float spawnRateSeconds;
    float timeToSpawn;
    int numEnemiesToSpawnAtOnce;
    boolean isActive = false;

    public EnemySpawner(EnemyManager enemyManager,
                        Vector2 position,
                        int totalEnemiesToSpawn,
                        float spawnRateSeconds,
                        int numEnemiesToSpawnAtOnce) {
        this.enemyManager = enemyManager;
        this.position = position;
        this.totalEnemiesToSpawn = totalEnemiesToSpawn;
        this.spawnRateSeconds = spawnRateSeconds;
        this.timeToSpawn = spawnRateSeconds;
        this.numEnemiesToSpawnAtOnce = numEnemiesToSpawnAtOnce;
    }

    public void activate() {
        isActive = true;
    }

    public void deactivate() {
        isActive = false;
    }

    public void update(float delta, Player player) {
        float distanceToPlayer = position.dst2(player.getPosition());
        if (!isActive && distanceToPlayer > INNER_SPAWN_RADIUS_2 && distanceToPlayer < OUTER_SPAWN_RADIUS_2) {
            activate();
        } else {
            deactivate();
        }

        if (isActive) {
            timeToSpawn -= delta;
        }
    }

    public boolean isReadyToSpawn() {
        return spawnedSoFar < totalEnemiesToSpawn
                && timeToSpawn <= 0;
    }

    public Array<Enemy> spawn(Steerable<Vector2> player) {
        spawnedSoFar+=numEnemiesToSpawnAtOnce;
        timeToSpawn = spawnRateSeconds;
        Array<Enemy> spawnedEnemies = new Array<Enemy>(numEnemiesToSpawnAtOnce);
        for(int i = 0; i < numEnemiesToSpawnAtOnce; i++) {
            float xOffset = MathUtils.random(0, 0);
            float yOffset = MathUtils.random(0, 0);
            Vector2 spawnPosition = new Vector2(position.x + xOffset, position.y + yOffset);
            spawnedEnemies.add(createEnemy(enemyManager, spawnPosition, player));
        }
        return spawnedEnemies;
    }

    protected abstract Enemy createEnemy(EnemyManager enemyManager, Vector2 position, Steerable<Vector2> player);
}
