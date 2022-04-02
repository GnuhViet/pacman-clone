package com.pacman.controller;

import com.pacman.entity.Map;
import com.pacman.entity.Pacman;
import com.pacman.utils.Constants;
import com.pacman.utils.FileUtils;
import com.pacman.view.GameView;

import java.io.IOException;

public class GameController implements Runnable{
    private FileUtils data;
    private Pacman pacman;
    private GhostManager ghostManager;
    private Map map;
    private GameView view;

    private boolean isWinLevel;
    private boolean isFinish;
    private int level;

    Thread gameThread; //https://stackoverflow.com/questions/27593900

    ///////
    // Runnable method implements
    //////
    @Override
    public void run() {
        // 1 sec = 1000000000 nanosec
        // draw 60fps
        double drawInterval = 1000000000/(double)Constants.FPS; // drawn 1 frame in 0.0166sec
        double delta = 0;
        long lastTime = 0;
        long currentTime;
        // count fps
        long fpsTimer = 0;
        int drawCount = 0;
        // hieu ung nhap nhay
        long energizerTimer = 0; // energizer nhap nhay
        long blinkTimer = 0; // ghost nhap nhay khi gan het thoi gian frightened
        // thoi gian game, theo sec
        int gameTimerFirst2Sec = 0;

        countDownReady(false);
        lastTime = System.nanoTime();

        while (!isFinish) {
            // draw 60fps
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;

            fpsTimer += currentTime - lastTime; // thoi gian reset khac nhau, khong the gan
            energizerTimer += currentTime - lastTime;
            blinkTimer += currentTime - lastTime;
            lastTime = currentTime;

            // delta >= 1 mean past 0.0166 sec
            if (delta >= 1) {
                // 1. update pacman position
                pacman.update(view.getKey(), map);

                // get pacman position in map
                int x = (int) Math.round(pacman.getPosition().x / (double) (Constants.CELL_SIZE));
                int y = (int) Math.round(pacman.getPosition().y / (double) (Constants.CELL_SIZE)) - Constants.SCREEN_TOP_MARGIN / Constants.CELL_SIZE;

                // kiem tra xem co o trong map khong...
                if (0 <= x && Constants.MAP_WIDTH > x && 0 <= y && Constants.MAP_HEIGHT > y) {
                    // 2. check collectItem
                    pacman.updateCollectItem(map.getMapItem(x, y));
                    // neu pacman an energizer thi ghost bi frightened
                    ghostManager.updateFrightened(pacman);
                    // 3. update map
                    map.mapUpdate(x, y);
                }

                // 4. update ghost
                ghostManager.update(map, pacman, gameTimerFirst2Sec);

                // 5. update view
                view.update(pacman, ghostManager, map, level);

                // 6. check kill pacman
                if(ghostManager.isKillPacman()) {
                    pacman.reset(false);
                    ghostManager.reset(false);
                    gameTimerFirst2Sec = 0;
                    pacman.decreaseLive();
                    if (pacman.getLive() != 0) {
                        view.resetReadyTimer(); // 2 second
                    }
                }

                // 7. Check lose
                if (pacman.getLive() == 0) {
                    pacman.setAlive(false);
                    // draw death animation
                    while (!pacman.isAnimationOver()) {
                        try {
                            Thread.sleep(40);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        view.update(pacman, ghostManager, map, level);
                    }
                    view.setEnd(false);
                    break; // out vong lap
                }

                // 8. Check win
                isWinLevel = isWinLevel();
                if (isWinLevel) {
                    if (level < 8) {
                        level += 1;
                        view.updateLoadingScreen();
                        view.resetReadyTimer(); // 2 second
                        this.nextLevel();
                        lastTime = System.nanoTime();
                    }
                    else {
                        isFinish = true;
                        view.setEnd(true);
                    }
                }

                // reset delta
                delta--;
                // count frame
                drawCount++;
            }

            // khong co gi de giai thich
            if (!view.getReady()) {
                countDownReady(true);
                lastTime = System.nanoTime();
            }

            // ghost nhap nhay
            if (blinkTimer >= 100000000) {
                if (pacman.getEnergizerTimer() <= 3 && pacman.getEnergizerTimer() > 0) {
                    ghostManager.makeBlinkEffect();
                }
                blinkTimer = 0;
            }

            // energizer nhap nhay
            if (energizerTimer >= 200000000) {
                map.energizerSwitch();
                energizerTimer = 0;
            }

            // print fps and update phase
            if (fpsTimer >= 1000000000) {
                if (pacman.getEnergizerTimer() > 0) {
                    pacman.reduceEnergizerTimer();
                }
                //System.out.println("FPS:" + drawCount);
                ghostManager.phaseUpdate();
                if (gameTimerFirst2Sec <= 3){
                    gameTimerFirst2Sec += 1;
                }
                drawCount = 0;
                fpsTimer = 0;
            }

            // an duoc ghost thi draw diem bonus trong 1 sec
            if (pacman.isDrawBonus()) {
                view.update(pacman, ghostManager, map, level);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pacman.setIsDrawBonus(false);
                lastTime = System.nanoTime();
            }
        }
    }

    ///////////////
    // Controller methods
    ///////////////
    public GameController(GameView view) throws IOException {
        data = new FileUtils();
        pacman = new Pacman();
        ghostManager = new GhostManager();
        map = new Map();
        this.view = view;
        initGame();
    }

    public void initGame() {
        // set map
        map.setMap(data.loadMap(pacman, ghostManager, level));

        // update view
        view.update(pacman, ghostManager, map, level);

        // move to right pos in map
        pacman.reset(true);

        // move to right pos in map
        ghostManager.reset(true);
    }


    //reset lai
    public void nextLevel() {
        // set map
        map.setMap(data.loadMap(pacman, ghostManager, level));

        // update view
        view.update(pacman, ghostManager, map, level);

        // move to right pos in map
        pacman.reset(false);

        // move to right pos in map
        ghostManager.reset(false);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        isWinLevel = false;
        isFinish = false;
        level = 1;
        gameThread.start();
    }

    public void countDownReady(boolean isMapBlink) {
        long lastTime = System.nanoTime();
        long currentTime;
        long readyTimer = 0; // dem ready time
        long numberTimer = 0; // dem nguoc..
        long mapColorTimer = 0;

        while (view.getReadyTimer() > 0) {
            currentTime = System.nanoTime();
            readyTimer += (currentTime - lastTime);
            numberTimer += (currentTime - lastTime);
            mapColorTimer += (currentTime - lastTime);
            lastTime = currentTime;

            if (isMapBlink && mapColorTimer >= 500000000) {
                map.switchColor();
                view.update(pacman, ghostManager, map, level);
                mapColorTimer = 0;
            }

            if (numberTimer >= 1000000000) {
                view.decreaseTimer();
                view.update(pacman, ghostManager, map, level);
                numberTimer = 0;
            }
            if (readyTimer >= 1000000000L * Constants.READY_TIME) {
                view.setReady(true);
                break;
            }
        }
        map.resetColor();
    }

    public boolean isWinLevel() {
        return map.isClear();
    }
}
