package com.pacman.view;

import com.pacman.controller.GhostManager;
import com.pacman.entity.Map;
import com.pacman.entity.Pacman;
import com.pacman.entity.SpriteSheet;
import com.pacman.utils.BufferedImageLoader;
import com.pacman.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameView extends JPanel implements KeyListener{
    private SpriteSheet mapSprite;
    private Map map;
    private Pacman pacman;
    private GhostManager ghostManager;

    private int key;

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
        initGame();
    }

    private void initGame() throws IOException {
        this.setOpaque(true);
        this.setBackground(Color.BLACK);

        // load sprite
        mapSprite = new SpriteSheet(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Entity\\Map32.png"));
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

    private void drawMap(Graphics2D g2d) throws IOException {
        for (int a = 0; a < Constants.MAP_WIDTH; a++){
            for (int b = 0; b < Constants.MAP_HEIGHT; b++) {

                int xPos = ((b * Constants.CELL_SIZE));
                int yPos = ((a * Constants.CELL_SIZE));

                switch (map.getMapItem(b, a)) {
                    case Door: {
                        g2d.drawImage(mapSprite.grabImage(1,2),  xPos, yPos, null);
                        break;
                    }
                    case Energizer: {
                        if (map.isEnergizerOff()) {
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
                            if (Constants.Cell.Wall == map.getMapItem(b + 1, a)) {
                                right = 1; // right
                            }
                        }

                        if (a > 0) {
                            if (Constants.Cell.Wall == map.getMapItem(b, a - 1)) {
                                up = 1; // up
                            }
                        }

                        if (a < Constants.MAP_HEIGHT - 1) {
                            if (Constants.Cell.Wall == map.getMapItem(b, a + 1)) {
                                down = 1; // dow
                            }
                        }

                        if (b > 0) {
                            if (Constants.Cell.Wall == map.getMapItem(b - 1, a)) {
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
