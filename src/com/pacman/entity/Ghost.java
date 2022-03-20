package com.pacman.entity;

import com.pacman.controller.GameController;
import com.pacman.utils.BufferedImageLoader;
import com.pacman.utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class Ghost {

    private int direction;
    Point position;

    //
    private int animationTimer;

    //
    SpriteSheet ghostSprite;

    public Ghost() throws IOException {
        animationTimer = 0;
        position = new Point();
        ghostSprite = new SpriteSheet(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Entity\\Ghost32.png"));
    }


    public void draw(Graphics2D g2d) {
        int frame = (int) Math.floor(animationTimer / Constants.GHOST_ANIMATION_SPEED);

        g2d.drawImage(ghostSprite.grabImage(0, frame), position.x, position.y, null);
        g2d.drawImage(ghostSprite.grabImage(1, direction), position.x, position.y, null);

        animationTimer = (animationTimer + 1) % (Constants.GHOST_ANIMATION_SPEED * Constants.GHOST_ANIMATION_FRAMES);
    }

    public void setPosition(int x, int y) {
        position.setLocation(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public void update(int direction,Constants.Cell[][] map) {
        boolean[] wall = new boolean[4];

        // check 4 ben xung quanh co la tuong khong
        // right
        wall[0] = GameController.mapCollision(false, position.x + Constants.GHOST_SPEED, position.y, map);
        // up
        wall[1] = GameController.mapCollision(false, position.x, position.y - Constants.GHOST_SPEED, map);
        // left
        wall[2] = GameController.mapCollision(false, position.x - Constants.GHOST_SPEED, position.y, map);
        // down
        wall[3] = GameController.mapCollision(false, position.x, position.y + Constants.GHOST_SPEED, map);

        this.direction = direction;

        if (!wall[direction]) {
            switch (direction) {
                case 0: //RIGHT
                    position.x += Constants.PACMAN_SPEED;
                    break;
                case 1: //UP
                    position.y -= Constants.PACMAN_SPEED;
                    break;
                case 2: //LEFT
                    position.x -= Constants.PACMAN_SPEED;
                    break;
                case 3: //DOWN
                    position.y += Constants.PACMAN_SPEED;
            }
        }

        if (-Constants.CELL_SIZE >= position.x) {
            position.x = Constants.CELL_SIZE * Constants.MAP_WIDTH - Constants.PACMAN_SPEED;
        }
        else if (Constants.CELL_SIZE * Constants.MAP_WIDTH <= position.x) {
            position.x = Constants.PACMAN_SPEED - Constants.CELL_SIZE;
        }
    }

}
