package com.pacman.view;

import com.pacman.controller.GameController;
import com.pacman.entity.PixelNumber;
import com.pacman.utils.BufferedImageLoader;
import com.pacman.utils.Constants;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class GameMainUI {
    private static final String GAME_TITLE = "PACMAN";

    private JFrame window;
    private ImagePanel titleUI;
    private GameView gameUI;
    private GameController controller;
    private Container con;

    private EndUI endUI;

    public GameMainUI() {
        initFrame();
        con = window.getContentPane();
        initTileUI();
        endUI = null;
    }

    private void initFrame() {
        window = new JFrame();
        window.setTitle(GAME_TITLE);
        window.setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void initTileUI() {
        titleUI = new ImagePanel("src\\com\\pacman\\res\\title-background.jpg");

        // Title panel config
        titleUI.setLayout(new BoxLayout(titleUI, BoxLayout.Y_AXIS));

        // sub title panel
        JPanel menuPanel = new JPanel();
        JPanel logoPanel = new JPanel();

        // Logo panel config
        JLabel logo = new JLabel();
        logo.setIcon(new ImageIcon("src\\com\\pacman\\res\\menu-logo.png"));
        logoPanel.add(logo);
        logoPanel.setBorder(new EmptyBorder(40,0,0,0));
        logoPanel.setOpaque(false);

        // Menu panel config
        menuPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        menuPanel.setPreferredSize(new Dimension(300,290));
        menuPanel.setMaximumSize(new Dimension(300, 290));

        // can le ben duoi
        menuPanel.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setOpaque(false);

        MenuButton startBtn = new MenuButton("StartButton.png");
        MenuButton scoreBtn = new MenuButton("ScoreButton.png");
        MenuButton optionBtn = new MenuButton("OptionsButton.png");
        MenuButton quitBtn = new MenuButton("QuitButton.png");

        //add button to menu panel
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,0,5,0);
        c.gridy = 0;
        menuPanel.add(startBtn, c);
        c.gridy = 1;
        menuPanel.add(scoreBtn, c);
        c.gridy = 2;
        menuPanel.add(optionBtn, c);
        c.gridy = 3;
        menuPanel.add(quitBtn, c);

        // add to title ui
        titleUI.add(logoPanel);
        titleUI.add(menuPanel);

        // add to frame
        con.add(titleUI);
    }


    // sub menu ui
    public void initEndUI(int score, boolean isWon) {
        if(endUI != null) {
            endUI.setVisible(true);
            endUI.setWon(isWon);
            window.removeKeyListener(gameUI);
            con.remove(gameUI);
            return;
        }

        try {
            endUI = new EndUI(score, isWon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        endUI.setBackground(Color.BLACK);
        endUI.setOpaque(true);

        window.removeKeyListener(gameUI);
        con.remove(gameUI);

        con.add(endUI);
        window.addKeyListener(endUI);
        window.setFocusable(true);
    }

    private class EndUI extends JPanel implements KeyListener {
        private PixelNumber pixelNumber;
        private int score;
        private boolean yes;
        private boolean isWon;

        private EndUI(int score, boolean isWon) throws IOException {
            pixelNumber = new PixelNumber();
            yes = true;
            this.score = score;
            this.isWon = isWon;
        }

        public void setWon(boolean isWon) {
            this.isWon = isWon;
            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = window.getWidth() / 2;
            int scoreSize = pixelNumber.getSize(this.score, PixelNumber.FontType.MediumBlack);
            int highestScoreSize = pixelNumber.getSize(888222333, PixelNumber.FontType.MediumBlack);

            try {
                g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\BackGround.jpg"), 0,0, null);
                g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\YourScore.png"), width - (173+scoreSize) / 2, 455, null);
                g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\YourHighestScore.png"), width - (292+highestScoreSize) / 2, 505, null);

                if (this.isWon) {
                    g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\GameWon.png"), width-300, -50, null);
                } else {
                    g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\GameOver.png"), width-300, -50, null);
                }

                if (this.yes) {
                    g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\Yes.png"), width-300, -50, null);
                } else {
                    g2d.drawImage(BufferedImageLoader.loadImage("src\\com\\pacman\\res\\No.png"), width-300, -50, null);
                }
            } catch (IOException e) {
            }

            pixelNumber.draw(g2d, this.score, width - (scoreSize) / 2 + 173/2, 450, PixelNumber.FontType.MediumBlack);
            pixelNumber.draw(g2d, 888222333, width - (highestScoreSize) / 2 + 292/2, 500, PixelNumber.FontType.MediumBlack);
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                this.yes = true;
                this.repaint();
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                this.yes = false;
                this.repaint();
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (yes) {
                    initGame();
                    endUI.setVisible(false);
                    window.addKeyListener(gameUI);
                    con.add(gameUI);
                    con.repaint();
                    gameUI.setVisible(true);
                    controller.startGameThread();
                }
                else {
                    endUI.setVisible(false);
                    titleUI.setVisible(true);
                    con.repaint();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    private void initGame() {
        try {
            gameUI = new GameView(this);
            controller = new GameController(gameUI);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Image button // TODO update thanh sprite
    private class MenuButton extends JLabel implements MouseListener {
        private static final String iconFolPath = "src\\com\\pacman\\res\\MenuButton\\";
        String buttonName;
        String iconName;
        String activeIconName;

        ImageIcon norIcon;
        ImageIcon actIcon;

        public MenuButton(String iconName) {
            super();
            this.buttonName = iconName;
            this.iconName = iconFolPath + iconName;
            this.activeIconName = iconFolPath + "Active" + iconName;

            norIcon = new ImageIcon(this.iconName);
            actIcon = new ImageIcon(activeIconName);

            this.setIcon(norIcon);
            addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (buttonName.equals("StartButton.png")) {
                titleUI.setVisible(false);
                con.repaint();
                initGame();

                window.addKeyListener(gameUI);
                window.setFocusable(true); // for key listener
                con.add(gameUI);

                controller.startGameThread();
                return;
            }

            if (buttonName.equals("ScoreButton.png")) {
                return;
            }

            if (buttonName.equals("OptionsButton.png")) {

                return;
            }

            // quit button
            if (buttonName.equals("QuitButton.png")) {
                System.exit(0);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            this.setIcon(actIcon);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            this.setIcon(norIcon);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
