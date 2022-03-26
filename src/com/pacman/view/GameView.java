package com.pacman.view;

import com.pacman.controller.GhostManager;
import com.pacman.entity.Map;
import com.pacman.entity.Pacman;
import com.pacman.entity.PixelNumber;
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

    private int key;
    private int timer = 3;

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
            map.drawMap(g2d);
            pacman.draw(g2d);
            ghostManager.draw(g2d);
            if (!isReady) {
                this.drawReady(g2d);
            }
            if (isLose) {
                drawDead(g2d);
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
        // load sprite

        pixelNumber = new PixelNumber();
        timer = 4;
    }

    public void update(Pacman pacman, GhostManager ghostManager, Map map) {
        this.pacman = pacman;
        this.ghostManager = ghostManager;
        this.map = map;
        this.repaint();
    }

    public int getKey() {
        return key;
    }

    public void setLose(boolean lose) {
        this.isLose = lose;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public void decreaseTimer() {
        timer-=1;
    }

    public void switchColor() {
        switchColor = !switchColor;
    }

    private void drawReady(Graphics2D g2d) throws IOException {
        if (timer > 1) {
            pixelNumber.draw(g2d, timer - 1,(dX - 32) / 2, (dY + 32) / 2);
            return;
        }
        g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Ready.png"),(dX - 128) / 2,(dY + 32) / 2,null);
    }

    public void drawDead(Graphics2D g2d) throws IOException {
        if (switchColor) {
            g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\lose01.png"), (dX - 128) / 2, (dY + 32) / 2, null);
            return;
        }
        g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\lose02.png"), (dX - 128) / 2, (dY + 32) / 2, null);
    }
}
