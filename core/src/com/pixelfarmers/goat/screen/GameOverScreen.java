package com.pixelfarmers.goat.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pixelfarmers.goat.constants.MessageCode;

public class GameOverScreen extends ScreenAdapter {

    private static final int WORLD_WIDTH = 640;
    private static final int WORLD_HEIGHT = 480;
    private static String GAME_OVER_MESSAGE = "Game over. Tap R to restart";

    private BitmapFont bitmapFont;
    private SpriteBatch spriteBatch;
    private GlyphLayout glyphLayout;

    private OrthographicCamera camera;
    private FitViewport viewport;

    public GameOverScreen() {
        bitmapFont = new BitmapFont(Gdx.files.internal("ui/Xeliard.fnt"));
        glyphLayout = new GlyphLayout();
        bitmapFont.setColor(Color.WHITE);
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.update();
    }

    @Override
    public void render(float delta) {
        viewport.apply();

        clearScreen();

        spriteBatch.setProjectionMatrix(camera.projection);
        spriteBatch.setTransformMatrix(camera.view);
        spriteBatch.begin();
        glyphLayout.setText(bitmapFont, GAME_OVER_MESSAGE);
        bitmapFont.draw(spriteBatch, glyphLayout, WORLD_WIDTH / 2 - glyphLayout.width / 2, WORLD_HEIGHT / 2- glyphLayout.height / 2);
        spriteBatch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.R)) {
            MessageManager.getInstance().dispatchMessage(MessageCode.OPEN_GAME_SCREEN);
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

}
