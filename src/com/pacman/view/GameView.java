package com.pacman.view;

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
    private Constants.Cell[][] mapInput; // dua xuong Controller
    private Pacman pacman;

    Thread gameThread;

    private int key;

    // JPanel methods override
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        try {
            this.drawMap(g2d);
            pacman.draw(g2d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2d.dispose();
    }

    // Interface method implements
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
        while (gameThread != null) {
            // draw 60fps
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1) { // delta >= 1 mean past 0.0166 sec
                // 1. update pacman position
                pacman.update(key, mapInput);
                // 2. redraw the screen
                this.repaint();
                // reset delta
                delta--;

                drawCount++;
            }

            if (timer >= 1000000000) {
                //System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        key = e.getKeyCode();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    // GameView methods

    public GameView() throws IOException {
        data = new FileUtils();
        pacman = new Pacman();
        initGame();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void initGame() throws IOException {
        this.setOpaque(true);
        this.setBackground(Color.BLACK);

        mapSprite = new SpriteSheet(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Entity\\Map32.png"));
        mapInput = data.getMap(pacman); //TODO .. chinh sua lai

        // move to right pos in map
        int pacmanX = ((pacman.getPosition().x * Constants.CELL_SIZE) - Constants.CELL_SIZE);
        int pacmanY = ((pacman.getPosition().y * Constants.CELL_SIZE) - Constants.CELL_SIZE);
        pacman.setPosition(pacmanX, pacmanY);
    }

    private void drawMap(Graphics2D g2d) throws IOException {

        for (int a = 0; a < Constants.MAP_WIDTH; a++){
            for (int b = 0; b < Constants.MAP_HEIGHT; b++) {

                int x = b + 1;
                int y = a + 1;

                int xPos = ((x * Constants.CELL_SIZE) - Constants.CELL_SIZE);
                int yPos = ((y * Constants.CELL_SIZE) - Constants.CELL_SIZE);

                switch (mapInput[a][b]) {
                    case Door: {
                        g2d.drawImage(mapSprite.grabImage(1,2),  xPos, yPos, null);
                        break;
                    }
                    case Energizer: {
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

                        int pos = (down + 2 * (left + 2 * (right + 2 * up)));

                        g2d.drawImage(mapSprite.grabImage(0, pos),  xPos, yPos, null);
                    }
                }
            }
        }
    }

}
