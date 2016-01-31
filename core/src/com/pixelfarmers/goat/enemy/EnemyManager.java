package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.utils.RayConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.pixelfarmers.goat.fx.BloodParticle;
import com.pixelfarmers.goat.fx.ParticleEngine;
import com.pixelfarmers.goat.level.Box2dRaycastCollisionDetector;
import com.pixelfarmers.goat.level.Level;
import com.pixelfarmers.goat.player.Player;
import com.pixelfarmers.goat.weapon.Projectile;
import com.pixelfarmers.goat.weapon.Sword;

public class EnemyManager {

    public interface EnemyDeathListener {
        void onDeath(float x, float y);
    }

    private DelayedRemovalArray<Enemy> enemyList;
    private Array<EnemySpawner> enemySpawners;
    private Player player;
    private World world;
    private EnemyDeathListener enemyDeathListener;

    private Level level;

    public EnemyManager(Player player, World world, Level level, EnemyDeathListener enemyDeathListener) {
        this.player = player;
        enemyList = new DelayedRemovalArray<Enemy>();
        this.enemySpawners = new Array<EnemySpawner>();
        this.world = world;
        this.level = level;
        this.enemyDeathListener = enemyDeathListener;
    }

    public void addSpawners(Array<EnemySpawner> spawners) {
        enemySpawners.addAll(spawners);
    }

    public void update(float delta) {
        checkForPlayerCollisions();

        for (int i = 0; i < enemyList.size; i++) {
            enemyList.get(i).update(delta);
        }

        final int innerSpawnRadius = 320;
        final int outerSpawnRadius = 480;

        for (EnemySpawner spawner : enemySpawners) {
            spawner.update(delta, player);
            if (spawner.isReadyToSpawn()) {
                enemyList.addAll(spawner.spawn(player));
            }
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        for (Enemy enemy : enemyList) {
            enemy.draw(spriteBatch);
        }
    }


    public void checkForPlayerCollisions() {
        for (Enemy enemy : enemyList) {
            if (!enemy.isActive) {
                continue;
            }

            if(Intersector.overlaps(enemy.getCollisionCircle(), player.getCollisionCircle())) {
                player.onHit(enemy.getDamage());
            }
        }
    }

    public void checkForSwordCollisions(Sword sword, ParticleEngine particleEngine, Sound hitSound) {
        if(!sword.isActive()) return;

        enemyList.begin();
        for (Enemy enemy : enemyList) {
            if(!enemy.isActive) {
                continue;
            }
            if (Intersector.overlaps(enemy.getCollisionCircle(), sword.getCollisionCircle())) {
                hitSound.play();
                boolean isDead = enemy.onHit(sword.getDamage());
                if (isDead) {
                    particleEngine.addParticle(new BloodParticle(enemy.position.x, enemy.position.y));
                    enemyList.removeValue(enemy, true);
                    enemyDeathListener.onDeath(enemy.getPosition().x, enemy.getPosition().y);
                }
            }
        }
        enemyList.end();
    }

    public void checkForProjectileCollisions(DelayedRemovalArray<Projectile> projectiles,
                                             ParticleEngine particleEngine,
                                             Sound hitSound) {
        projectiles.begin();
        enemyList.begin();
        for (Enemy enemy : enemyList) {
            if(!enemy.isActive) {
                continue;
            }
            for (Projectile projectile : projectiles) {
                if (Intersector.overlaps(enemy.getCollisionCircle(), projectile.getCollisionCircle())) {
                    hitSound.play();
                    boolean isDead = enemy.onHit(projectile.getDamage());
                    bloodSplash(particleEngine, enemy);
                    if (isDead) {
                        enemyList.removeValue(enemy, true);
                        enemyDeathListener.onDeath(enemy.getPosition().x, enemy.getPosition().y);
                    }
                    projectiles.removeValue(projectile, true);
                }
            }
        }
        projectiles.end();
        enemyList.end();
    }

    private void bloodSplash(ParticleEngine particleEngine, Enemy enemy) {
        for (int i = 0; i < 4; i++) {
            particleEngine.addParticle(new BloodParticle(enemy.position.x, enemy.position.y));
        }
    }
    public void drawDebug(ShapeRenderer shapeRenderer) {
        for (Enemy enemy : enemyList) {
            enemy.drawDebug(shapeRenderer);
        }
    }

    public EnemyBat createBat(Vector2 position, Steerable<Vector2> player) {
        EnemyBat enemy = new EnemyBat(position);

        BlendedSteering<Vector2> steering = createStandardSteeringBehaviors(enemy, player);
        Pursue<Vector2> pursue = new Pursue<Vector2>(enemy, player);
        steering.add(new BlendedSteering.BehaviorAndWeight<Vector2>(pursue, 1));
        enemy.setSteeringBehavior(steering);
        return enemy;
    }

    public Enemy createMummy(Vector2 position, Steerable<Vector2> player) {
        Enemy enemy = new EnemyMummy(position);
        enemy.setSteeringBehavior(createStandardSteeringBehaviors(enemy, player));
        return enemy;
    }

    private BlendedSteering<Vector2> createStandardSteeringBehaviors(Enemy enemy, Steerable<Vector2> player) {
        BlendedSteering<Vector2> kamikazeSteering = new BlendedSteering<Vector2>(enemy);

        Proximity<Vector2> proximity = new KamikazeProximity();
        proximity.setOwner(enemy);
        CollisionAvoidance<Vector2> separationSb = new CollisionAvoidance<Vector2>(enemy, proximity);

        Box2dRaycastCollisionDetector raycastCollisionDetector = new Box2dRaycastCollisionDetector(world);
        RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidanceSB = new RaycastObstacleAvoidance<Vector2>(enemy, createRayConfiguration(enemy));
        raycastObstacleAvoidanceSB.setRaycastCollisionDetector(raycastCollisionDetector);

        kamikazeSteering.add(new BlendedSteering.BehaviorAndWeight<Vector2>(separationSb, 1));
        kamikazeSteering.add(new BlendedSteering.BehaviorAndWeight<Vector2>(raycastObstacleAvoidanceSB, 4));
        return kamikazeSteering;
    }

    private RayConfiguration<Vector2> createRayConfiguration(Enemy enemy) {
        return new CentralRayWithWhiskersConfiguration<Vector2>(enemy, enemy.getMaxLinearSpeed() * 2, 40, 35 * MathUtils.degreesToRadians);
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
            for (Enemy enemy : enemyList) {
                if (enemy != owner) {
                    callback.reportNeighbor(enemy);
                }
            }
            return enemyList.size;
        }
    }

}
