package com.pixelfarmers.goat.enemy;


import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.math.Vector2;

public class MummySpawner extends EnemySpawner {
    public MummySpawner(EnemyManager enemyManager, Vector2 position, int totalEnemiesToSpawn, float spawnRateSeconds, int numEnemiesToSpawnAtOnce) {
        super(enemyManager, position, totalEnemiesToSpawn, spawnRateSeconds, numEnemiesToSpawnAtOnce);
    }

    @Override
    protected Enemy createEnemy(EnemyManager enemyManager, Vector2 position, Steerable<Vector2> player) {
        return enemyManager.createMummy(position, player);
    }
}
