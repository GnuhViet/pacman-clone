package com.pacman.utils;

public interface Constants {
    public static final int CELL_SIZE = 32; // SPRITE size
    public static final int MAP_HEIGHT = 21;
    public static final int MAP_WIDTH = 21;
    public static final int FONT_SIZE = 18;

    public static final int SCREEN_WIDTH  = (CELL_SIZE * MAP_WIDTH ) + CELL_SIZE / 2;
    public static final int SCREEN_HEIGHT = (CELL_SIZE * MAP_HEIGHT) + CELL_SIZE * 3;

    public static final int PACMAN_ANIMATION_FRAMES = 11;
    public static final int PACMAN_ANIMATION_SPEED = 3;
    public static final int PACMAN_DEATH_FRAMES = 12;
    public static final int PACMAN_SPEED = 2;

    public static final int GHOST_SPEED = 2;
    public static final int GHOST_ANIMATION_SPEED = 10;
    public static final int GHOST_ANIMATION_FRAMES = 2;

    public static final int FPS = 60;

    enum Cell {
        Door,
        Empty,
        Energizer,
        Pellet,
        Wall
    };

}
