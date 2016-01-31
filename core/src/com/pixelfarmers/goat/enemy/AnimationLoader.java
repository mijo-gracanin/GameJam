package com.pixelfarmers.goat.enemy;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;


public class AnimationLoader {
    private static AnimationLoader instance;

    private Map<String, Animation> loadedAnimations;

    public static AnimationLoader getInstance() {
        if (instance == null) {
            instance = new AnimationLoader();
        }
        return instance;
    }

    public AnimationLoader() {
        this.loadedAnimations = new HashMap<String, Animation>();
    }

    public Animation getAnimation(String baseFilename, int width, int height, int row) {
        Animation animation = loadedAnimations.get(baseFilename);
        if (animation == null) {
            TextureRegion[][] textureRegions = TextureRegion.split(new Texture(baseFilename + ".png"), width, height);
            animation = new Animation(0.1f, textureRegions[row]);
            loadedAnimations.put(baseFilename, animation);
        }
        return animation;
    }
}
