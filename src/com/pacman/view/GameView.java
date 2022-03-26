package com.pacman.view;

import com.pacman.controller.GhostManager;
import com.pacman.entity.Map;
import com.pacman.entity.Pacman;
import com.pacman.entity.PixelNumber;
import com.pacman.entity.SpriteSheet;
import com.pacman.utils.BufferedImageLoader;
import com.pacman.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameView extends JPanel implements KeyListener {
    private Map map;
    private Pacman pacman;
    private GhostManager ghostManager;
    private PixelNumber pixelNumber;
    private SpriteSheet item;

    private int key;
    private int readyTimer;

    private boolean isReady;
    private boolean isLose;
    private boolean switchColor;

    private static final int dX = Constants.CELL_SIZE * Constants.MAP_WIDTH;
    private static final int dY = Constants.CELL_SIZE * Constants.MAP_HEIGHT + Constants.SCREEN_TOP_MARGIN * 2;

    ///////
    // JPanel methods override
    ///////
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        try {
            this.drawScore(g2d);
            this.drawLives(g2d);
            map.drawMap(g2d);
            pacman.draw(g2d);
            ghostManager.draw(g2d);
            if (!isReady) {
                this.drawReady(g2d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2d.dispose();
    }

    ///////
    // KeyListener method implements
    //////
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        key = e.getKeyCode();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    ////////////////////////
    /////// GameView methods
    ////////////////////////
    public GameView() throws IOException {
        initGame();
    }

    private void initGame() throws IOException {
        this.setOpaque(true);
        this.setBackground(Color.BLACK);
        isLose = false;
        switchColor = true;
        isReady = false;
        resetReadyTimer();
        // load sprite
        pixelNumber = new PixelNumber();
        item = new SpriteSheet(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Item.png"));
    }

    public void resetReadyTimer() {
        isReady = false;
        readyTimer = Constants.READY_TIME;
    }

    public void update(Pacman pacman, GhostManager ghostManager, Map map) {
        this.pacman = pacman;
        this.ghostManager = ghostManager;
        this.map = map;
        this.repaint();
    }

    public int getReadyTimer() {
        return readyTimer;
    }
    public int getKey() {
        return key;
    }

    public boolean getReady() {
        return isReady;
    }

    public void setLose(boolean lose) {
        this.isLose = lose;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public void decreaseTimer() {
        readyTimer -=1;
    }

    public void switchColor() {
        switchColor = !switchColor;
    }

    /////////////
    /// draws methods
    ////////////
    private void drawScore(Graphics2D g2d) throws IOException {
        g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Score32.png"), 0, 0, null);
        pixelNumber.draw(g2d, pacman.getScore(),86, 0, 16);
    }

    private void drawLives(Graphics2D g2d) {
        int n = pacman.getLive();
        int dX = 100;
        int dY = Constants.SCREEN_HEIGHT - Constants.SCREEN_BOTTOM_MARGIN + Constants.CELL_SIZE / 3;
        for (int i = 0; i < n; i++) {
            g2d.drawImage(item.grabImage(0,0), dX, dY, null);
            dX += Constants.CELL_SIZE + Constants.CELL_SIZE / 4;
        }
    }

    private void drawReady(Graphics2D g2d) throws IOException {
        if (readyTimer > 1) {
            pixelNumber.draw(g2d, readyTimer - 1,(dX - 32) / 2, (dY + 32) / 2, 32);
            return;
        }
        g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Ready.png"),(dX - 128) / 2,(dY + 32) / 2,null);
    }
}
