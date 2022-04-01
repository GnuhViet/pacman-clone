package com.pacman.entity;

import com.pacman.controller.GhostManager;
import com.pacman.utils.BufferedImageLoader;
import com.pacman.utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Pacman {
    private int direction;
    private Point position;

    private int live;
    private boolean isAlive;
    private int startX;
    private int startY;

    // timer
    private int animationTimer;
    private int energizerTimer;
    private boolean animationOver;

    // ......
    private SpriteSheet pacmanSprite;
    private SpriteSheet pacmanDeadSprite;

    private int score;
    private int bonus;
    private boolean isDrawBonus;
    private GhostManager.GhostType ghostKilled;

    /////////////
    /// Methods
    ////////////

    public Pacman() throws IOException {
        position = new Point();
        pacmanSprite = new SpriteSheet(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Entity\\Pacman32.png"));
        pacmanDeadSprite = new SpriteSheet(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Entity\\PacmanDeath32.png"));
    }

    public void reset(boolean isNewGame) {
        if (isNewGame) {
            startX = (position.x * Constants.CELL_SIZE);
            startY = (position.y * Constants.CELL_SIZE) + Constants.SCREEN_TOP_MARGIN;
            live = Constants.PACMAN_START_LIVES;
            score = 0;
            bonus = 0;
            animationOver = false;
        }

        if (live == 0) {
            // game over..
        }

        direction = 0;
        position.setLocation(startX, startY);
        isAlive = true;
        animationTimer = 0;
        isDrawBonus = false;
    }

    ///////////////
    /// GETTER & SETTER
    //////////////
    public int getLive() {
        return live;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
        animationTimer = 0; // reset animation timer de ve tu dau
    }

    public boolean isAlive() {
        return isAlive;
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

    public int getEnergizerTimer() {
        return energizerTimer;
    }

    public int getScore() {
        return score;
    }

    public GhostManager.GhostType getGhostKilled() {
        return ghostKilled;
    }

    public void setIsDrawBonus(boolean isDrawBonus) {
        this.isDrawBonus = isDrawBonus;
    }

    public boolean isDrawBonus() {
        return isDrawBonus;
    }

    public int getBonus() {
        return bonus;
    }

    public boolean isAnimationOver() {
        return animationOver;
    }

    public void resetAnimationOver() {
        animationOver = false;
    }

    ///////////
    protected void setDirection(int direction) {
        this.direction = direction;
    }

    ///////////////
    /// UPDATE
    //////////////

    public void decreaseLive() {
        live--;
    }

    public void update(int key, Map map) {
        boolean[] wall = new boolean[4];

        // check 4 ben xung quanh co la tuong khong
        // right
        wall[0] = map.mapCollision(false, position.x + Constants.PACMAN_SPEED, position.y);
        // up
        wall[1] = map.mapCollision(false, position.x, position.y - Constants.PACMAN_SPEED);
        // left
        wall[2] = map.mapCollision(false, position.x - Constants.PACMAN_SPEED, position.y);
        // down
        wall[3] = map.mapCollision(false, position.x, position.y + Constants.PACMAN_SPEED);

        if (key == KeyEvent.VK_RIGHT) {
            if (!wall[0]) /// neu co tuong thi khong di duoc
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


        // portal... (x)
        if (-Constants.CELL_SIZE >= position.x) { // left
            position.x = Constants.CELL_SIZE * Constants.MAP_WIDTH - Constants.PACMAN_SPEED;
        } else if (Constants.CELL_SIZE * Constants.MAP_WIDTH <= position.x) { // right
            position.x = -Constants.CELL_SIZE + Constants.PACMAN_SPEED;
        }

        if (Constants.SCREEN_TOP_MARGIN >= position.y) { // top
            position.y = Constants.CELL_SIZE * Constants.MAP_HEIGHT - Constants.PACMAN_SPEED;
        } else if (Constants.CELL_SIZE * Constants.MAP_HEIGHT <= position.y) { // bottom
            position.y = Constants.SCREEN_TOP_MARGIN + Constants.PACMAN_SPEED;
        }
    }

    public void updateCollectItem(Constants.Cell mapItem) {
        if (Constants.Cell.Energizer == mapItem) {
            energizerTimer = Constants.ENERGIZER_DURATION;
            score += Constants.ENERGIZER_SCORE;
            bonus = 0;
        }

        if (Constants.Cell.Pellet == mapItem) {
            score += Constants.PELLET_SCORE;
        }
    }

    public void impactGhostWhenEnergizer(GhostManager.GhostType ghostKilled) {
        bonus += Constants.GHOST_SCORE;
        score += bonus;
        isDrawBonus = true;
        this.ghostKilled = ghostKilled;
    }

    public void reduceEnergizerTimer() {
        energizerTimer = Math.max(0, energizerTimer - 1);
        if (energizerTimer == 0) {
            bonus = 0; // reset bonus
        }
    }

    public void draw(Graphics2D g2d) {

        int frame = (int) Math.floor(animationTimer / (double) Constants.PACMAN_ANIMATION_SPEED);
        if (isAlive) {
            g2d.drawImage(pacmanSprite.grabImage(direction, frame), position.x, position.y, null);
            animationTimer = (animationTimer + 1) % (Constants.PACMAN_ANIMATION_SPEED * Constants.PACMAN_ANIMATION_FRAMES);
        }
        else {
            if (animationTimer < Constants.PACMAN_DEATH_FRAMES * Constants.PACMAN_ANIMATION_SPEED) {
                animationTimer++;
                g2d.drawImage(pacmanDeadSprite.grabImage(0, frame), position.x, position.y, null);
            }
            else {
                animationOver = true;
            }
        }
    }
}
