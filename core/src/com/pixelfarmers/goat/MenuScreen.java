package com.pixelfarmers.goat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by mijo on 30/01/16.
 */
public class MenuScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;
    private Stage stage;
    private Texture backgroundTexture;
    private Texture normalButtonTexture;
    private Texture normalButtonPressTexture;
    private Texture hardcoreButtonTexture;
    private Texture hardcoreButtonPressTexture;
    private final Game game;

    public MenuScreen(Game game) {
        this.game = game;
    }

  public void show() {
    stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
    Gdx.input.setInputProcessor(stage);

      backgroundTexture = new Texture(Gdx.files.internal("PixelFarmers.jpg"));
      Image background = new Image(backgroundTexture);
      stage.addActor(background);
      background.setPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, Align.center);

      normalButtonTexture = new Texture(Gdx.files.internal("normal_button.png"));
      normalButtonPressTexture = new Texture(Gdx.files.internal("normal_button_press.png"));
      ImageButton normalButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(normalButtonTexture)),
              new TextureRegionDrawable(new TextureRegion(normalButtonPressTexture)));
      stage.addActor(normalButton);
      normalButton.setPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2 + 20, Align.bottom);

      hardcoreButtonTexture = new Texture(Gdx.files.internal("hardcore_button.png"));
      hardcoreButtonPressTexture = new Texture(Gdx.files.internal("hardcore_button_press.png"));
      ImageButton hardcoreButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(hardcoreButtonTexture)),
              new TextureRegionDrawable(new TextureRegion(hardcoreButtonPressTexture)));
      stage.addActor(hardcoreButton);
      hardcoreButton.setPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2 - 20, Align.top);

      normalButton.addListener(new ChangeListener() {
          @Override
          public void changed(ChangeListener.ChangeEvent event, Actor actor) {
              GameSettings.getInstance().setDifficulty(GameSettings.Difficulty.NORMAL);
              game.setScreen(new GameScreen(game));
          }
      });

      hardcoreButton.addListener(new ChangeListener() {
          @Override
          public void changed(ChangeListener.ChangeEvent event, Actor actor) {
              GameSettings.getInstance().setDifficulty(GameSettings.Difficulty.HARDCORE);
              game.setScreen(new GameScreen(game));
          }
      });
  }

  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  public void render(float delta) {
    stage.act(delta);
    stage.draw();
  }
}

