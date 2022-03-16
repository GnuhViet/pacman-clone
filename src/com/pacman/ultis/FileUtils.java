package com.pacman.ultis;

public class FileUtils {
    private static final String[] mapSketch = { // 21 x 21
            " ################### ",
            " #........#........# ",
            " #o##.###.#.###.##o# ",
            " #.................# ",
            " #.##.#.#####.#.##.# ",
            " #....#...#...#....# ",
            " ####.### # ###.#### ",
            "    #.#   0   #.#    ",
            "#####.# ##=## #.#####",
            "     .  #123#  .     ",
            "#####.# ##### #.#####",
            "    #.#       #.#    ",
            " ####.# ##### #.#### ",
            " #........#........# ",
            " #.##.###.#.###.##.# ",
            " #o.#.....P.....#.o# ",
            " ##.#.#.#####.#.#.## ",
            " #....#...#...#....# ",
            " #.######.#.######.# ",
            " #.................# ",
            " ################### "
    };

    public Constants.Cell[][] getMap() {
        Constants.Cell[][] mapOutput = new Constants.Cell[Constants.MAP_WIDTH][Constants.MAP_HEIGHT];

        for (int i = 0; i < Constants.MAP_WIDTH; i++) {
            String[] sub = mapSketch[i].split("");

            for (int j = 0; j < Constants.MAP_HEIGHT; j++) {
                mapOutput[i][j] = Constants.Cell.Empty;
                switch (sub[j]) {
                    case "#":
                        mapOutput[i][j] = Constants.Cell.Wall;
                        break;
                    case "=":
                        mapOutput[i][j] = Constants.Cell.Door;
                        break;
                    case ".":
                        mapOutput[i][j] = Constants.Cell.Pellet;
                        break;
                    case "o":
                        mapOutput[i][j] = Constants.Cell.Energizer;
                        // ghost, pacman, ...
                }
            }
        }
        return mapOutput;
    }
}
