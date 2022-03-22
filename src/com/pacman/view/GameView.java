package com.pacman.view;

import com.pacman.controller.GameController;
import com.pacman.controller.GhostManager;
import com.pacman.entity.Pacman;
import com.pacman.entity.SpriteSheet;
import com.pacman.utils.BufferedImageLoader;
import com.pacman.utils.Constants;
import com.pacman.utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameView extends JPanel implements Runnable, KeyListener{
    private FileUtils data; // TODO... chinh sua lai
    private SpriteSheet mapSprite;;
    private Constants.Cell[][] mapInput;
    private Pacman pacman;
    private GhostManager ghostManager;

    Thread gameThread;

    boolean off = false;
    long offTime;
    long onTime;

    private int key;

    boolean isWin;

    ///////
    // JPanel methods override
    ///////
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        try {
            this.drawMap(g2d);
            pacman.draw(g2d);
            ghostManager.draw(g2d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2d.dispose();
    }

    ///////
    // Runnable method implements
    //////
    /*
     *  BAT DAU GAME
     */
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

            if (delta >= 1) {
                // delta >= 1 mean past 0.0166 sec
                // 1. update pacman position
                pacman.update(key, mapInput);

                // 2. check eat energizer
                int x = (int) Math.round(pacman.getPosition().x / (double) (Constants.CELL_SIZE));
                int y = (int) Math.round(pacman.getPosition().y / (double) (Constants.CELL_SIZE));
                pacman.updateEnergizer(mapInput[y][x]);

                // 3. update map
                if (Constants.MAP_HEIGHT > y && Constants.MAP_WIDTH > x) {
                    mapInput[y][x] = GameController.mapUpdate(x, y, mapInput);
                }

                // 3. update ghost
                ghostManager.update(mapInput, pacman);

                // 4. redraw the screen
                this.repaint();

                // 5. Check win
                isWin = GameController.isWin(mapInput);
                // reset delta
                delta--;
                // count frame
                drawCount++;
            }

            //for blinking energizer
            if (off) {
                offTime += timer;
            } else {
                onTime += timer;
            }

            if (onTime  >= 800000000) {
                off = true;
                onTime = 0;
            }
            if (offTime >= 800000000) {
                off = false;
                offTime = 0;
            }

            // print fps and update phase
            if (timer >= 1000000000) {
                System.out.println("Timer: " + pacman.getEnergizerTimer());
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

    /*
     * KET THUC GAME
     */
    public void gameStop() {

    }

    ///////
    // KeyListener method implements
    //////
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        key = e.getKeyCode();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    ////////////////////////
    /////// GameView methods
    ////////////////////////
    public GameView() throws IOException {
        data = new FileUtils();
        pacman = new Pacman();
        ghostManager = new GhostManager();
        initGame();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        isWin = false;
    }

    private void initGame() throws IOException {
        this.setOpaque(true);
        this.setBackground(Color.BLACK);

        mapSprite = new SpriteSheet(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Entity\\Map32.png"));
        mapInput = data.getMap(pacman, ghostManager); //TODO .. chinh sua lai

        // move to right pos in map
        int pacmanX = (pacman.getPosition().x * Constants.CELL_SIZE);
        int pacmanY = (pacman.getPosition().y * Constants.CELL_SIZE);
        pacman.setPosition(pacmanX, pacmanY);

        // move to right pos in map
        ghostManager.initPos();
        ghostManager.reset();
    }

    private void drawMap(Graphics2D g2d) throws IOException {

        for (int a = 0; a < Constants.MAP_WIDTH; a++){
            for (int b = 0; b < Constants.MAP_HEIGHT; b++) {

                int xPos = ((b * Constants.CELL_SIZE));
                int yPos = ((a * Constants.CELL_SIZE));

                switch (mapInput[a][b]) {
                    case Door: {
                        g2d.drawImage(mapSprite.grabImage(1,2),  xPos, yPos, null);
                        break;
                    }
                    case Energizer: {
                        if (off) {
                            g2d.drawImage(mapSprite.grabImage(1,3),  xPos, yPos, null);
                            break;
                        }
                        g2d.drawImage(mapSprite.grabImage(1,1),  xPos, yPos, null);
                        break;
                    }
                    case Pellet: {
                        g2d.drawImage(mapSprite.grabImage(1,0),  xPos, yPos, null);
                        break;
                    }
                    case Wall: {

                        int up = 0;         // b la truc x
                        int left = 0;       // a la truc y
                        int down = 0;
                        int right = 0;

                        if (b < Constants.MAP_WIDTH - 1) {
                            if (Constants.Cell.Wall == mapInput[a][b + 1]) {
                                right = 1; // right
                            }
                        }

                        if (a > 0) {
                            if (Constants.Cell.Wall == mapInput[a - 1][b]) {
                                up = 1; // up
                            }
                        }

                        if (a < Constants.MAP_HEIGHT - 1) {
                            if (Constants.Cell.Wall == mapInput[a + 1][b]) {
                                down = 1; // dow
                            }
                        }

                        if (b > 0) {
                            if (Constants.Cell.Wall == mapInput[a][b - 1]) {
                                left = 1; // left
                            }
                        }

                        /////// sprite pattern
                        int pos = down + 2*left + 4*right + 8*up;

                        g2d.drawImage(mapSprite.grabImage(0, pos),  xPos, yPos, null);
                    }
                }
            }
        }
    }

}
