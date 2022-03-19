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

    //
    private int animationTimer;
    private int energizerTimer;

    //
    SpriteSheet pacmanSprite;
    SpriteSheet pacmanDeadSprite;

    // collision
    public Rectangle solidArea;

    public Pacman() throws IOException {
        animationTimer = 0;
        position = new Point();
        pacmanSprite = new SpriteSheet(BufferedImageLoader.loadImage("src/com/pacman/res/Entity/Pacman16.png"));
        pacmanDeadSprite = new SpriteSheet(BufferedImageLoader.loadImage("src/com/pacman/res/Entity/PacmanDeath16.png"));

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 8;
        solidArea.width = 24;
        solidArea.height = 24;
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

    public void update(int key, Constants.Cell[][] map) {
        boolean[] wall = new boolean[4];

        wall[0] = GameController.mapCollision(false, false, position.x + Constants.PACMAN_SPEED, position.y, map);
        wall[1] = GameController.mapCollision(false, false, position.x, position.y - Constants.PACMAN_SPEED, map);
        wall[2] = GameController.mapCollision(false, false, position.x - Constants.PACMAN_SPEED, position.y, map);
        wall[3] = GameController.mapCollision(false, false, position.x, position.y + Constants.PACMAN_SPEED, map);


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

        if (-Constants.CELL_SIZE >= position.x) {
            position.x = Constants.CELL_SIZE * Constants.MAP_WIDTH - Constants.PACMAN_SPEED;
        }
        else if (Constants.CELL_SIZE * Constants.MAP_WIDTH <= position.x) {
            position.x = Constants.PACMAN_SPEED - Constants.CELL_SIZE;
        }

    }

}
