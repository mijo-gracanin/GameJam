package com.pixelfarmers.goat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixelfarmers.goat.MusicPlayer;
import com.pixelfarmers.goat.SoundPlayer;
import com.pixelfarmers.goat.constants.MessageCode;
import com.pixelfarmers.goat.constants.Sounds;
import com.pixelfarmers.goat.constants.Textures;
import com.pixelfarmers.goat.enemy.EnemyManager;
import com.pixelfarmers.goat.enemy.Goat;
import com.pixelfarmers.goat.enemy.spawner.SpawnerFactory;
import com.pixelfarmers.goat.fx.ParticleEngine;
import com.pixelfarmers.goat.hud.Hud;
import com.pixelfarmers.goat.intro.CinematicBlock;
import com.pixelfarmers.goat.level.CollisionDetection;
import com.pixelfarmers.goat.level.Level;
import com.pixelfarmers.goat.level.LevelRenderer;
import com.pixelfarmers.goat.level.TiledMapLevelLoader;
import com.pixelfarmers.goat.player.Player;
import com.pixelfarmers.goat.powerup.PowerupHandler;
import com.pixelfarmers.goat.util.PFMathUtils;
import com.pixelfarmers.goat.player.Projectile;

public class GameScreen extends ScreenAdapter implements Telegraph {

    public static final float WORLD_WIDTH = 480;
    public static final float WORLD_HEIGHT = 360;

    private AssetManager assetManager;
    private Viewport viewport;
    private Camera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;

    private Player player;
    private Goat goat;
    private Level currentLevel;
    private LevelRenderer levelRenderer;
    private ParticleEngine particleEngine;
    private DelayedRemovalArray<Projectile> projectiles = new DelayedRemovalArray<Projectile>();
    private EnemyManager enemyManager;

