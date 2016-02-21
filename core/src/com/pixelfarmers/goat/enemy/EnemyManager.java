package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.behaviors.Pursue;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.utils.RayConfiguration;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.pixelfarmers.goat.constants.MessageCode;
import com.pixelfarmers.goat.enemy.spawner.EnemySpawner;
import com.pixelfarmers.goat.util.Box2dRaycastCollisionDetector;
import com.pixelfarmers.goat.level.Tile;
import com.pixelfarmers.goat.player.Player;
import com.pixelfarmers.goat.player.Projectile;
import com.pixelfarmers.goat.player.Sword;

public class EnemyManager implements Telegraph {

    private DelayedRemovalArray<Enemy> enemyList;
    private Array<EnemySpawner> enemySpawners;
    private Array<Cultist> cultists;
    private Player player;
    private World world;
    private boolean paused = false;

    public EnemyManager(Player player, World world) {
        MessageManager.getInstance().addListeners(this, MessageCode.CINEMATIC_END, MessageCode.CINEMATIC_START);
        this.player = player;
        enemyList = new DelayedRemovalArray<>();
        cultists = new Array<>();
        this.enemySpawners = new Array<>();
        this.world = world;
        addCultists();
    }

    private void addCultists() {
        Array<Vector2> cultistLocations = new Array<>();

        cultistLocations.add(calculateCultistLocation(103, 35, 0));
        cultistLocations.add(calculateCultistLocation(95, 35, 8));
        cultistLocations.add(calculateCultistLocation(103, 40, 0));
        cultistLocations.add(calculateCultistLocation(95, 40, 8));

        Cultist cultist;
        cultist = new Cultist(cultistLocations.get(0));
        addCultistSteeringBehavior(cultist, cultistLocations.get(1));
        enemyList.add(cultist);
        cultists.add(cultist);

        cultist = new Cultist(cultistLocations.get(1));
        addCultistSteeringBehavior(cultist, cultistLocations.get(3));
        enemyList.add(cultist);
        cultists.add(cultist);

        cultist = new Cultist(cultistLocations.get(2));
        addCultistSteeringBehavior(cultist, cultistLocations.get(0));
        enemyList.add(cultist);
        cultists.add(cultist);

        cultist = new Cultist(cultistLocations.get(3));
        addCultistSteeringBehavior(cultist, cultistLocations.get(2));
        enemyList.add(cultist);
        cultists.add(cultist);
    }

    private Vector2 calculateCultistLocation(int x, int y, int xOffset) {
        Vector2 position = Tile.tileToPosition(x, y);
        return new Vector2(position.x + xOffset, position.y);
    }

