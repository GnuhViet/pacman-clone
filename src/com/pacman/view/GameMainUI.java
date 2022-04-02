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

    private MenuButtonListener buttonListener;

    public GameMainUI() {
        buttonListener = new MenuButtonListener();
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

        MenuButton startBtn = new MenuButton("StartButton");
        MenuButton scoreBtn = new MenuButton("ScoreButton");
        MenuButton optionBtn = new MenuButton("OptionsButton");
        MenuButton quitBtn = new MenuButton("QuitButton");
        startBtn.addMouseListener(buttonListener);
        scoreBtn.addMouseListener(buttonListener);
        optionBtn.addMouseListener(buttonListener);
        quitBtn.addMouseListener(buttonListener);

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
                    titleUI.setVisible(true);
                    con.repaint();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    public void showMainUi() {
        titleUI.setVisible(true);
        window.removeKeyListener(gameUI);
        con.repaint();
    }

    private void initGame() { // initGameUI
        Object pauseLock = new Object();
        try {
            gameUI = new GameView(this, pauseLock, con);
            controller = new GameController(gameUI, pauseLock);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Image button Listener// TODO update thanh sprite
    private class MenuButtonListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            MenuButton button = (MenuButton) e.getSource();
            String buttonName = button.getButtonName();

            if (buttonName.equals("StartButton")) {
                titleUI.setVisible(false);
                con.repaint();
                initGame();

                window.addKeyListener(gameUI);
                window.setFocusable(true); // for key listener
                con.add(gameUI);

                controller.startGameThread();
                return;
            }

            if (buttonName.equals("ScoreButton")) {
                return;
            }

            if (buttonName.equals("OptionsButton")) {

                return;
            }

            // quit button
            if (buttonName.equals("QuitButton")) {
                System.exit(0);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            MenuButton btn = (MenuButton) e.getSource();
            btn.setActIcon();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            MenuButton btn = (MenuButton) e.getSource();
            btn.setNorIcon();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
}
