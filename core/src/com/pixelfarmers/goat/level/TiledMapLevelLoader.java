package com.pixelfarmers.goat.level;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class TiledMapLevelLoader implements LevelGenerator {
    private final AssetManager assetManager;
    private final TiledMap tiledMap;

    public TiledMapLevelLoader(String filename) {
        assetManager = new AssetManager();
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load("map_test.tmx", TiledMap.class);
        tiledMap = assetManager.get("map_test.tmx");
    }

    @Override
    public Level generate() {
        return null;
    }
}
