package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pixelfarmers.goat.player.Player;

public class EnemySpawner {

    private static final float MIN_DIST_TO_ACTIVATE = 200;
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
        Gdx.app.log("DISI", "ACTIVATING");
        isActive = true;
    }

    public void deactivate() {
        isActive = false;
    }

    public void update(float delta, Player player) {
        if(!isActive && position.dst2(player.getPosition()) < MIN_DIST_TO_ACTIVATE * MIN_DIST_TO_ACTIVATE) {
            activate();
        }

        if(isActive) {
            timeToSpawn-=delta;
        }
    }

    public boolean isReadyToSpawn() {
        return spawnedSoFar < totalEnemiesToSpawn
                && timeToSpawn <= 0;
    }

    public Array<EnemyBat> spawn(Location<Vector2> player) {
        spawnedSoFar+=numEnemiesToSpawnAtOnce;
        timeToSpawn = spawnRateSeconds;
        Array<EnemyBat> spawnedEnemies = new Array<EnemyBat>(numEnemiesToSpawnAtOnce);
        for(int i = 0; i < numEnemiesToSpawnAtOnce; i++) {
            float xOffset = MathUtils.random(0, 0);
            float yOffset = MathUtils.random(0, 0);
            Vector2 spawnPosition = new Vector2(position.x + xOffset, position.y + yOffset);
            spawnedEnemies.add(enemyManager.createKamikaze(spawnPosition, player));
        }
        return spawnedEnemies;
    }

}
