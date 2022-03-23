package com.pacman.entity;

import com.pacman.controller.GhostManager;
import com.pacman.utils.BufferedImageLoader;
import com.pacman.utils.Constants;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Ghost {
    private final GhostManager.GhostType type;
    private int direction;
    private boolean iUseDoor;
    private boolean isScatter;
    private Point position;
    private Point target;
    private final Point home;
    private final Point homeExit;

    // 0 la khong bi
    // 1 la bi
    // 2 la bi an
    private int frightenedMode;

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
        animationTimer = (animationTimer + 1) % (Constants.GHOST_ANIMATION_SPEED * Constants.GHOST_ANIMATION_FRAMES);

        if (frightenedMode == 0) {
            g2d.drawImage(ghostSprite.grabImage(i, frame), position.x, position.y, null);      //body
            g2d.drawImage(ghostSprite.grabImage(5, direction), position.x, position.y, null); // eyes
            return;
        }
        if (frightenedMode == 2) {
            g2d.drawImage(ghostSprite.grabImage(6, direction), position.x, position.y, null); // eyes
            return;
        }
        // normal
        g2d.drawImage(ghostSprite.grabImage(4, frame), position.x, position.y, null);  //body
        g2d.drawImage(ghostSprite.grabImage(5, 4), position.x, position.y, null); // eyes

    }

    public void setPosition(int x, int y) {
        position.setLocation(x, y);
    }

    public Point getPosition() {
        return position;
    }

    public boolean pacmanCollision(Point pacmanPosition) {
        if (position.x > pacmanPosition.x - Constants.CELL_SIZE && position.x < pacmanPosition.x + Constants.CELL_SIZE) {
            if (position.y > pacmanPosition.y - Constants.CELL_SIZE && position.y < pacmanPosition.y + Constants.CELL_SIZE) {
                return true;
            }
        }
        return false;
    }

    public void updateFrightened(Pacman pacman) {
        if (0 == frightenedMode && pacman.getEnergizerTimer() == Constants.ENERGIZER_DURATION / Constants.FPS) {
            frightenedMode = 1;
            direction = (2 + direction) % 4;
        } else if (0 == pacman.getEnergizerTimer() && frightenedMode == 1) {
            fixGrid();
            frightenedMode = 0;
        }
    }

    public void fixGrid() {
        if (position.x % 2 != 0 || position.y % 2 != 0) {
            switch (direction) {
                case 0: //RIGHT
                    position.x += 1;
                    break;
                case 1: //UP
                    position.y -= 1;
                    break;
                case 2: //LEFT
                    position.x -= 1;
                    break;
                case 3: //DOWN
                    position.y += 1;
            }
        }
    }

    public void update(Map map, Pacman pacman, Point redGhostPosition) {
        int pacmanDirection = pacman.getDirection();
        Point pacmanPos = pacman.getPosition(); // TODO

        boolean canMove = false; // co di duoc khong ?
        int availableWay = 0;    // huong dang sau khong phai la huong di duoc

        int speed = Constants.GHOST_SPEED;
        if (1 == frightenedMode) {
            speed = Constants.GHOST_FRIGHTENED_SPEED;
        }
        // outside grid
        if (2 == frightenedMode && 0 == position.x % Constants.GHOST_ESCAPE_SPEED && 0 == position.y % Constants.GHOST_ESCAPE_SPEED) {
            speed = Constants.GHOST_ESCAPE_SPEED;
        }

        this.updateTarget(pacmanPos, pacmanDirection, redGhostPosition);

        // check 4 ben xung quanh co la tuong khong
        boolean[] wall = new boolean[4];

        wall[0] = map.mapCollision(iUseDoor, position.x + speed, position.y); // right
        wall[1] = map.mapCollision(iUseDoor, position.x, position.y - speed); // up
        wall[2] = map.mapCollision(iUseDoor, position.x - speed, position.y); // left
        wall[3] = map.mapCollision(iUseDoor, position.x, position.y + speed); // down

        if (frightenedMode != 1) { // not frightened mode
            int optimalDirection = 3 + 1; // nam ngoai khoang di chuyen duoc
            canMove = true;

            for (int a = 0; a < 4; a++) {
                if (a == (2 + this.direction) % 4) { // ghost khong quay dau
                    continue;
                } else if (!wall[a]) {
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
            } else {
                if (4 == optimalDirection) { // truong hop o home
                    direction = (2 + direction) % 4;
                } else {
                    direction = optimalDirection;
                }
            }
        } else {
            canMove = true;
            int ranNum = ThreadLocalRandom.current().nextInt(0, 4);
            for (int a = 0; a < 4; a++) {
                if (a == (2 + this.direction) % 4) { // ghost khong quay dau
                    continue;
                } else if (!wall[a]) {
                    availableWay++;
                }
            }

            if (0 < availableWay) {
                while (wall[ranNum] || ranNum == (2 + direction) % 4) {
                    ranNum = ThreadLocalRandom.current().nextInt(0, 4);
                }
                direction = ranNum;
            } else {
                direction = (2 + direction) % 4;
            }
        }

        // check direction
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

        if (pacmanCollision(pacmanPos)) {
            if (0 == frightenedMode) {
                // pacman set dead
            } else {
                iUseDoor = true;
                fixGrid();
                frightenedMode = 2;
                target.setLocation(home);
            }
        }
    }

    public void updateTarget(Point pacmanPos, int pacmanDirection, Point redGhostPosition) {
        if (iUseDoor) {
            if (position.equals(target)) {
                if (homeExit.equals(target)) {// da di den exit
                    iUseDoor = false; // khong ve duoc nha nua..
                }
                else if (home.equals(target)) {
                    frightenedMode = 0;
                    target.setLocation(homeExit);
                }
            }
        }

        if (!iUseDoor) { // khong dung else neu khong se co xu huong di sang ben phai vi khong update target kip..
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
                        } else {
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
        frightenedMode = 0;

        this.home.setLocation(home);
        this.homeExit.setLocation(homeExit);
        target.setLocation(this.homeExit);
    }

    public double getTargetDistance(int direction) {
        int x = position.x;
        int y = position.y;

        // tinh khoang cach tu huong dua vao( diem tiep theo ) den muc tieu
        switch (direction) {
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
