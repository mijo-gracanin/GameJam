package com.pixelfarmers.goat.level;


import com.badlogic.gdx.math.Vector2;
import com.pixelfarmers.goat.player.PhysicalEntity;

public class CollisionDetection {
    public void doCharacterLevelCollision(PhysicalEntity character, Level level) {
        Vector2 velocity = character.getVelocity();
    }

    public boolean isCharacterCollidingWall(PhysicalEntity character) {
        return false;
    }
}
