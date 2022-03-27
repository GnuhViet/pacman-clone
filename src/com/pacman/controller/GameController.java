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

    private boolean isWin;
    private boolean isLose;
    private boolean isFinish;
    private int level;

    Thread gameThread;

    ///////
    // Runnable method implements
    //////
    @Override
    public void run() {
        // 1 sec = 1000000000 nanosec
        // draw 60fps
        double drawInterval = 1000000000/Constants.FPS; // drawn 1 frame in 0.0166sec
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
        int gameTimer = 0;

        countDownReady();
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
                if (0 < x && Constants.MAP_HEIGHT > y && Constants.MAP_WIDTH > x) {
                    // 2. check eat energizer
                    pacman.updateCollectItem(map.getMapItem(x, y));
                    // neu pacman an energizer thi ghost bi frightened
                    ghostManager.updateFrightened(pacman);
                    // 3. update map
                    map.mapUpdate(x, y);
                }

                // 4. update ghost
                ghostManager.update(map, pacman, gameTimer);

                // 5. update view
                view.update(pacman, ghostManager, map, level);

                // 6. check kill pacman
                if(ghostManager.isKillPacman()) {
                    pacman.reset(false);
                    ghostManager.reset(false);
                    view.resetReadyTimer(); // 2 second

                    gameTimer = 0; // FIXME Out of range
                    pacman.decreaseLive();
                }

                // 7. Check win
                isWin = isWin();
                if (isWin) {
                    if (level < 8) {
                        level += 1;
                        this.nextLevel();
                    }
                    else {
                        isFinish = true;
                    }
                }

                // 8. Check lose
                if (pacman.getLive() == 0) {
                    pacman.setAlive(false);
                    view.setReady(true);
                    view.setLose(true);
                    break; // out vong lap
                    // draw death
                }

                // reset delta
                delta--;
                // count frame
                drawCount++;
            }

            if (!view.getReady()) {
                countDownReady();
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
                //System.out.println("Timer: " + pacman.getEnergizerTimer());
                if (pacman.getEnergizerTimer() > 0) {
                    pacman.reduceEnergizerTimer();
                }
                //System.out.println("FPS:" + drawCount);
                ghostManager.phaseUpdate();
                gameTimer += 1;
                drawCount = 0;
                fpsTimer = 0;
            }
        }

        // TODO ve death animation roi doi panel
        lastTime = System.nanoTime();
        while (true) {
            // draw 60fps
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            fpsTimer += currentTime - lastTime; // thoi gian reset khac nhau, khong the gan
            energizerTimer += currentTime - lastTime;
            blinkTimer += currentTime - lastTime;
            lastTime = currentTime;
            if (delta >= 1) {
                pacman.updateDeath();
                delta--;
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
        map.setMap(data.getMap(pacman, ghostManager));

        // update view
        view.update(pacman, ghostManager, map, level);

        // move to right pos in map
        pacman.reset(true);

        // move to right pos in map
        ghostManager.reset(true);
    }

    public void nextLevel() { // TODO... update me, player, khong reset diem,..., ve them man choi
        // set map
        map.setMap(data.getMap(pacman, ghostManager));

        // update view
        view.update(pacman, ghostManager, map, level);

        // move to right pos in map
        pacman.reset(false);

        // move to right pos in map
        ghostManager.reset(false);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        isWin = false;
        isLose = false;
        isFinish = false;
        level = 1;
        gameThread.start();
    }

    public void countDownReady() {
        long lastTime = System.nanoTime();
        long currentTime;
        long readyTimer = 0; // dem ready time
        long timer = 0; // dem nguoc..

        while (view.getReadyTimer() > 0) {
            currentTime = System.nanoTime();
            readyTimer += (currentTime - lastTime);
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (timer >= 1000000000) {
                view.decreaseTimer();
                view.update(pacman, ghostManager, map, level);
                timer = 0;
            }
            if (readyTimer >= 1000000000L * Constants.READY_TIME) {
                view.setReady(true);
                break;
            }
        }
    }

    public boolean isWin() {
        return map.isClear();
    }
}
