package com.pacman.utils;

import com.pacman.controller.GhostManager;
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
            "    #.#   0   #.#    ",
            "#####.# ##=## #.#####",
            "     .  #213#  .     ",
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

    public Constants.Cell[][] getMap(Pacman pacman, GhostManager ghost) {
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
                        pacman.setPosition(j, i);
                        break;
                    case "0":
                        ghost.setPosition(GhostManager.GhostType.RED, j, i);
                        break;
                    case "1":
                        ghost.setPosition(GhostManager.GhostType.PINK, j, i);
                        break;
                    case "2":
                        ghost.setPosition(GhostManager.GhostType.BLUE, j, i);
                        break;
                    case "3":
                        ghost.setPosition(GhostManager.GhostType.ORANGE, j, i);
                        break;
                    case "o":
                        mapOutput[i][j] = Constants.Cell.Energizer;
                }
            }
        }
        return mapOutput;
    }
}
