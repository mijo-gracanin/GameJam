package com.pixelfarmers.goat.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixelfarmers.goat.PFMathUtils;
import com.pixelfarmers.goat.Player;

public class TestEnemyScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;

    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private AssetManager assetManager;

    Array<Enemy> enemies;
    EnemyFactory enemyFactory;
    Texture kamikazeTexture;
    Player player;
    EnemySpawner enemySpawner;
    private ShapeRenderer shapeRenderer;

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
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        player = new Player();

        kamikazeTexture = new Texture("test_enemy.png");
        enemies = new Array<Enemy>();
        assetManager = new AssetManager();
        assetManager.load(TextureFilePaths.KAMIKAZE, Texture.class);
        assetManager.finishLoading();

        enemyFactory = new EnemyFactory(assetManager);
        enemySpawner = new EnemySpawner(enemyFactory, new Vector2(0, 0), 16, 1, 2);
    }

    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        draw(delta);
    }

    private void draw(float delta) {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();

        for(Enemy enemy : enemies) {
            enemy.draw(batch);
        }
        batch.end();

        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin();
        player.drawDebug(shapeRenderer);
        shapeRenderer.end();

    }

    private void update(float delta) {
        GdxAI.getTimepiece().update(delta);
        queryInput();
        player.update(delta);

        for (Enemy enemy : enemies) {
            enemy.update(delta);
        }

        enemySpawner.update(delta);
        if(enemySpawner.isReadyToSpawn()) {
            enemies.addAll(enemySpawner.spawn(player));
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void hide() {
        assetManager.dispose();
        super.hide();
    }

    private void queryInput() {
        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.S);

        if (lPressed) player.movementDirection = Player.Movement.LEFT;
        if (rPressed) player.movementDirection = Player.Movement.RIGHT;
        if (uPressed) player.movementDirection = Player.Movement.UP;
        if (dPressed) player.movementDirection = Player.Movement.DOWN;

        if (!lPressed && !rPressed && !uPressed && !dPressed) {
            player.movementDirection = Player.Movement.STOP;
        }

        Vector2 mousePosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        float orientation = PFMathUtils.calcRotationAngleInDegrees(player.getPosition(), mousePosition);
        player.setOrientation(orientation);
    }

}