    private void addCultistSteeringBehavior(Cultist cultist, Vector2 p1) {
        Array<Vector2> waypoints = new Array<>();
        waypoints.add(p1);
        waypoints.add(cultist.getPosition());
        LinePath<Vector2> path = new LinePath<>(waypoints, false);
        FollowPath<Vector2, LinePath.LinePathParam> followSb = new FollowPath<>(cultist, path, 40);
        followSb.setArrivalTolerance(20);
        followSb.setDecelerationRadius(60);

        cultist.setSteeringBehavior(followSb);
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void addSpawners(Array<EnemySpawner> spawners) {
        enemySpawners.addAll(spawners);
    }

    public void update(float delta, DelayedRemovalArray<Projectile> projectiles) {
        checkForPlayerCollisions();
        checkForProjectileCollisions(projectiles);
        checkForSwordCollisions(player.sword);

        for (int i = 0; i < enemyList.size; i++) {
            enemyList.get(i).update(delta);
        }

        if (!paused) {
            for (EnemySpawner spawner : enemySpawners) {
                spawner.update(delta, player);
                if (spawner.isReadyToSpawn()) {
                    enemyList.addAll(spawner.spawn(player));
                }
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
                player.onHit(enemy.getDamage(), enemy.getLinearVelocity());
            }
        }
    }

    public void checkForSwordCollisions(Sword sword) {
        if(!sword.isActive()) return;

        enemyList.begin();
        for (Enemy enemy : enemyList) {
            if(!enemy.isActive) {
                continue;
            }
            if (Intersector.overlaps(enemy.getCollisionCircle(), sword.getCollisionCircle())) {
                MessageManager.getInstance().dispatchMessage(MessageCode.SWORD_HIT_ENEMY, enemy.getPosition());
                boolean isDead = enemy.onHit(sword.getDamage() * player.getDamageModifier());
                if (isDead) {
                    enemyList.removeValue(enemy, true);
                    MessageManager.getInstance().dispatchMessage(MessageCode.ENEMY_DIED, enemy.getPosition());
                }
            }
        }
        enemyList.end();
    }

    public void checkForProjectileCollisions(DelayedRemovalArray<Projectile> projectiles) {
        projectiles.begin();
        enemyList.begin();
        for (Enemy enemy : enemyList) {
            if(!enemy.isActive) {
                continue;
            }
            for (Projectile projectile : projectiles) {
                if (Intersector.overlaps(enemy.getCollisionCircle(), projectile.getCollisionCircle())) {
                    MessageManager.getInstance().dispatchMessage(MessageCode.PROJECTILE_HIT_ENEMY, enemy.getPosition());
                    boolean isDead = enemy.onHit(projectile.getDamage() * player.getDamageModifier());
                    if (isDead) {
                        enemyList.removeValue(enemy, true);
                        MessageManager.getInstance().dispatchMessage(MessageCode.ENEMY_DIED, enemy.getPosition());
                    }
                    projectiles.removeValue(projectile, true);
                }
            }
        }
        projectiles.end();
        enemyList.end();
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        for (Enemy enemy : enemyList) {
            enemy.drawDebug(shapeRenderer);
        }
    }

    public Bat createBat(Vector2 position, Steerable<Vector2> player) {
        Bat enemy = new Bat(position);

        BlendedSteering<Vector2> steering = createStandardSteeringBehaviors(enemy);
        Pursue<Vector2> pursue = new Pursue<>(enemy, player);
        steering.add(new BlendedSteering.BehaviorAndWeight<>(pursue, 1));
        enemy.setSteeringBehavior(steering);
        return enemy;
    }

    public Enemy createMummy(Vector2 position, Steerable<Vector2> player) {
        Enemy enemy = new Mummy(position);
        BlendedSteering<Vector2> steering = createStandardSteeringBehaviors(enemy);
        Seek<Vector2> seek = new Seek<>(enemy, player);
        steering.add(new BlendedSteering.BehaviorAndWeight<>(seek, 1));
        enemy.setSteeringBehavior(steering);
        enemy.setSteeringBehavior(steering);
        return enemy;
    }

    private BlendedSteering<Vector2> createStandardSteeringBehaviors(Enemy enemy) {
        BlendedSteering<Vector2> kamikazeSteering = new BlendedSteering<>(enemy);

        Proximity<Vector2> proximity = new KamikazeProximity();
        proximity.setOwner(enemy);
        CollisionAvoidance<Vector2> separationSb = new CollisionAvoidance<>(enemy, proximity);

        Box2dRaycastCollisionDetector raycastCollisionDetector = new Box2dRaycastCollisionDetector(world);
        RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidanceSB = new RaycastObstacleAvoidance<>(enemy, createRayConfiguration(enemy));
        raycastObstacleAvoidanceSB.setRaycastCollisionDetector(raycastCollisionDetector);

        kamikazeSteering.add(new BlendedSteering.BehaviorAndWeight<>(separationSb, 1));
        kamikazeSteering.add(new BlendedSteering.BehaviorAndWeight<>(raycastObstacleAvoidanceSB, 4));
        return kamikazeSteering;
    }

    public BlendedSteering<Vector2> createGoatSteeringBehavior(Enemy goat) {
        BlendedSteering<Vector2> steering = new BlendedSteering<>(goat);

        Proximity<Vector2> proximity = new KamikazeProximity();
        proximity.setOwner(goat);

        Box2dRaycastCollisionDetector raycastCollisionDetector = new Box2dRaycastCollisionDetector(world);
        RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidanceSB = new RaycastObstacleAvoidance<>(goat, createGoatRayConfiguration(goat));
        raycastObstacleAvoidanceSB.setRaycastCollisionDetector(raycastCollisionDetector);

        Seek<Vector2> seek = new Seek<>(goat, player);
        steering.add(new BlendedSteering.BehaviorAndWeight<>(seek, 1));
        steering.add(new BlendedSteering.BehaviorAndWeight<>(raycastObstacleAvoidanceSB, 2));
        return steering;
    }

    private RayConfiguration<Vector2> createGoatRayConfiguration(Enemy enemy) {
        return new CentralRayWithWhiskersConfiguration<>(enemy, enemy.getMaxLinearSpeed(), 40, 20 * MathUtils.degreesToRadians);
    }

    private RayConfiguration<Vector2> createRayConfiguration(Enemy enemy) {
        return new CentralRayWithWhiskersConfiguration<>(enemy, enemy.getMaxLinearSpeed(), 40, 35 * MathUtils.degreesToRadians);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if(msg.message == MessageCode.CINEMATIC_END) {
            resume();
            for(Cultist cultist : cultists) {
                cultist.setSteeringBehavior(null);
            }
            return true;
        }

        if(msg.message == MessageCode.CINEMATIC_START) {
            pause();
            return true;
        }

        return false;
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
