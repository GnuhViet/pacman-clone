package com.pacman.utils;

import com.pacman.controller.GhostManager;
import com.pacman.entity.Pacman;

import java.io.*;
import java.util.List;

public class FileUtils {

    private static final String gameDataPath = "src\\com\\pacman\\res\\data\\map.txt";
    private static final String gameSavePath = "src\\com\\pacman\\res\\data\\save.txt";

    private static final String[] mapSketch7 = {
            "#####################",
            "#...................#",
            "#.#.###.#####.###.#.#",
            "#.#o..#...#...#..o#.#",
            "#.###.#.#.#.#.#.###.#",
            "#.###.#.#.#.#.#.###.#",
            "........#...#........",
            "###.###.#####.###.###",
            "  #.###...0...###.#  ",
            "  #.....##=##.....#  ",
            "  #.###.#312#.###.#  ",
            "  #...#.#####.#...#  ",
            "  ###...........###  ",
            "#####.#.#####.#.#####",
            "......#...#...#......",
            "#.#.#####.#.#####.#.#",
            "#.#o......P......o#.#",
            "#.###.#.#####.#.###.#",
            "#.###.#...#...#.###.#",
            "#.....###...###.....#",
            "#####################"
    };
    private static final String[] mapSketch6 = { // 21 x 21
            "#####################",
            "#...................#",
            "#.#.###.#####.###.#.#",
            "#.#o###.#...#.###o#.#",
            "#.#.....#.#.#.....#.#",
            "#.###.#.#.#.#.#.###.#",
            "#.....#...#...#.....#",
            "###.#####.#.#####.###",
            "###...#...0...#...###",
            "....#.#.##=##.#.#....",
            "#####...#213#...#####",
            "....#.#.#####.#.#....",
            "###...#.......#...###",
            "  #.#.###.#.###.#.#  ",
            "  #.#.....#.....#.#  ",
            "###.###.#.#.#.###.###",
            "#.......#.P.#.......#",
            "#.###.#.#####.#.###.#",
            "#.#o..#.#####.#..o#.#",
            "#...#...........#...#",
            "#####################"
    };

    private static final String[] mapSketch5 = { // 21 x 21
            "#####################",
            "#.....#.......#.....#",
            "#.###.#.#####.#.###.#",
            "#o.................o#",
            "##.#.####.#.####.#.##",
            "##.#.####.#.####.#.##",
            "...#......#......#...",
            "##.####.#####.####.##",
            " #........0........# ",
            " #.####.##=##.####.# ",
            " #.##...#213#...##.# ",
            "##.##.#.#####.#.##.##",
            "......#.......#......",
            "##.######.#.######.##",
            " #........#........# ",
            "##.####.#####.####.##",
            "#.........P.........#",
            "#.###.###.#.###.###.#",
            "#o###.#...#...#.###o#",
            "#.......#...#.......#",
            "#####################"
    };

    private static final String[] mapSketch4 = { // 21 x 21
            "#####################",
            "#.......#.o.#.......#",
            "#.#####.#.#.#.#####.#",
            "#.#.......#.......#.#",
            "#...#.###.#.###.#...#",
            "###.#...........#.###",
            "....###.#####.###....",
            "#.#.......0.......#.#",
            "#.###.#.##=##.#.###.#",
            "#.....#.#213#.#.....#",
            "#o##.##.#####.##.##o#",
            "#.#...............#.#",
            "#...#.###.#.###.#...#",
            "##.##.#...#...#.##.##",
            "#.....#.#####.#.....#",
            "#.#.#.....P.....#.#.#",
            "#.###.###.#.###.###.#",
            "#.....#...#...#.....#",
            "#.###.#.#####.#.###.#",
            "#.....#...o...#.....#",
            "#####################"
    };

    private static final String[] mapSketch3 = { // 21 x 21
            "#####################",
            "......#.......#......",
            "#####.#.#####.#.#####",
            "#.........#.........#",
            "#.#####.#.#.#.#####.#",
            "#o#.....#...#.....#o#",
            "#.#.###.#####.###.#.#",
            "#.....#...0...#.....#",
            "#####.#.##=##.#.#####",
            "#.....#.#213#.#.....#",
            "#.###...#####...###.#",
            "#...#.#.......#.#...#",
            "###.#.##.###.##.#.###",
            "  #.#.##.###.##.#.#  ",
            "  #......###......#  ",
            "###.####.###.####.###",
            ".......#..P..#.......",
            "###.##.#.###.#.##.###",
            "#o#.##.#.###.#.##.#o#",
            "#...................#",
            "#####################"
    };

