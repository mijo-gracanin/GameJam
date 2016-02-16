package com.pixelfarmers.goat.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.pixelfarmers.goat.constants.MessageCode;
import com.pixelfarmers.goat.constants.Textures;
import com.pixelfarmers.goat.intro.CameraFocus;
import com.pixelfarmers.goat.intro.CameraPan;
import com.pixelfarmers.goat.intro.Cinematic;
import com.pixelfarmers.goat.intro.CinematicBlock;
import com.pixelfarmers.goat.intro.CompoundCinematic;
import com.pixelfarmers.goat.intro.TextCinematic;
import com.pixelfarmers.goat.intro.TextDrawer;
import com.pixelfarmers.goat.level.Level;

import java.util.ArrayList;
import java.util.List;

public class Hud implements Telegraph {

    private float width;
    private float height;

    private HeartsContainer heartsContainer;
    private Stage stage;
    private Image whiteImage;
    private boolean isFadingOut;
    private float alpha = 0;

    public Hud(float width, float height) {
        MessageManager.getInstance().addListeners(this, MessageCode.ON_WIN, MessageCode.PLAYER_HEALTH_UPDATE);
        stage = new Stage(new FitViewport(width, height));
        this.width = width;
        this.height = height;
    }

    public void setup(AssetManager assetManager) {
        whiteImage = new Image(assetManager.get(Textures.WHITE, Texture.class));

        Image fog = new Image(assetManager.get(Textures.FOG, Texture.class));
        stage.addActor(fog);

        heartsContainer = new HeartsContainer(width);
        stage.addActor(heartsContainer);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void update(float delta) {
        if (isFadingOut) {
            whiteImage.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, alpha);
            alpha += delta;
            if (alpha > 2.0f) {
                MessageManager.getInstance().dispatchMessage(MessageCode.OPEN_WIN_SCREEN);
            }
        }
        stage.act(delta);
    }

    public void draw() {
        stage.draw();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        int code = msg.message;

        if(code == MessageCode.ON_WIN) {
            isFadingOut = true;
            stage.addActor(whiteImage);
            return true;
        }

        if(code == MessageCode.PLAYER_HEALTH_UPDATE) {
            Integer newHitPoints = (Integer) msg.extraInfo;
            heartsContainer.setCount(newHitPoints);
            return true;
        }

        return false;
    }

    public CinematicBlock buildIntroCinematic() {
        MessageManager.getInstance().dispatchMessage(MessageCode.CINEMATIC_START);

        final Skin uiSkin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        final Group subtitlesContainer = new Group();
        stage.addActor(subtitlesContainer);

        TextDrawer textDrawer = new TextDrawer() {
            @Override
            public void drawText(String text) {
                final Actor subtitles = new Label(text, uiSkin);
                subtitlesContainer.clear();
                subtitlesContainer.setPosition(32, 32);
                subtitlesContainer.addActor(subtitles);
            }

            @Override
            public void clear() {
                subtitlesContainer.clear();
            }
        };

        List<Cinematic> cinematicList = new ArrayList<Cinematic>();

        Cinematic cameraFocus1 = new CameraFocus(3.0f, Level.GOAT_START_POSITION.x, Level.GOAT_START_POSITION.y);
        Cinematic cameraFocus2 = new CameraFocus(3.0f, Level.GOAT_START_POSITION.x, Level.GOAT_START_POSITION.y);
        Cinematic text1 = new TextCinematic(3.0f, 0.5f, "Evil cultists have kidnapped your goat ...", textDrawer);
        Cinematic text2 = new TextCinematic(3.0f, 0.5f, "... and are planning to do a ritual sacrifice.", textDrawer);
        Cinematic text3 = new TextCinematic(3.0f, 1.0f, "Save your goat!", textDrawer);
        Cinematic cameraPan = new CameraPan(3.0f, Level.GOAT_START_POSITION, Level.PLAYER_START_POSITION, 0.1f, 1.5f);

        cinematicList.add(new CompoundCinematic(cameraFocus1, text1));
        cinematicList.add(new CompoundCinematic(cameraFocus2, text2));
        cinematicList.add(new CompoundCinematic(cameraPan, text3));
        cinematicList.add(cameraPan);

        return new CinematicBlock(cinematicList, new CinematicBlock.CameraControl() {
            @Override
            public void returnCameraControl() {
                MessageManager.getInstance().dispatchMessage(0, MessageCode.CINEMATIC_END);
                subtitlesContainer.clear();
            }
        });
    }

}
