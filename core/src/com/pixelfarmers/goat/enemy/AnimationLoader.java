package com.pixelfarmers.goat.enemy;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

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

    public Animation getAnimation(String baseFilename) {
        Animation animation = loadedAnimations.get(baseFilename);
        if (animation == null) {
            Array<TextureRegion> frames = new Array<TextureRegion>();
            for (int i = 1; i <= 3; i++) {
                Texture texture = new Texture(baseFilename + "_" + i + ".png");
                frames.add(new TextureRegion(texture));
            }
            animation = new Animation(0.1f, frames);
            loadedAnimations.put(baseFilename, animation);
        }
        return animation;
    }
}
