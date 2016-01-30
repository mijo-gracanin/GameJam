package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class SpawnerFactory {

    public static Array<EnemySpawner> createSpawnersForLevel(EnemyManager enemyManager, int levelId) {
        Array<EnemySpawner> spawners = new Array<EnemySpawner>();
        if(levelId == 1) {
            EnemySpawner spawner = new EnemySpawner(enemyManager, new Vector2(160, 240), 20, 2, 1);
            spawners.add(spawner);
        }
        return spawners;
    }
}
