package com.pixelfarmers.goat.level;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class TiledMapLevelLoader implements LevelGenerator {

    private static Tile.Type[] tileTypeMapping = {
            null,
            Tile.Type.FLOOR,
            Tile.Type.WALL
    };

    private final String filename;

    public TiledMapLevelLoader(String filename) {
        this.filename = filename;
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
                tiles[inverseRow][col] = new Tile(tileTypeMapping[index], col * Tile.TILE_SIZE, inverseRow * Tile.TILE_SIZE);
            }
        }

        return new Level(tiles);
    }
}
