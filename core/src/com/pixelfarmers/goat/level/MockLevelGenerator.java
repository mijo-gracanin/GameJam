package com.pixelfarmers.goat.level;


public class MockLevelGenerator implements LevelGenerator {
    @Override
    public Level generate() {
        String[] level = {
                "********************",
                "*..................*",
                "*..................*",
                "*.........*******..*",
                "*.........*........*",
                "*.........*........*",
                "*.........*........*",
                "*.........*........*",
                "*..................*",
                "*..................*",
                "********************",
        };

        final int width = level[0].length();
        final int height = level.length;
        Tile[][] tiles = new Tile[height][width];

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                tiles[h][w] = level[h].charAt(w) == '*' ? Tile.WALL : Tile.FLOOR;
            }
        }

        return new Level(tiles);
    }
}
