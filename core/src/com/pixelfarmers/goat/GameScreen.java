package com.pixelfarmers.goat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pixelfarmers.goat.level.Level;
import com.pixelfarmers.goat.level.LevelRenderer;
import com.pixelfarmers.goat.level.MockLevelGenerator;
import com.pixelfarmers.goat.player.Player;

public class GameScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;

    private Player player;
    private Level currentLevel;
    private LevelRenderer levelRenderer;

    private BitmapFont bitmapFont;

    public GameScreen() {
        bitmapFont = new BitmapFont();
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
        player = new Player(32, 32);
        levelRenderer = new LevelRenderer();
        currentLevel = new MockLevelGenerator().generate();

        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
    }

    @Override
    public void render(float delta) {
        queryInput();
        update(delta);
        clearScreen();

        draw(delta);
        drawDebug(delta);
    }

    private void draw(float delta) {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        batch.begin();
        // Draw sprites
        levelRenderer.render(batch, currentLevel);
        batch.end();
    }

    private void drawDebug(float delta) {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        player.drawDebug(shapeRenderer);

        shapeRenderer.end();
    }

    private void update(float delta) {
        player.update(delta);
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
