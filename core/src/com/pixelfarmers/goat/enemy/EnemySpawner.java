package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;

public class EnemySpawner {

    EnemyFactory enemyFactory;

    Vector2 position;
    int spawnedSoFar = 0;
    int totalEnemiesToSpawn;
    float spawnRateSeconds;
    float timeToSpawn;
    int numEnemiesToSpawnAtOnce;

    public EnemySpawner(EnemyFactory enemyFactory,
                        Vector2 position,
                        int totalEnemiesToSpawn,
                        float spawnRateSeconds,
                        int numEnemiesToSpawnAtOnce) {
        this.enemyFactory = enemyFactory;
        this.position = position;
        this.totalEnemiesToSpawn = totalEnemiesToSpawn;
        this.spawnRateSeconds = spawnRateSeconds;
        this.timeToSpawn = spawnRateSeconds;
        this.numEnemiesToSpawnAtOnce = numEnemiesToSpawnAtOnce;
    }

    public void update(float delta) {
        timeToSpawn-=delta;
    }

    public boolean isReadyToSpawn() {
        return spawnedSoFar < totalEnemiesToSpawn
                && timeToSpawn <= 0;
    }

    public Enemy spawn(Location<Vector2> player) {
        spawnedSoFar++;
        timeToSpawn = spawnRateSeconds;
        return enemyFactory.createKamikaze(position, player);
    }

}
