package com.pacman.ultis;

public interface Constants {
    public static final int CELL_SIZE = 32; // SPRITE size
    public static final int MAP_HEIGHT = 21;
    public static final int MAP_WIDTH = 21;
    public static final int SCREEN_RESIZE = 1;
    public static final int FONT_SIZE = 18;

    public static final int SCREEN_WIDTH  = (CELL_SIZE / 2 + CELL_SIZE * MAP_WIDTH )* SCREEN_RESIZE;
    public static final int SCREEN_HEIGHT = (CELL_SIZE * 4 + CELL_SIZE * MAP_HEIGHT)* SCREEN_RESIZE;

    public static final int PACMAN_ANIMATION_FRAMES = 6;
    public static final int PACMAN_ANIMATION_SPEED = 4;
    public static final int PACMAN_DEATH_FRAMES = 12;
    public static final int PACMAN_SPEED = 2;

    enum Cell {
        Door,
        Empty,
        Energizer,
        Pellet,
        Wall
    };

}
