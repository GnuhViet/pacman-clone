package com.pacman.controller;

import com.pacman.utils.Constants;

public class GameController {
    public static Constants.Cell mapUpdate(int x, int y, Constants.Cell[][] map) {
        if (Constants.Cell.Energizer == map[y][x]) {
            return Constants.Cell.Empty;
        }
        //pellet
        return Constants.Cell.Empty;

    }

    public static boolean mapCollision(boolean iUseDoor, int iX, int iY, Constants.Cell[][] map) {
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

            // kiem tra xem vi tri co trong map khong
            if (0 <= x && 0 <= y && Constants.MAP_HEIGHT > y && Constants.MAP_WIDTH > x)
            {
                if (Constants.Cell.Wall == map[y][x]) {
                    output = true;
                }
                else if (iUseDoor == false && Constants.Cell.Door == map[y][x]) {
                    output = true;
                }
            }
        }

        return output;
    }

}
