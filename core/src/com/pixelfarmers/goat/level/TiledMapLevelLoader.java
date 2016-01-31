package com.pixelfarmers.goat.level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;
import java.util.Map;

public class TiledMapLevelLoader implements LevelGenerator {

    private static Map<Integer, Boolean> tilePassabilityMap;

    private final String filename;

    public TiledMapLevelLoader(String filename) {
        this.filename = filename;

        tilePassabilityMap = new HashMap<Integer, Boolean>();
        tilePassabilityMap.put(1, true);
        tilePassabilityMap.put(2, true);
        tilePassabilityMap.put(3, false);
        tilePassabilityMap.put(4, false);
        tilePassabilityMap.put(5, false);
        tilePassabilityMap.put(9, true);

        tilePassabilityMap.put(10, true);
        tilePassabilityMap.put(11, true);
        tilePassabilityMap.put(12, true);
        tilePassabilityMap.put(13, true);

        tilePassabilityMap.put(17, true);
        tilePassabilityMap.put(18, true);
        tilePassabilityMap.put(19, true);
        tilePassabilityMap.put(21, true);
        tilePassabilityMap.put(22, true);

        tilePassabilityMap.put(26, true);
        tilePassabilityMap.put(27, true);

        tilePassabilityMap.put(33, false);
        tilePassabilityMap.put(34, false);
        tilePassabilityMap.put(35, false);

        tilePassabilityMap.put(41, false);
        tilePassabilityMap.put(42, false);
        tilePassabilityMap.put(43, false);

        tilePassabilityMap.put(49, false);
        tilePassabilityMap.put(50, false);
        tilePassabilityMap.put(51, false);
    }

    @Override
    public Level generate() {
        FileHandle file = Gdx.files.internal(filename);
        String contents = file.readString();
        String[] lines = contents.split("\\s+");

        final int rows = lines.length;
        final int cols = lines[0].split(",").length;

        Tile[][] tiles = new Tile[rows][cols];

        for (int row = 0; row < rows; row++) {
            String[] values = lines[row].split(",");

            for (int col = 0; col < cols; col++) {
                int index = Integer.valueOf(values[col]);
                int inverseRow = rows - row - 1;
                tiles[inverseRow][col] = new Tile(index, tilePassabilityMap.get(index), col * Tile.TILE_SIZE, inverseRow * Tile.TILE_SIZE);
            }
        }

        return new Level(tiles);
    }
}
