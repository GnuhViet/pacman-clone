package com.pacman.entity;

import com.pacman.ultis.BufferedImageLoader;
import com.pacman.ultis.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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

    public Pacman() throws IOException {
        position = new Point();
        direction = 0;
        pacmanSprite = new SpriteSheet(BufferedImageLoader.loadImage("src/com/pacman/res/Entity/Pacman16.png"));
        pacmanDeadSprite = new SpriteSheet(BufferedImageLoader.loadImage("src/com/pacman/res/Entity/PacmanDeath16.png"));
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
        int x = position.x + 1;
        int y = position.y + 1;

        int xPos = (((x * Constants.CELL_SIZE) - Constants.CELL_SIZE) * Constants.SCREEN_RESIZE);
        int yPos = (Constants.CELL_SIZE * 2 + ((y * Constants.CELL_SIZE) - Constants.CELL_SIZE) * Constants.SCREEN_RESIZE);

        int frame = animationTimer / Constants.PACMAN_ANIMATION_SPEED;

        g2d.drawImage(pacmanSprite.grabImage(0,0),xPos , yPos, null);

    }

    public void setPosition(int x, int y) {
        position.setLocation(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public void update() {
        switch (direction) {
            case 0:
                position.x += Constants.PACMAN_SPEED - 1;
                break;
            case 1:
                position.y -= Constants.PACMAN_SPEED - 1;
                break;
            case 2:
                position.x -= Constants.PACMAN_SPEED - 1;
                break;
            case 3:
                position.y += Constants.PACMAN_SPEED - 1;
        }

        if (-Constants.CELL_SIZE >= position.x)
        {
            position.x = Constants.CELL_SIZE * Constants.MAP_WIDTH - Constants.PACMAN_SPEED;
        }
        else if (Constants.CELL_SIZE * Constants.MAP_WIDTH <= position.x)
        {
            position.x = Constants.PACMAN_SPEED - Constants.CELL_SIZE;
        }
    }


    public void keyPressed(int key) {
        if (key == KeyEvent.VK_RIGHT) {
            direction = 0;
        }

        if (key == KeyEvent.VK_UP) {
            direction = 1;
        }

        if (key == KeyEvent.VK_LEFT) {
            direction = 2;
        }

        if (key == KeyEvent.VK_DOWN) {
            direction = 3;
        }
    }

}
