package com.pixelfarmers.goat.level;


import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.pixelfarmers.goat.constants.MessageCode;
import com.pixelfarmers.goat.player.PhysicalEntity;
import com.pixelfarmers.goat.player.Player;
import com.pixelfarmers.goat.powerup.Powerup;

import java.util.ArrayList;
import java.util.List;

public class CollisionDetection {
    private static int[] dx = {1, -1, 0,  0};
    private static int[] dy = {0,  0, 1, -1};

    private static Array<Powerup> collidingPowerups = new Array<Powerup>();

    public static boolean isCharacterCollidingWall(PhysicalEntity character, Level level) {
        for (int row = 0; row < level.height(); row++) {
            for (int col = 0; col < level.width(); col++) {
                Tile tile = level.getTile(col, row);
                if (tile.isSolid && Intersector.overlaps(character.getCollisionCircle(), tile.boundingBox)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void checkPlayerPowerupCollisions(Player player, Array<Powerup>powerups) {
        for(Powerup powerup : powerups) {
            if(Intersector.overlaps(powerup.getCollisionCircle(), player.getCollisionCircle())) {
                collidingPowerups.add(powerup);
            }
        }

        for(Powerup powerup : collidingPowerups) {
            MessageManager.getInstance().dispatchMessage(0, MessageCode.POWERUP_PICKUP, powerup);
        }

        collidingPowerups.clear();
    }

    //TODO do we need this?
    private static List<Tile> findOverlappingTiles(PhysicalEntity character, Level level) {
        List<Tile> overlappingTiles = new ArrayList<Tile>();

        for (int row = 0; row < level.height(); row++) {
            for (int col = 0; col < level.width(); col++) {
                Tile tile = level.getTile(col, row);

                if (tile.isSolid && Intersector.overlaps(character.getCollisionCircle(), tile.boundingBox)) {
                    overlappingTiles.add(tile);
                    checkNeighbouringTilesForCollision(overlappingTiles, character, level, row, col);
                    return overlappingTiles;
                }
            }
        }

        return overlappingTiles;
    }

    //TODO do we need this?
    private static void checkNeighbouringTilesForCollision(List<Tile> overlappingTiles, PhysicalEntity character, Level level, int startingRow, int staringCol) {
        for (int i = 0; i < 4; i++) {
            int col = staringCol + dx[i];
            int row = startingRow + dy[i];
            Tile tile = level.getTile(col, row);

            if (tile.isSolid && Intersector.overlaps(character.getCollisionCircle(), tile.boundingBox)) {
                overlappingTiles.add(tile);
            }
        }
    }

}
