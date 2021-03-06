package com.pixelfarmers.goat.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pixelfarmers.goat.constants.Textures;

public class FinishScreen extends ScreenAdapter {

    private Stage stage;
    private Image background, white;

    public FinishScreen() {
        Texture backgroundTexture = new Texture(Gdx.files.internal("win_bg.png"));
        background = new Image(backgroundTexture);
        white = new Image(new Texture(Gdx.files.internal(Textures.WHITE)));
    }

    @Override
    public void show() {
        super.show();
        stage = new Stage(new FitViewport(GameScreen.WORLD_WIDTH, GameScreen.WORLD_HEIGHT));
        stage.addActor(background);
        white.setColor(Color.WHITE);
        stage.addActor(white);
    }

    float alpha = 2.0f;

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();

        if (alpha > 0) {
            alpha -= delta;
        }

        white.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, alpha);
    }
}
