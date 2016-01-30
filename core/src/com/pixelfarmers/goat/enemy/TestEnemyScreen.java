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
import com.pixelfarmers.goat.player.Player;

public class TestEnemyScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;

    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private AssetManager assetManager;

    EnemyManager enemyManager;
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

        player = new Player(32, 32);

        assetManager = new AssetManager();
        assetManager.load(TextureFilePaths.KAMIKAZE, Texture.class);
        assetManager.finishLoading();

        enemyManager = new EnemyManager(assetManager, player);
        EnemySpawner enemySpawner = new EnemySpawner(enemyManager, new Vector2(250, 250), 6, 1, 3);
        enemyManager.addSpawner(enemySpawner);
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
        enemyManager.draw(batch);
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
        enemyManager.update(delta);
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
        boolean escPressed = Gdx.input.isKeyPressed(Input.Keys.ESCAPE);

        if (escPressed) {
            Gdx.app.exit();
        }

        if (lPressed) player.goLeft();
        if (rPressed) player.goRight();
        if (uPressed) player.goUp();
        if (dPressed) player.goDown();

        Vector2 mousePosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        if (mousePosition.x >= 0 && mousePosition.x < WORLD_WIDTH &&
                mousePosition.y >= 0 && mousePosition.y < WORLD_HEIGHT) {
            mousePosition.y = WORLD_HEIGHT - mousePosition.y; // Mouse origin is at TOP left
        }
        Gdx.app.log("Mouse", "" + mousePosition.x + " " + mousePosition.y);
        float orientation = PFMathUtils.calcRotationAngleInRadians(player.getPosition(), mousePosition);
        player.setOrientation(orientation);
    }

}
