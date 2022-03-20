package com.pacman.utils;

import com.pacman.entity.Ghost;
import com.pacman.entity.Pacman;

public class FileUtils {
    private static final String[] mapSketch = { // 21 x 21
            " ################### ",
            " #........#........# ",
            " #o##.###.#.###.##o# ",
            " #.................# ",
            " #.##.#.#####.#.##.# ",
            " #....#...#...#....# ",
            " ####.### # ###.#### ",
            "    #.#   1   #.#    ",
            "#####.# ##=## #.#####",
            "     .  #   #  .     ",
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

    private static final String[] mapSketch12 = { // 21 x 21
            "                     ",
            "                     ",
            "                     ",
            "                     ",
            "                     ",
            "     #         #     ",
            "     #         #     ",
            "     #    P    #     ",
            "                     ",
            "       ##   ##       ",
            "        #####        ",
            "                     ",
            "                     ",
            "                     ",
            "                     ",
            "                     ",
            "                     ",
            "                     ",
            "                     ",
            "                     ",
            " ################### "
    };

    public Constants.Cell[][] getMap(Pacman pacman, Ghost ghost) {
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
                    case "P":
                        pacman.setPosition(j + 1,i + 1);
                        break;
                    case "1":
                    case "2":
                    case "3":
                        ghost.setPosition(j + 1, i + 1);
                        break;
                    case "o":
                        mapOutput[i][j] = Constants.Cell.Energizer;
                        // ghost
                }
            }
        }
        return mapOutput;
    }
}
