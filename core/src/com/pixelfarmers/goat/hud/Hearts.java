package com.pixelfarmers.goat.hud;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pixelfarmers.goat.player.Player;

public class Hearts {
    private Texture heartTexture;
    private Texture halfHeartTexture;
    private Texture emptyHeartTexture;
    private int numOfHearts;

    private Group heartContainer;

    public Hearts(Stage stage, float screenWidth, Player player) {
        heartTexture = new Texture("heart.png");
        halfHeartTexture = new Texture("half_heart.png");
        emptyHeartTexture = new Texture("empty_heart.png");

        numOfHearts = player.getMaxHitPoints() / 2;

        heartContainer = new Group();
        heartContainer.setPosition(screenWidth/2 - 8*numOfHearts, 64);
        for (int i = 0; i < numOfHearts; i++) {
            Image image = new Image(heartTexture);
            image.setPosition(i*20, 0);
            heartContainer.addActor(image);
        }

        stage.addActor(heartContainer);
    }

    public void setCount(int count) {
        int x = 0;
        heartContainer.clear();
        for (int i = 0; i < count/2; i++) {
            Image image = new Image(heartTexture);
            image.setPosition(x, 0);
            heartContainer.addActor(image);
            x += 20;
        }
        if (count%2 == 1) {
            Image image = new Image(halfHeartTexture);
            image.setPosition(x, 0);
            heartContainer.addActor(image);
            x += 20;
        }
        for (int i = count/2 + (count%2); i < numOfHearts; i++) {
            Image image = new Image(emptyHeartTexture);
            image.setPosition(x, 0);
            heartContainer.addActor(image);
            x += 20;
        }
    }
}
