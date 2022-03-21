package com.pacman.entity;

import com.pacman.controller.GameController;
import com.pacman.utils.BufferedImageLoader;
import com.pacman.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Pacman extends JLabel{

    private int direction;
    Point position;

    //
    private int animationTimer;

    //
    SpriteSheet pacmanSprite;
    SpriteSheet pacmanDeadSprite;

    public Pacman() throws IOException {
        animationTimer = 0;
        position = new Point();
        pacmanSprite = new SpriteSheet(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Entity\\Pacman32.png"));
        pacmanDeadSprite = new SpriteSheet(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Entity\\PacmanDeath32.png"));
    }

    public void draw(Graphics2D g2d) {
        int frame = (int) Math.floor(animationTimer / Constants.PACMAN_ANIMATION_SPEED);

        g2d.drawImage(pacmanSprite.grabImage(direction, frame), position.x, position.y, null);

        animationTimer = (animationTimer + 1) % (Constants.PACMAN_ANIMATION_SPEED * Constants.PACMAN_ANIMATION_FRAMES);

    }

    public void setPosition(int x, int y) {
        position.setLocation(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public int getDirection() {
        return direction;
    }

    public void update(int key, Constants.Cell[][] map) {
        boolean[] wall = new boolean[4];

        // check 4 ben xung quanh co la tuong khong
        // right
        wall[0] = GameController.mapCollision(false, position.x + Constants.PACMAN_SPEED, position.y, map);
        // up
        wall[1] = GameController.mapCollision(false, position.x, position.y - Constants.PACMAN_SPEED, map);
        // left
        wall[2] = GameController.mapCollision(false, position.x - Constants.PACMAN_SPEED, position.y, map);
        // down
        wall[3] = GameController.mapCollision(false, position.x, position.y + Constants.PACMAN_SPEED, map);

        if (key == KeyEvent.VK_RIGHT) {
            if (!wall[0])
                direction = 0;
        }

        if (key == KeyEvent.VK_UP) {
            if (!wall[1])
                direction = 1;
        }

        if (key == KeyEvent.VK_LEFT) {
            if (!wall[2])
                direction = 2;
        }

        if (key == KeyEvent.VK_DOWN) {
            if (!wall[3])
                direction = 3;
        }

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

        if (position.x <= -Constants.CELL_SIZE) {
            position.x = Constants.CELL_SIZE * Constants.MAP_WIDTH - Constants.PACMAN_SPEED;
        }
        else if (position.x >= Constants.CELL_SIZE * Constants.MAP_WIDTH) {
            position.x = Constants.PACMAN_SPEED - Constants.CELL_SIZE;
        }
    }

}
