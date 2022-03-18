package com.pacman.entity;

import com.pacman.controller.GameController;
import com.pacman.utils.BufferedImageLoader;
import com.pacman.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Pacman extends JLabel{
    private boolean animationOver;
    private boolean dead;

    private int direction;

    Point position;
    private boolean[] wall;
    //
    private int animationTimer;
    private int energizerTimer;

    //
    SpriteSheet pacmanSprite;
    SpriteSheet pacmanDeadSprite;

    public Pacman() throws IOException {
        animationTimer = 0;
        position = new Point();
        direction = 0;
        pacmanSprite = new SpriteSheet(BufferedImageLoader.loadImage("src/com/pacman/res/Entity/Pacman16.png"));
        pacmanDeadSprite = new SpriteSheet(BufferedImageLoader.loadImage("src/com/pacman/res/Entity/PacmanDeath16.png"));

        wall = new boolean[4];
    }

    public boolean isAnimationOver() {
        return animationOver;
    }

    public boolean isDead() {
        return dead;
    }

    public int getDirection() {
        return direction;
    }

    public int getAnimationTimer() {
        return animationTimer;
    }

    public void draw(Graphics2D g2d) {

        int frame = animationTimer / Constants.PACMAN_ANIMATION_SPEED;
        //System.out.println(position.x + "move" + position.y);

        g2d.drawImage(pacmanSprite.grabImage(0,0), position.x, position.y, null);

        animationTimer = (1 + animationTimer) % (Constants.PACMAN_ANIMATION_SPEED * Constants.PACMAN_DEATH_FRAMES);
    }

    public void setPosition(int x, int y) {
        position.setLocation(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public void update() {
        if (wall[direction]) {
            switch (direction) {
                case 0:
                    position.x += Constants.PACMAN_SPEED;
                    break;
                case 1:
                    position.y -= Constants.PACMAN_SPEED;
                    break;
                case 2:
                    position.x -= Constants.PACMAN_SPEED;
                    break;
                case 3:
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


    public void keyPressed(int key, Constants.Cell[][] map) {

        wall[0] = GameController.mapCollision(Constants.PACMAN_SPEED + position.x, position.y, map);
        wall[1] = GameController.mapCollision(position.x, position.y - Constants.PACMAN_SPEED, map);
        wall[2] = GameController.mapCollision(position.x - Constants.PACMAN_SPEED, position.y, map);
        wall[3] = GameController.mapCollision(position.x, Constants.PACMAN_SPEED +position.y, map);

        if (key == KeyEvent.VK_RIGHT && wall[0]) {
            direction = 0;
        }

        if (key == KeyEvent.VK_UP && wall[1]) {
            direction = 1;
        }

        if (key == KeyEvent.VK_LEFT && wall[2]) {
            direction = 2;
        }

        if (key == KeyEvent.VK_DOWN && wall[3]) {
            direction = 3;
        }
    }

}
