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

    boolean isWin;

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
        long lastTime = System.nanoTime();
        long currentTime;

        // count fps
        long fpsTimer = 0;
        int drawCount = 0;

        long energizerTimer = 0;

        while (!isWin) {
            // draw 60fps
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;

            fpsTimer += currentTime - lastTime;
            energizerTimer += currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1) {
                // delta >= 1 mean past 0.0166 sec
                // 1. update pacman position
                pacman.update(view.getKey(), map);

                // get pacman position in map
                int x = (int) Math.round(pacman.getPosition().x / (double) (Constants.CELL_SIZE));
                int y = (int) Math.round(pacman.getPosition().y / (double) (Constants.CELL_SIZE));

                // kiem tra xem co o trong map khong...
                if (0 < x && Constants.MAP_HEIGHT > y && Constants.MAP_WIDTH > x) {
                    // 2. check eat energizer
                    pacman.updateEnergizer(map.getMapItem(x, y));
                    // neu pacman an energizer thi ghost bi frightened
                    ghostManager.updateFrightened(pacman);
                    // 3. update map
                    mapUpdate(x, y);
                }

                // 4. update ghost
                ghostManager.update(map, pacman);
                // 5.

                // 6. update view
                view.update(pacman, ghostManager, map);

                // 7. check kill pacman
                if(ghostManager.isKillPacman()) {
                    pacman.reset(false);
                    ghostManager.reset(false);
                }

                // 8. Check win
                isWin = isWin();

                // reset delta
                delta--;
                // count frame
                drawCount++;
            }

            if (energizerTimer >= 300000000) {
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
                drawCount = 0;
                fpsTimer = 0;
            }
        }
    }

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
        view.update(pacman, ghostManager, map);

        // move to right pos in map
        pacman.reset(true);

        // move to right pos in map
        ghostManager.reset(true);
    }

    public boolean isWin() {
        return map.isClear();
    }

    public void mapUpdate(int x, int y) {
        if (Constants.Cell.Energizer == map.getMapItem(y, x)) {
            map.setMapItem(x, y, Constants.Cell.Empty);
            return;
        }
        map.setMapItem(x, y, Constants.Cell.Empty);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        isWin = false;
    }
}
