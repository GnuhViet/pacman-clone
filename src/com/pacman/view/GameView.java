package com.pacman.view;

import com.pacman.entity.SpriteSheet;
import com.pacman.ultis.BufferedImageLoader;
import com.pacman.ultis.Constants;
import com.pacman.ultis.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameView extends JPanel {
    private FileUtils data; // TODO... chinh sua lai
    private SpriteSheet mapSprite;;
    private Constants.Cell[][] mapInput;

    public GameView() throws IOException {
        data = new FileUtils();
        initGame();
    }

    private void initGame() throws IOException {
        mapSprite = new SpriteSheet(BufferedImageLoader.loadImage("src/com/pacman/res/Entity/Map16.png"));

        mapInput = data.getMap(); //TODO .. chinh sua lai
        this.setOpaque(true);
        this.setBackground(Color.BLACK);
        this.setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
    }

    private void drawMap(Graphics2D g2d) throws IOException {

        for (int a = 0; a < Constants.MAP_WIDTH; a++){
            for (int b = 0; b < Constants.MAP_HEIGHT; b++) {

                int x_pos = Constants.CELL_SIZE + ((b * Constants.CELL_SIZE) - Constants.CELL_SIZE) * Constants.SCREEN_RESIZE;

                int y_pos = Constants.FONT_SIZE + Constants.CELL_SIZE * 3 + ((a * Constants.CELL_SIZE) - Constants.CELL_SIZE) * Constants.SCREEN_RESIZE;

                switch (mapInput[a][b]) {
                    case Door: {
                        g2d.drawImage(mapSprite.grabImage(2,3), x_pos, y_pos, null);
                        break;
                    }
                    case Energizer: {
                        g2d.drawImage(mapSprite.grabImage(2,2), x_pos, y_pos, null);
                        break;
                    }
                    case Pellet: {
                        g2d.drawImage(mapSprite.grabImage(2,1), x_pos, y_pos, null);
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

                        int pos = (down + 2 * (left + 2 * (right + 2 * up))) + 1;

                        g2d.drawImage(mapSprite.grabImage(1, pos), x_pos, y_pos, null);
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
            drawMap(g2d);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
