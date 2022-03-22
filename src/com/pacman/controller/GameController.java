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

    private int key;
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
        long timer = 0;
        int drawCount = 0;

        while (!isWin) {
            // draw 60fps
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1) {// delta >= 1 mean past 0.0166 sec

                // 1. update pacman position
                pacman.update(view.getKey(), map);

                // get pacman position in map
                int x = (int) Math.round(pacman.getPosition().x / (double) (Constants.CELL_SIZE));
                int y = (int) Math.round(pacman.getPosition().y / (double) (Constants.CELL_SIZE));

                // kiem tra xem co o trong map khong...
                if (0 < x && Constants.MAP_HEIGHT > y && Constants.MAP_WIDTH > x) {
                    // 2. check eat energizer
                    pacman.updateEnergizer(map.getMapItem(x, y));
                    // 3. update map
                    mapUpdate(x, y);
                }

                // 4. update ghost
                ghostManager.update(map, pacman);
                // 5. update view
                view.update(pacman, ghostManager, map);
                // 6. Check win
                isWin = isWin();

                // reset delta
                delta--;
                // count frame
                drawCount++;
            }

            // print fps and update phase
            if (timer >= 1000000000) {
                //System.out.println("Timer: " + pacman.getEnergizerTimer());
                if (pacman.getEnergizerTimer() > 0) {
                    pacman.reduceEnergizerTimer();
                }
                //System.out.println("FPS:" + drawCount);
                ghostManager.phaseUpdate();
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public GameController(GameView view) throws IOException {
        data = new FileUtils();
        pacman = new Pacman();
        ghostManager = new GhostManager();
        map = new Map(data.getMap(pacman, ghostManager));
        this.view = view;

        initGame();
    }

    public void initGame() {
        // update view
        view.update(pacman, ghostManager, map);

        // move to right pos in map
        int pacmanX = (pacman.getPosition().x * Constants.CELL_SIZE);
        int pacmanY = (pacman.getPosition().y * Constants.CELL_SIZE);
        pacman.setPosition(pacmanX, pacmanY);

        // move to right pos in map
        ghostManager.initPos();
        ghostManager.reset();
    }

    public boolean isWin() {
        return map.isClear();
    }

    public void mapUpdate(int x, int y) {
        if (Constants.Cell.Energizer == map.getMapItem(y, x)) {
            map.setMap(x, y, Constants.Cell.Empty);
            return;
        }
        map.setMap(x, y, Constants.Cell.Empty);
    }


    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        isWin = false;
    }
}
