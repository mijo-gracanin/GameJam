package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pixelfarmers.goat.level.Tile;

public class SpawnerFactory {

    public static Array<EnemySpawner> createSpawnersForLevel(EnemyManager enemyManager, int levelId) {
        Array<EnemySpawner> spawners = new Array<EnemySpawner>();
        if(levelId == 1) {
            EnemySpawner spawner1 = new BatSpawner(enemyManager, new Vector2(160, 100), 12, 1, 1);
            EnemySpawner spawner2 = new BatSpawner(enemyManager, new Vector2(27 * Tile.TILE_SIZE , 11 * Tile.TILE_SIZE), 12, 0.5f, 2);
            spawners.add(spawner1);
            spawners.add(spawner2);
        }
        return spawners;
    }
}
