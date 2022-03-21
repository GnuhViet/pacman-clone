package com.pacman.controller;

import com.pacman.entity.Ghost;
import com.pacman.entity.Pacman;
import com.pacman.utils.Constants;

import java.awt.*;
import java.io.IOException;

public class GhostManager {
    public static final int NUMBER_OF_GHOSTS = 4;

    public enum GhostType{
        RED,
        PINK,
        BLUE,
        ORANGE,
        FRIGHTENED
    }

    private final Ghost[] ghosts;

    public GhostManager() throws IOException {
        ghosts = new Ghost[NUMBER_OF_GHOSTS];
        ghosts[0] = new Ghost(GhostType.RED);
        ghosts[1] = new Ghost(GhostType.PINK);
        ghosts[2] = new Ghost(GhostType.BLUE);
        ghosts[3] = new Ghost(GhostType.ORANGE);
    }

    public void reset() {
        // blue la house, red la exit
        for (int i = 0; i < NUMBER_OF_GHOSTS; i++) {
            ghosts[i].reset(ghosts[2].getPosition(), ghosts[0].getPosition());
        }
    }

    public Ghost getGhost(GhostType type) {
        return ghosts[type.ordinal()];
    }

    public void update(Constants.Cell[][] mapInput, Point pacmanPos, int pacmanDirection) {
        for (int i = 0; i < NUMBER_OF_GHOSTS; i++) {
            ghosts[i].update(mapInput, pacmanPos, pacmanDirection, ghosts[0].getPosition());
        }
    }

    public void draw(Graphics2D g2d) {
        for (int i = 0; i < 4; i++) {
            ghosts[i].draw(i, g2d);
        }
    }

    public void setPosition(GhostType type, int x, int y) {
        ghosts[type.ordinal()].setPosition(x, y);
    }

    public void initPos() {
        for (int i = 0; i < NUMBER_OF_GHOSTS; i++) {
            int ghostX = ((ghosts[i].getX() * Constants.CELL_SIZE) - Constants.CELL_SIZE);
            int ghostY = ((ghosts[i].getY() * Constants.CELL_SIZE) - Constants.CELL_SIZE);
            ghosts[i].setPosition(ghostX, ghostY);
        }
    }
}
