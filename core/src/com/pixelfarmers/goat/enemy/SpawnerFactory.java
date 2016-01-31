package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.pixelfarmers.goat.level.Level;
import com.pixelfarmers.goat.level.Tile;

public class SpawnerFactory {

    public static class Parameters {
        public final float minDistanceBetweenSpawners;
        public final int totalSpawners;

        public Parameters(float minDistanceBetweenSpawners, int totalSpawners) {
            this.minDistanceBetweenSpawners = minDistanceBetweenSpawners;
            this.totalSpawners = totalSpawners;
        }
    }

    public static Array<EnemySpawner> createSpawnersForLevel(EnemyManager enemyManager, Level level, Parameters parameters) {
        Array<EnemySpawner> spawners = new Array<EnemySpawner>();

        for (int row = 0; row < level.height(); row++) {
            for (int col = 0; col < level.width(); col++) {
                float x = col * Tile.TILE_SIZE;
                float y = row * Tile.TILE_SIZE;
                boolean isFloor = !level.getTile(col, row).isSolid;

                if (isFloor && distanceToNearestSpawner(x, y, spawners) > parameters.minDistanceBetweenSpawners) {
                    if (MathUtils.random() < 0.6f) {
                        spawners.add(new BatSpawner(enemyManager, new Vector2(x, y), 20, 1.5f, 1));
                    } else {
                        spawners.add(new MummySpawner(enemyManager, new Vector2(x, y), 20, 2, 1));
                    }

                    if (spawners.size == parameters.totalSpawners) {
                        return spawners;
                    }
                }
            }
        }

        return spawners;
    }

    private static float distanceToNearestSpawner(float x, float y, Array<EnemySpawner> spawners) {
        float minDist = 10000000;
        for (EnemySpawner spawner : spawners) {
            float dx = Math.abs(x - spawner.position.x);
            float dy = Math.abs(y - spawner.position.y);
            float dist = dx * dx + dy * dy;
            if (dist < minDist) {
                minDist = dist;
            }
        }
        return (float)Math.sqrt(minDist);
    }
}
