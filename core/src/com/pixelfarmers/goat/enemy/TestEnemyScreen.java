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
    private ShapeRenderer shapeRenderer;
    private AssetManager assetManager;

    Enemies enemies;
    Player player;

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

        assetManager = new AssetManager();
        assetManager.load(TextureFilePaths.KAMIKAZE, Texture.class);
        assetManager.finishLoading();

        enemies = new Enemies(assetManager, player);
        EnemySpawner enemySpawner = new EnemySpawner(enemies, new Vector2(250, 250), 6, 1, 3);
        enemies.addSpawner(enemySpawner);
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
        enemies.draw(batch);
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
        enemies.update(delta);
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