    private static final String[] mapSketch2 = { // 21 x 21
            "# ########.######## #",
            "#.......o# #o.......#",
            "##.##.#### ####.##.##",
            " #.................# ",
            "##.##.#.#####.#.##.##",
            " .....#...#...#..... ",
            "#####.### # ###.#####",
            "#  #..    0    ..#  #",
            "####### ##=## #######",
            "    ..# #213# #..    ",
            "#####.# ##### #.#####",
            "#o....#       #....o#",
            "#####.# ## ## #.#####",
            " #.......# #.......# ",
            "##.##.#### ####.##.##",
            "  ..#.....P.....#..  ",
            "###.#.#.#####.#.#.###",
            "#.....#.......#.....#",
            "#.##.##.##.##.##.##.#",
            "# #.....o#.#o.....# #",
            "# ########.######## #"
    };

    private static final String[] mapSketch1 = { // 21 x 21
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

    private static final String[] mapSketchz = { // 21 x 21
            " ################### ",
            " #        #        # ",
            " # ## ### # ### ## # ",
            " #                 # ",
            " # ## # ##### # ## # ",
            " #    #   #   #    # ",
            " #### ### # ### #### ",
            "    # #   0   # #    ",
            "##### # ##=## # #####",
            "        #213#        ",
            "##### # ##### # #####",
            "    # #       # #    ",
            " #### # ##### # #### ",
            " #        #        # ",
            " # ## ### # ### ## # ",
            " #  #  ...P     #  # ",
            " ## # # ##### # # ## ",
            " #    #   #   #    # ",
            " # ###### # ###### # ",
            " #                 # ",
            " ################### "
    };

    public Constants.Cell[][] loadMap(Pacman pacman, GhostManager ghost, int level) {
        File f = new File(gameDataPath);
        FileReader fr = null;
        BufferedReader br = null;

        // mapString
        String[] mapSketch;

        Constants.Cell[][] mapOutput = null;
        level--;
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);

            // dua den level can doc
            for (int i = 0; i < level; i++) {
                br.readLine();
            }

            // nhay gia tri
            mapSketch = br.readLine().split(",");
            mapOutput = getMap(pacman, ghost, mapSketch);
        } catch (IOException e) {
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
            }
        }

        return mapOutput;
    }

    public void writeMap() {
        File f = new File(gameDataPath);
        FileWriter fw = null;
        BufferedWriter bw = null;

        try {
            fw = new FileWriter(f);
            bw = new BufferedWriter(fw);

            for (int i = 0; i < Constants.MAP_WIDTH; i++) {
                bw.write(mapSketch7[i] + ","); // FIXME ?? meo chay duoc khong hieu vi sao
            }
            bw.newLine();

            for (int i = 0; i < Constants.MAP_WIDTH; i++) {
                bw.write(mapSketch1[i] + ","); // FIXME ?? meo chay duoc khong hieu vi sao
            }
            bw.newLine();

            for (int i = 0; i < Constants.MAP_WIDTH; i++) {
                bw.write(mapSketch2[i] + ","); // FIXME ?? meo chay duoc khong hieu vi sao
            }
            bw.newLine();

            for (int i = 0; i < Constants.MAP_WIDTH; i++) {
                bw.write(mapSketch3[i] + ","); // FIXME ?? meo chay duoc khong hieu vi sao
            }
            bw.newLine();

            for (int i = 0; i < Constants.MAP_WIDTH; i++) {
                bw.write(mapSketch4[i] + ","); // FIXME ?? meo chay duoc khong hieu vi sao
            }
            bw.newLine();

            for (int i = 0; i < Constants.MAP_WIDTH; i++) {
                bw.write(mapSketch5[i] + ","); // FIXME ?? meo chay duoc khong hieu vi sao
            }
            bw.newLine();

            System.out.println("xong");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Constants.Cell[][] getMap(Pacman pacman, GhostManager ghost, String[] mapSketch) {
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


    public static void main(String[] args) {
        FileUtils fileUtils = new FileUtils();
        fileUtils.writeMap();
    }
}
