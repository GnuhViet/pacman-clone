package com.pacman.controller;

import com.pacman.utils.Constants;

public class GameController {

    public static boolean mapCollision(boolean iCollectPellet, boolean iUseDoor, int iX, int iY, Constants.Cell[][] map) {
        boolean output = false;

        double cellX = iX / (double)(Constants.CELL_SIZE);
        double cellY = iY / (double)(Constants.CELL_SIZE);
        System.out.println(cellX + "," + cellY + " ");

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
                    break;
                }
                case 2:
                {
                    x = (int)Math.floor(cellX);
                    y = (int)Math.ceil(cellY);
                    break;
                }
                case 3:
                {
                    x = (int)Math.ceil(cellX);
                    y = (int)Math.ceil(cellY);
                }
            }


            if (0 <= x && 0 <= y && Constants.MAP_HEIGHT > y && Constants.MAP_WIDTH > x)
            {
                if (iCollectPellet == false) {
                    if (Constants.Cell.Wall == map[y][x]) {
                        output = true;
                    }
                    else if (iUseDoor == false && Constants.Cell.Door == map[y][x]) {
                        output = true;
                    }
                }
                else {
                    if (Constants.Cell.Energizer == map[y][x]) {
                        output = true;
                        map[y][x] = Constants.Cell.Empty;
                    }
                    else if (Constants.Cell.Pellet == map[y][x]) {
                        map[y][x] = Constants.Cell.Empty;
                    }
                }
            }
        }

        return output;
    }

}
