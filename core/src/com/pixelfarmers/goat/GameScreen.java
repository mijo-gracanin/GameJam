package com.pixelfarmers.goat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.assets.AssetManager;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixelfarmers.goat.enemy.EnemyManager;
import com.pixelfarmers.goat.enemy.SpawnerFactory;
import com.pixelfarmers.goat.enemy.TextureFilePaths;
import com.pixelfarmers.goat.fx.ParticleEngine;
import com.pixelfarmers.goat.level.CollisionDetection;
import com.pixelfarmers.goat.level.Level;
import com.pixelfarmers.goat.level.LevelRenderer;
import com.pixelfarmers.goat.level.TiledMapLevelLoader;
import com.pixelfarmers.goat.player.Player;
import com.pixelfarmers.goat.projectile.Projectile;

public class GameScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private AssetManager assetManager;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;

    private Player player;
    private Level currentLevel;
    private LevelRenderer levelRenderer;
    private ParticleEngine particleEngine;
    private Array<Projectile> projectiles = new Array<Projectile>();
    private Array<Projectile>projectilesForRemoval = new Array<Projectile>();

    private EnemyManager enemyManager;

    private BitmapFont bitmapFont;

    public GameScreen() {
        bitmapFont = new BitmapFont();
        particleEngine = new ParticleEngine();
        bitmapFont.setColor(Color.WHITE);
        Gdx.graphics.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        assetManager = new AssetManager();
        assetManager.load(TextureFilePaths.KAMIKAZE, Texture.class);
        assetManager.finishLoading();

        player = new Player(32, 32);
        levelRenderer = new LevelRenderer();
        currentLevel = new TiledMapLevelLoader("test_level.tmx").generate();
        enemyManager = new EnemyManager(assetManager, player, currentLevel.getWorld());
        enemyManager.addSpawners(SpawnerFactory.createSpawnersForLevel(enemyManager, 1));
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);

        setupInputProcessor();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render(float delta) {
        queryKeyboardInput();
        update(delta);
        clearScreen();
        draw(delta);
        drawDebug(delta);
    }

    private void queryKeyboardInput() {
        boolean escPressed = Gdx.input.isKeyPressed(Input.Keys.ESCAPE);

        if (escPressed) {
            Gdx.app.exit();
        }

        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.S);

        if (lPressed) player.goLeft();
        if (rPressed) player.goRight();
        if (uPressed) player.goUp();
        if (dPressed) player.goDown();
    }

    private void draw(float delta) {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        batch.begin();
        levelRenderer.render(batch, currentLevel);
        enemyManager.draw(batch);
        particleEngine.draw(batch);
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
        GdxAI.getTimepiece().update(delta);
        player.update(delta, currentLevel);
        enemyManager.update(delta);
        updateProjectiles(delta);
        particleEngine.update(delta);

        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();
    }

    private void updateProjectiles(float delta) {
        for (Projectile projectile : projectiles) {
            projectile.update(delta);
            if (CollisionDetection.isCharacterCollidingWall(projectile, currentLevel)) {
                projectilesForRemoval.add(projectile);
            }
        }

        projectiles.removeAll(projectilesForRemoval, true);
        projectilesForRemoval.clear();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void setupInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    Projectile projectile = new Projectile(player.getPosition().cpy(),
                            player.getOrientation() - MathUtils.PI/2);
                    projectiles.add(projectile);
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
        assetManager.dispose();
        super.hide();
    }
}
