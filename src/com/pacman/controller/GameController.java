package com.pacman.controller;

import com.pacman.ultis.Constants;

public class GameController {
    //TODO

    static boolean mapCollision(boolean collectPellets, boolean useDoor, int iX, int iY, Constants.Cell[][] map) {
        boolean output = false;

        double cellX = iX / (double)(Constants.CELL_SIZE);
        double cellY = iY / (double)(Constants.CELL_SIZE);

        for (int i = 0; i < 4; i++) {
            int x = 0;
            int y = 0;

            //switch (i) {
                //case 0: //TOP LEFT CELL

            //}
        }

        return output;
    }
}
