package com.pacman.entity;

import com.pacman.utils.Constants;

public class Map {
    private Constants.Cell[][] map;
    private boolean off = false;
    private long offTime;
    private long onTime;

    public Map(Constants.Cell[][] map) {
        this.map = map;
    }

    public void setMap(int x, int y, Constants.Cell element) {
        map[y][x] = element;
    }

    public Constants.Cell getMapItem(int x, int y) {
        return map[y][x];
    }

    public Constants.Cell[][] getMap() {
        return map;
    }

    public boolean isEnergizerOff() {
        return off;
    }

    public void UpdateEnergizerBlinkTime(long timer) {
        //for blinking energizer
        if (off) {
            offTime += timer;
        } else {
            onTime += timer;
        }

        if (onTime  >= 800000000) {
            off = true;
            onTime = 0;
        }
        if (offTime >= 800000000) {
            off = false;
            offTime = 0;
        }
    }

    public boolean mapCollision(boolean iUseDoor, int iX, int iY) {
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

    public boolean isClear() {
        for (int i = 0; i < Constants.MAP_WIDTH; i++) {
            for (int j = 0; j < Constants.MAP_HEIGHT; j++) {
                if (map[i][j] == Constants.Cell.Pellet || map[i][j] == Constants.Cell.Energizer) {
                    return false;
                }
            }
        }
        return true;
    }
}