    private CinematicBlock introCinematic;
    private Hud hud;
    private MusicPlayer musicPlayer;

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        hud.resize(width, height);
    }

    @Override
    public void show() {
        MessageManager.getInstance().addListeners(this, MessageCode.CINEMATIC_END);
        loadAssets();
        particleEngine = new ParticleEngine();
        hud = new Hud(WORLD_WIDTH, WORLD_HEIGHT);
        setupCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        new SoundPlayer(assetManager);
        musicPlayer = new MusicPlayer(assetManager);
        musicPlayer.play();
        setupInputProcessor();
        currentLevel = new TiledMapLevelLoader("map.tmx").generate();
        levelRenderer = new LevelRenderer(currentLevel);
        player = new Player(assetManager, currentLevel.getPlayerStartPosition());
        enemyManager = new EnemyManager(player, currentLevel.getWorld());
        SpawnerFactory.Parameters spawnParameters = new SpawnerFactory.Parameters(256, 10);
        enemyManager.addSpawners(SpawnerFactory.createSpawnersForLevel(enemyManager, currentLevel, spawnParameters));
        goat = new Goat(currentLevel.getGoatStartingPosition());
        goat.setSteeringBehavior(enemyManager.createGoatSteeringBehavior(goat));
        new PowerupHandler(assetManager);
        hud.setup(assetManager);
        introCinematic = hud.buildIntroCinematic();
    }

    private void loadAssets() {
        assetManager = new AssetManager();

        assetManager.load(Textures.CHARACTER, Texture.class);
        assetManager.load(Textures.PROJECTILE, Texture.class);
        assetManager.load(Textures.HEALTH_POWERUP, Texture.class);
        assetManager.load(Textures.DAMAGE_POWERUP, Texture.class);
        assetManager.load(Textures.SPEED_POWERUP, Texture.class);
        assetManager.load(Textures.FOG, Texture.class);
        assetManager.load(Textures.WHITE, Texture.class);
        assetManager.load(Textures.SWORD, Texture.class);

        assetManager.load(Sounds.PROJECTILE_HIT, Sound.class);
        assetManager.load(Sounds.PROJECTILE_SHOOT, Sound.class);
        assetManager.load(Sounds.SWORD_HIT, Sound.class);
        assetManager.load(Sounds.PAIN, Sound.class);
        assetManager.load(Sounds.FADEOUT_NOISE, Sound.class);
        assetManager.load(Sounds.MUSIC, Music.class);

        assetManager.finishLoading();
    }

    private void setupCamera() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        queryKeyboardInput();
        clearScreen();
        update(delta);
        draw();
        drawDebug();
        hud.update(delta);
        hud.draw();
    }

    private void queryKeyboardInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.HOME)) {
            onWin();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.END)) {
            onGameOver();
        }
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();

        levelRenderer.render(batch);
        enemyManager.draw(batch);
        player.draw(batch);
        goat.draw(batch);
        particleEngine.draw(batch);
        for (Projectile projectile: projectiles) {
            projectile.draw(batch);
        }

        batch.end();
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        player.drawDebug(shapeRenderer);
        enemyManager.drawDebug(shapeRenderer);
        for (Projectile projectile: projectiles) {
            projectile.drawDebug(shapeRenderer);
        }

        shapeRenderer.end();
    }

    private void update(float delta) {
        MessageManager.getInstance().update();
        checkForGameOver();
        GdxAI.getTimepiece().update(delta);
        player.update(delta, currentLevel);
        updateGoat(delta);
        updateProjectiles(delta);
        CollisionDetection.checkPlayerPowerupCollisions(player, levelRenderer.powerups);
        enemyManager.update(delta, projectiles);
        particleEngine.update(delta);
        updateCamera();
        introCinematic.update(delta, camera);
    }

    private void checkForGameOver() {
        if(!player.isAlive()) {
            onGameOver();
        }
    }

    private void onGameOver() {
        MessageManager.getInstance().dispatchMessage(MessageCode.OPEN_GAMEOVER_SCREEN);
    }

    private void onWin() {
        MessageManager.getInstance().dispatchMessage(MessageCode.ON_WIN);
    }

    private void updateGoat(float delta) {
        if(Intersector.overlaps(goat.getCollisionCircle(), player.getCollisionCircle())) {
            MessageManager.getInstance().dispatchMessage(0, MessageCode.GOAT_START_FOLLOW);
        }
        if(Intersector.overlaps(goat.getCollisionCircle(), currentLevel.getSafeZoneRect())) {
            onWin();
        }
        goat.update(delta);
    }

    private void updateCamera() {
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();
    }

    private void updateProjectiles(float delta) {
        projectiles.begin();
        for (Projectile projectile : projectiles) {
            projectile.update(delta);
            if (CollisionDetection.isCharacterCollidingWall(projectile, currentLevel)) {
                projectiles.removeValue(projectile, true);
            }
        }

        projectiles.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void setupInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (!introCinematic.isFinished()) {
                    introCinematic.skipAll();
                    return true;
                }

                if (button == Input.Buttons.LEFT) {
                    if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        player.castSword();
                        return true;
                    }
                    Projectile projectile =
                            new Projectile(assetManager.get(Textures.PROJECTILE, Texture.class),
                                    player.getPosition().cpy(),
                                    player.getLinearVelocity(),
                                    player.getOrientation() - MathUtils.PI / 2);
                    projectiles.add(projectile);
                    MessageManager.getInstance().dispatchMessage(MessageCode.PROJECTILE_SHOOT);
                } else if (button == Input.Buttons.RIGHT) {
                    player.castSword();
                }
                return true;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                // we can also handle mouse movement without anything pressed
                Vector3 tp = new Vector3();
                camera.unproject(tp.set(screenX, screenY, 0));
                float orientation = PFMathUtils.calcRotationAngleInRadians(player.getPosition(),
                        new Vector2(tp.x, tp.y));
                player.setOrientation(orientation);
                return true;
            }
        });
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if(msg.message == MessageCode.CINEMATIC_END) {
            camera.position.set(player.getPosition().x, player.getPosition().y, 0);
            return true;
        }
        return false;
    }

    @Override
    public void hide() {
        musicPlayer.stop();
        assetManager.dispose();
    }

}
