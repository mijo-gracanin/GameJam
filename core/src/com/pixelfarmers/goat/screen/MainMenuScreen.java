package com.pixelfarmers.goat.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pixelfarmers.goat.GameSettings;


public class MainMenuScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = GameScreen.WORLD_WIDTH;
    private static final float WORLD_HEIGHT = GameScreen.WORLD_HEIGHT;
    private Stage stage;
    private Texture backgroundTexture;
    private Texture normalButtonTexture;
    private Texture normalButtonPressTexture;
    private Texture hardcoreButtonTexture;
    private Texture hardcoreButtonPressTexture;
    private Texture creditsButtonTexture;
    private Texture creditsButtonPressTexture;
    private Skin gameUISkin;
    private BitmapFont font;
    private final Game game;

    public MainMenuScreen(Game game) {
        this.game = game;
    }

    public void show() {
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("menu_bg.png"));
        Image background = new Image(backgroundTexture);
        stage.addActor(background);
        background.setPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, Align.center);

        gameUISkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        font = new BitmapFont(Gdx.files.internal("ui/Xeliard20.fnt"));

        addNormalButton();
        addHardcoreButton();
        addCreditsButton();
    }

    private void addNormalButton() {
        normalButtonTexture = new Texture(Gdx.files.internal("normal_button.png"));
        normalButtonPressTexture = new Texture(Gdx.files.internal("normal_button_press.png"));
        ImageButton normalButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(normalButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(normalButtonPressTexture)));
        stage.addActor(normalButton);
        normalButton.setPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2 - 50, Align.bottom);

        normalButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                GameSettings.getInstance().setDifficulty(GameSettings.Difficulty.NORMAL);
                game.setScreen(new GameScreen(game));
            }
        });
    }

    private void addHardcoreButton() {
        hardcoreButtonTexture = new Texture(Gdx.files.internal("hardcore_button.png"));
        hardcoreButtonPressTexture = new Texture(Gdx.files.internal("hardcore_button_press.png"));
        ImageButton hardcoreButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(hardcoreButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(hardcoreButtonPressTexture)));
        stage.addActor(hardcoreButton);
        hardcoreButton.setPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2 - 70, Align.top);

        hardcoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                GameSettings.getInstance().setDifficulty(GameSettings.Difficulty.HARDCORE);
                game.setScreen(new GameScreen(game));
            }
        });
    }

    private void addCreditsButton() {
        creditsButtonTexture = new Texture(Gdx.files.internal("credits_button.png"));
        creditsButtonPressTexture = new Texture(Gdx.files.internal("credits_button_press.png"));
        ImageButton creditsButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(creditsButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(creditsButtonPressTexture)));
        stage.addActor(creditsButton);
        creditsButton.setPosition(WORLD_WIDTH / 2, 10, Align.bottom);

        creditsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                showCredits();
            }
        });
    }

    private void showCredits() {
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
        Label label1 = new Label("Game creators:\n\nMatej Vidakovic\nDario Sikirica\nMijo Gracanin\n\n" +
                "Thanks to Alex Glukhov (glukhov.me) for creating\nXeliard Font\n" +
                "Thanks to barker852 from freesound.org for \nPain_1.wav \n" +
                "Thanks to Trash-Man-1 from newgrounds.com for \nDeamon Car Takedown track", style);
        label1.setAlignment(Align.center);
        label1.setFontScale(0.7f);
        label1.setWrap(true);
        style.fontColor = Color.WHITE;

        Texture okButtonTexture = new Texture(Gdx.files.internal("ok_button.png"));
        Texture okButtonPressTexture = new Texture(Gdx.files.internal("ok_button_press.png"));
        ImageButton okButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(okButtonTexture)),
                new TextureRegionDrawable(new TextureRegion(okButtonPressTexture)));

        // /////////////////
        final Dialog dialog = new Dialog("Created for Global Game Jam 2016", gameUISkin) {
            @Override
            public float getPrefWidth() {
                return WORLD_WIDTH - 80;
            }

            @Override
            public float getPrefHeight() {
                return WORLD_HEIGHT - 80;
            }
        };
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.setResizable(false);

        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                dialog.hide();
                dialog.cancel();
                dialog.remove();
            }
        });

        dialog.setColor(Color.BLACK);

        float btnSize = 80f;
        Table t = new Table();
        // t.debug();

        dialog.getContentTable().add(label1).padTop(30f);

        t.add(okButton).width(btnSize).height(btnSize);

        dialog.getButtonTable().add(t).bottom().padBottom(0f);
        dialog.show(stage).setPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, Align.center);

        dialog.setName("Credits");
        stage.addActor(dialog);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
        stage.act(delta);
        stage.draw();
    }
}

