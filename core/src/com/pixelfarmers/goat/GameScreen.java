package com.pixelfarmers.goat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixelfarmers.goat.enemy.EnemyManager;
import com.pixelfarmers.goat.enemy.Goat;
import com.pixelfarmers.goat.enemy.SpawnerFactory;
import com.pixelfarmers.goat.fx.ParticleEngine;
import com.pixelfarmers.goat.hud.Hearts;
import com.pixelfarmers.goat.level.CollisionDetection;
import com.pixelfarmers.goat.level.Level;
import com.pixelfarmers.goat.level.LevelRenderer;
import com.pixelfarmers.goat.level.TiledMapLevelLoader;
import com.pixelfarmers.goat.player.Player;
import com.pixelfarmers.goat.weapon.Projectile;

public class GameScreen extends ScreenAdapter {

    public static final float WORLD_WIDTH = 480;
    public static final float WORLD_HEIGHT = 360;

    private AssetManager assetManager;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;

    private Player player;
    private Goat goat;

    private Level currentLevel;
    private LevelRenderer levelRenderer;
    private ParticleEngine particleEngine;
    private DelayedRemovalArray<Projectile> projectiles = new DelayedRemovalArray<Projectile>();

    private EnemyManager enemyManager;

    private BitmapFont bitmapFont;
    private Texture fogTexture;
    private Stage stage;
    private Hearts heartsContainer;

    private Sound swordHitSound;
    private Sound projectileHitSound;
    private Sound projectileSound;
    private Texture projectileTexture;

    Game game;

    private Music music;

    public GameScreen(Game game) {
        this.game = game;
        bitmapFont = new BitmapFont();
        particleEngine = new ParticleEngine();
        bitmapFont.setColor(Color.WHITE);
        fogTexture = new Texture("fog.png");
        projectileTexture = new Texture("magic.png");
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        setupFog();

        assetManager = new AssetManager();
        assetManager.load(TextureFilePaths.CHARACTER, Texture.class);
        assetManager.load(TextureFilePaths.PROJECTILE, Texture.class);
        assetManager.load("goat.wav", Sound.class);
        assetManager.load("projectile_shoot.wav", Sound.class);
        assetManager.load("sword_hit.wav", Sound.class);
        assetManager.load("song.mp3", Music.class);
        assetManager.finishLoading();

        swordHitSound = assetManager.get("sword_hit.wav", Sound.class);
        projectileHitSound = assetManager.get("goat.wav", Sound.class);
        projectileSound = assetManager.get("projectile_shoot.wav", Sound.class);

        levelRenderer = new LevelRenderer();
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);

        music = assetManager.get("song.mp3", Music.class);
        music.setLooping(true);
        music.play();
        setupInputProcessor();

        init();
    }

    private void setupFog() {
        Image fog = new Image(fogTexture);
        stage.addActor(fog);
    }

    private void init() {
        player = new Player(assetManager, new Vector2(352, 352), new Player.OnHitListener() {
            @Override
            public void onHit(int newHitPoints) {
                heartsContainer.setCount(newHitPoints);
            }
        });
        goat = new Goat(new Vector2(352, 352));
        currentLevel = new TiledMapLevelLoader("map.tmx").generate();
        enemyManager = new EnemyManager(player, currentLevel.getWorld(), currentLevel, new EnemyManager.EnemyDeathListener() {
            @Override
            public void onDeath(float x, float y) {
                if (GameSettings.getInstance().getBloodLevel() == GameSettings.BloodLevel.NORMAL) {
                    levelRenderer.addBloodStain(x, y);
                }
            }
        });
        SpawnerFactory.Parameters spawnParameters = new SpawnerFactory.Parameters(256, 10);
        enemyManager.addSpawners(SpawnerFactory.createSpawnersForLevel(enemyManager, currentLevel, spawnParameters));
        heartsContainer = new Hearts(stage, WORLD_WIDTH, player);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render(float delta) {
        queryKeyboardInput();
        clearScreen();
        update(delta);
        draw(delta);
        drawDebug(delta);
        stage.act(delta);
        stage.draw();
    }

    private void queryKeyboardInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) player.goLeft();
        if (Gdx.input.isKeyPressed(Input.Keys.D)) player.goRight();
        if (Gdx.input.isKeyPressed(Input.Keys.W)) player.goUp();
        if (Gdx.input.isKeyPressed(Input.Keys.S)) player.goDown();
    }

    private void draw(float delta) {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        batch.begin();
        levelRenderer.render(batch, currentLevel);
        enemyManager.draw(batch);
        player.draw(batch);
        goat.draw(batch);
        particleEngine.draw(batch);

        for (Projectile projectile: projectiles) {
            projectile.draw(batch);
        }

        batch.end();
    }

    private void drawDebug(float delta) {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        player.drawDebug(shapeRenderer);
        enemyManager.drawDebug(shapeRenderer);
        drawDebugProjectiles(shapeRenderer);

        shapeRenderer.end();
    }

    private void drawDebugProjectiles(ShapeRenderer shapeRenderer) {
        for (Projectile projectile: projectiles) {
            projectile.drawDebug(shapeRenderer);
        }
    }

    private void update(float delta) {
        checkForGameOver();

        GdxAI.getTimepiece().update(delta);

        player.update(delta, currentLevel);
        goat.update(delta);
        updateProjectiles(delta);
        enemyManager.checkForProjectileCollisions(projectiles, particleEngine, projectileHitSound);
        enemyManager.checkForSwordCollisions(player.sword, particleEngine, swordHitSound);
        enemyManager.update(delta);
        particleEngine.update(delta);

        updateCamera();
    }

    private void updateCamera() {
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();
    }

    private void checkForGameOver() {
        if(!player.isAlive()) {
            onGameOver();
        }
    }

    private void onGameOver() {
        game.setScreen(new GameOverScreen(game));
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

            @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    Projectile projectile =
                            new Projectile(projectileTexture,
                                    player.getPosition().cpy(),
                                    player.getOrientation() - MathUtils.PI/2);
                    projectiles.add(projectile);
                    projectileSound.play();
                }
                else if (button == Input.Buttons.RIGHT) {
                    player.castSword();
                }
                return true;
            }

            @Override public boolean mouseMoved (int screenX, int screenY) {
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
    public void hide() {
        music.stop();
        assetManager.dispose();
        super.hide();
    }
}
