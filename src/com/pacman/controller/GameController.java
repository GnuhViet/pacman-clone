package com.pacman.controller;

import com.pacman.utils.Constants;

public class GameController {


    //TODO

    public static boolean mapCollision(int iX, int iY, Constants.Cell[][] map) {
        return true;
        /*
        Constants.Cell[] cells = new Constants.Cell[4];

        double cellX = iX / (double)(Constants.CELL_SIZE);
        double cellY = iY / (double)(Constants.CELL_SIZE) - 2;

        //System.out.println(cellX + "-" + cellY);

        int downLeft = (int) Math.floor(cellX);
        int downRight = (int) Math.floor(cellY);
        int upLeft = (int) Math.ceil(cellX);
        int upRight = (int) Math.ceil(cellY);

        cells[0] = map[downLeft][downRight];
        cells[1] = map[upLeft][downRight];
        cells[2] = map[downLeft][upRight];
        cells[3] = map[upLeft][upRight];



        for (Constants.Cell x : cells) {
            if (Constants.Cell.Wall == x) {
                return true;
            }
        }
        return true;
        */

    }
    /*
    public static boolean mapCollision(boolean collectPellets, boolean useDoor, int iX, int iY, Constants.Cell[][] map) {
        boolean output = false;

        double cellX = iX / (double)(Constants.CELL_SIZE);
        double cellY = iY / (double)(Constants.CELL_SIZE);

        for (int i = 0; i < 4; i++) {
            int x = 0;
            int y = 0;

            switch (i) {
                case 0://TOP LEFT CELL
                {
                    x = (int)Math.floor(cellX);
                    y = (int)Math.floor(cellY);
                    break;
                }
                case 1: //TOP RIGHT
                {
                    x = (int)Math.ceil(cellX);
                    y = (int)Math.floor(cellY);
                }
                case 2:
                {
                    x = (int)Math.floor(cellX);
                    y = (int)Math.ceil(cellY);
                }
                case 3:
                {
                    x = (int)Math.ceil(cellX);
                    y = (int)Math.ceil(cellY);
                }
            }

            if (0 <= x && 0 <= y && Constants.MAP_HEIGHT > y && Constants.MAP_WIDTH > x)
            {
                if (!collectPellets) {
                    if (Constants.Cell.Wall == map[x][y]) {
                        output = true;
                    }
                    else if (!useDoor && Constants.Cell.Door == map[x][y]) {
                        output = true;
                    }
                }
            }
        }

        return output;
    }
       */
}
