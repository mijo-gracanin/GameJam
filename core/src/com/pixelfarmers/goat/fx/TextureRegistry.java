package com.pixelfarmers.goat.fx;


import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class TextureRegistry {
    private Map<String, Texture> textureRegistry;

    private static TextureRegistry instance;

    private TextureRegistry() {
        textureRegistry = new HashMap<String, Texture>();
        textureRegistry.put("blood_particle", new Texture("blood_particle.png"));
        textureRegistry.put("blood_stain", new Texture("blood_stain.png"));
    }

    public static TextureRegistry getInstance() {
        if (instance == null) {
            instance = new TextureRegistry();
        }
        return instance;
    }

    public Texture getTexture(String key) {
        return textureRegistry.get(key);
    }
}
