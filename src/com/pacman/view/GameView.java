package com.pacman.view;

import com.pacman.entity.Pacman;
import com.pacman.entity.SpriteSheet;
import com.pacman.ultis.BufferedImageLoader;
import com.pacman.ultis.Constants;
import com.pacman.ultis.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameView extends JPanel implements ActionListener, KeyListener{
    private FileUtils data; // TODO... chinh sua lai
    private SpriteSheet mapSprite;;
    private Constants.Cell[][] mapInput;
    private Pacman pacman;
    private Timer timer;

    public GameView() throws IOException {
        data = new FileUtils();
        pacman = new Pacman();
        initGame();
    }

    private void initGame() throws IOException {
        this.setOpaque(true);
        this.setBackground(Color.BLACK);

        mapSprite = new SpriteSheet(BufferedImageLoader.loadImage("src/com/pacman/res/Entity/Map16.png"));

        mapInput = data.getMap(pacman); //TODO .. chinh sua lai

        timer = new Timer(200,this); // loop game
        timer.start();
    }


    private void drawMap(Graphics2D g2d) throws IOException {

        for (int a = 0; a < Constants.MAP_WIDTH; a++){
            for (int b = 0; b < Constants.MAP_HEIGHT; b++) {

                int x = b + 1;
                int y = a + 1;

                int xPos = ((x * Constants.CELL_SIZE) - Constants.CELL_SIZE) * Constants.SCREEN_RESIZE;
                int yPos = Constants.CELL_SIZE * 2 + ((y * Constants.CELL_SIZE) - Constants.CELL_SIZE) * Constants.SCREEN_RESIZE;

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
        System.out.println("123123");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        pacman.update();
        repaint(); // ve lai game
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pacman.keyPressed(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
