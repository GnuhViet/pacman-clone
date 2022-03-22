package com.pacman.entity;

import com.pacman.controller.GameController;
import com.pacman.controller.GhostManager;
import com.pacman.utils.BufferedImageLoader;
import com.pacman.utils.Constants;

import java.awt.*;
import java.io.IOException;

public class Ghost {
    private GhostManager.GhostType type;
    private int direction;
    private boolean iUseDoor;
    private boolean isScatter;
    private Point position;
    private Point target;
    private Point home;
    private Point homeExit;

    //
    private int animationTimer;

    //
    SpriteSheet ghostSprite;

    public Ghost(GhostManager.GhostType type) throws IOException {
        this.type = type;

        position = new Point();
        target = new Point();
        home = new Point();
        homeExit = new Point();

        ghostSprite = new SpriteSheet(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Entity\\Ghost32.png"));
    }

    public void draw(int i, Graphics2D g2d) {
        int frame = (int) Math.floor(animationTimer / Constants.GHOST_ANIMATION_SPEED);

        g2d.drawImage(ghostSprite.grabImage(i, frame), position.x, position.y, null);      //body
        g2d.drawImage(ghostSprite.grabImage(5, direction), position.x, position.y, null); // eyes

        animationTimer = (animationTimer + 1) % (Constants.GHOST_ANIMATION_SPEED * Constants.GHOST_ANIMATION_FRAMES);
    }

    public void setPosition(int x, int y) {
        position.setLocation(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public void update(Constants.Cell[][] map, Point pacmanPos, int pacmanDirection, Point redGhostPosition) {
        boolean canMove = false; // co di duoc khong ?
        int availableWay = 0;    // huong dang sau khong phai la huong di duoc

        int speed = Constants.GHOST_SPEED;

        this.updateTarget(pacmanPos, pacmanDirection, redGhostPosition);

        // check 4 ben xung quanh co la tuong khong
        boolean[] wall = new boolean[4];
        // right
        wall[0] = GameController.mapCollision(iUseDoor, position.x + Constants.GHOST_SPEED, position.y, map);
        // up
        wall[1] = GameController.mapCollision(iUseDoor, position.x, position.y - Constants.GHOST_SPEED, map);
        // left
        wall[2] = GameController.mapCollision(iUseDoor, position.x - Constants.GHOST_SPEED, position.y, map);
        // down
        wall[3] = GameController.mapCollision(iUseDoor, position.x, position.y + Constants.GHOST_SPEED, map);

        if (true) { // not frightened mode
            int optimalDirection = 3 + 1; // nam ngoai khoang di chuyen duoc
            canMove = true;

            for (int a = 0; a < 4; a++) {
                if (a == (2 + this.direction) % 4) { // ghost khong quay dau
                    continue;
                }
                else if (!wall[a]) {
                    if (4 == optimalDirection) {
                        optimalDirection = a;
                    }

                    availableWay++;

                    if (getTargetDistance(a) < getTargetDistance(optimalDirection)) {
                        optimalDirection = a; // huong gan target nhat
                    }
                }
            }
            if (1 < availableWay) {
                direction = optimalDirection;
            }
            else {
                if (4 == optimalDirection) { // truong hop o home
                    direction = (2 + direction) % 4;
                } else {
                    direction = optimalDirection;
                }
            }
        }

        if (canMove) {
            switch (direction) {
                case 0: //RIGHT
                    position.x += speed;
                    break;
                case 1: //UP
                    position.y -= speed;
                    break;
                case 2: //LEFT
                    position.x -= speed;
                    break;
                case 3: //DOWN
                    position.y += speed;
            }
            if (-Constants.CELL_SIZE >= position.x) {
                position.x = Constants.CELL_SIZE * Constants.MAP_WIDTH - speed;
            } else if (Constants.CELL_SIZE * Constants.MAP_WIDTH <= position.x) {
                position.x = speed - Constants.CELL_SIZE;
            }
        }

    }

    public void updateTarget(Point pacmanPos, int pacmanDirection , Point redGhostPosition) {
        if (iUseDoor) {
            if (position.equals(target)) {
                if (homeExit.equals(target)) {// da di den exit
                    iUseDoor = false; // khong ve duoc nha nua..
                }
                // TODO ve den nha thi chuyen mode
            }
        }
        if (!iUseDoor){ // khong dung else neu khong se co xu huong di sang ben phai vi khong update target kip..
            if (isScatter) {
                switch (type) {
                    case RED: {
                        target.x = Constants.CELL_SIZE * (Constants.MAP_WIDTH - 1);
                        target.y = 0;
                        break;
                    }
                    case PINK: {
                        target.x = 0;
                        target.y = 0;
                        break;
                    }
                    case BLUE: {
                        target.x = Constants.CELL_SIZE * (Constants.MAP_WIDTH - 1);
                        target.y = Constants.CELL_SIZE * (Constants.MAP_HEIGHT - 1);
                        break;
                    }
                    case ORANGE: {
                        target.x = 0;
                        target.y = Constants.CELL_SIZE * (Constants.MAP_HEIGHT - 1);
                    }
                }
            } else {    // chase mode....
                switch (type) {
                    case RED: {// ghost Red duoi pacman
                        target.setLocation(pacmanPos);
                        break;
                    }
                    case PINK: {// ghost Pink duoi o thu 4 truoc mat pacman
                        target.setLocation(pacmanPos);
                        switch (pacmanDirection) {
                            case 0:
                                target.x += Constants.CELL_SIZE * Constants.GHOST_PINK_CHASE;
                                break;
                            case 1:
                                target.y -= Constants.CELL_SIZE * Constants.GHOST_PINK_CHASE;
                                break;
                            case 2:
                                target.x -= Constants.CELL_SIZE * Constants.GHOST_PINK_CHASE;
                                break;
                            case 3:
                                target.y += Constants.CELL_SIZE * Constants.GHOST_PINK_CHASE;
                        }
                        break;
                    }
                    case BLUE: {
                        target.setLocation(pacmanPos);
                        switch (pacmanDirection) { // lay 2 cell truoc mat pacman
                            case 0:
                                target.x += Constants.CELL_SIZE * Constants.GHOST_BLUE_CHASE;
                                break;
                            case 1:
                                target.y -= Constants.CELL_SIZE * Constants.GHOST_BLUE_CHASE;
                                break;
                            case 2:
                                target.x -= Constants.CELL_SIZE * Constants.GHOST_BLUE_CHASE;
                                break;
                            case 3:
                                target.y += Constants.CELL_SIZE * Constants.GHOST_BLUE_CHASE;
                        }

                        target.x += target.x - redGhostPosition.x;
                        target.y += target.y - redGhostPosition.y;
                        break;
                    }
                    case ORANGE: {
                        int ghost3chase = Constants.CELL_SIZE * Constants.GHOST_ORANGE_CHASE;
                        if (ghost3chase <= Math.sqrt(Math.pow(position.x - pacmanPos.x, 2) + Math.pow(position.y - pacmanPos.y, 2))) {
                            target.setLocation(pacmanPos);
                        }
                        else {
                            target.setLocation(0, Constants.CELL_SIZE * (Constants.MAP_HEIGHT - 1));
                        }
                    }
                }
            }
        }
    }

    public void reset(Point home, Point homeExit) {
        isScatter = true;
        iUseDoor = type != GhostManager.GhostType.RED; // red khong the di vao cua'
        animationTimer = 0;

        this.home.setLocation(home);
        this.homeExit.setLocation(homeExit);

        target.setLocation(this.homeExit);
    }

    public double getTargetDistance(int a) {
        int x = position.x;
        int y = position.y;

        // tinh khoang cach tu huong dua vao( diem tiep theo ) den muc tieu
        switch (a) {
            case 0: {
                x += Constants.GHOST_SPEED;
                break;
            }
            case 1: {
                y -= Constants.GHOST_SPEED;
                break;
            }
            case 2: {
                x -= Constants.GHOST_SPEED;
                break;
            }
            case 3: {
                y += Constants.GHOST_SPEED;
            }
        }
        //... pythagoras
        return Math.sqrt(Math.pow(x - target.x, 2) + Math.pow(y - target.y, 2));
    }

    public void switchMode() {
        isScatter = !isScatter;
    }
}
