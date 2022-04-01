package com.pacman.MapMaker;

import com.pacman.controller.GhostManager;
import com.pacman.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MapMakerApp {
    private static final String GAME_TITLE = "MapMakerApp";

    private JFrame window;
    private JPanel main;
    private Container con;
    private char text;

    MapListener mapListener;
    ItemButtonListener itemButtonListener;
    JButton[] jButtonMap;

    public MapMakerApp() {
        window = new JFrame();
        mapListener = new MapListener();
        itemButtonListener = new ItemButtonListener();
        jButtonMap = new JButton[Constants.MAP_WIDTH * Constants.MAP_HEIGHT];
        initTileUI();
        initFrame();
    }

    private void initFrame() {
        window.setTitle(GAME_TITLE);
        window.setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void initTileUI() {
        main = new JPanel();
        con = window.getContentPane();

        // sub title panel
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        main.add(leftPanel, BorderLayout.CENTER);
        main.add(rightPanel, BorderLayout.WEST);

        initMapButton(leftPanel);

        JPanel rightNorthPanel = new JPanel();
        JPanel rightSouthPanel = new JPanel();

        rightPanel.add(rightNorthPanel, BorderLayout.NORTH);
        rightPanel.add(rightSouthPanel, BorderLayout.SOUTH); // TODO load save

        rightNorthPanel.setLayout(new GridLayout(20, 1));
        initRightButton(rightNorthPanel, jButtonMap);

        // add to frame
        con.add(main);
        window.setContentPane(con);
    }

    private void initMapButton(JPanel panel) {
        JLabel[] northLb = new JLabel[21];
        JLabel[] westLb = new JLabel[21];
        for (int i = 0; i < Constants.MAP_HEIGHT; i++) {
            northLb[i] = new JLabel();
            westLb[i] = new JLabel();
            northLb[i].setPreferredSize(new Dimension(Constants.CELL_SIZE + 10, Constants.CELL_SIZE + 10));
            westLb[i].setPreferredSize(new Dimension(Constants.CELL_SIZE + 10, Constants.CELL_SIZE + 10));
            northLb[i].setText("     " + (i + 1));
            westLb[i].setText((i + 1)+ " ");
        }

        panel.setLayout(new GridLayout(22, 22));

        JLabel zeroBtn = new JLabel();
        zeroBtn.setPreferredSize(new Dimension(Constants.CELL_SIZE + 10, Constants.CELL_SIZE + 10));
        zeroBtn.setText(0 + " ");
        panel.add(zeroBtn);

        for (int i = 0; i < Constants.MAP_WIDTH; i++) {
            panel.add(northLb[i]);
        }


        int c = 0;
        for (int i = 1; i < Constants.MAP_WIDTH + 1; i++) {
            for (int j = 0; j < Constants.MAP_HEIGHT + 1; j++) {
                if (j == 0) {
                    panel.add(westLb[i-1]);
                }
                if (j != 0) {
                    jButtonMap[c] = new JButton(".");
                    jButtonMap[c].setFocusPainted(false);
                    jButtonMap[c].setPreferredSize(new Dimension(Constants.CELL_SIZE + 10, Constants.CELL_SIZE + 10));
                    jButtonMap[c].setBackground(Color.BLACK);
                    jButtonMap[c].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    jButtonMap[c].setFont(new Font("Arial", Font.PLAIN, 25));
                    jButtonMap[c].setForeground(Color.WHITE);
                    jButtonMap[c].addMouseListener(mapListener);
                    panel.add(jButtonMap[c]);
                    c++;
                }
            }
        }
    }

    private void initRightButton(JPanel panel, JButton[] jButtonMap) {
        // left radio button
        int numberOfCells = Constants.Cell.values().length;
        int numberOfItemButtons = numberOfCells + GhostManager.NUMBER_OF_GHOSTS + 1;
        JRadioButton[] itemButtons = new JRadioButton[numberOfItemButtons];
        int c = 0;
        while (c < numberOfCells) {
            itemButtons[c] = new JRadioButton(Constants.Cell.values()[c].toString());
            itemButtons[c].setFocusPainted(false);
            itemButtons[c].setFont(new Font("Arial", Font.PLAIN, 25));
            itemButtons[c].addMouseListener(itemButtonListener);
            panel.add(itemButtons[c]);
            c++;
        }
        int i = 0;
        while (c < numberOfCells + GhostManager.NUMBER_OF_GHOSTS) {
            itemButtons[c] = new JRadioButton(GhostManager.GhostType.values()[i].toString() + "-" + i);
            itemButtons[c].setFocusPainted(false);
            itemButtons[c].setFont(new Font("Arial", Font.PLAIN, 25));
            itemButtons[c].addMouseListener(itemButtonListener);
            panel.add(itemButtons[c]);
            i++;
            c++;
        }
        itemButtons[c] = new JRadioButton("Pacman");
        itemButtons[c].setFocusPainted(false);
        itemButtons[c].setFont(new Font("Arial", Font.PLAIN, 25));
        itemButtons[c].addMouseListener(itemButtonListener);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFocusPainted(false);
        clearBtn.setFont(new Font("Arial", Font.PLAIN, 25));
        clearBtn.addMouseListener(new MouseListener() {
            public void clear() {
                for (int i = 0; i < Constants.MAP_WIDTH * Constants.MAP_HEIGHT; i++) {
                    jButtonMap[i].setBackground(Color.BLACK);
                    jButtonMap[i].setText(" ");
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                clear();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                clear();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JButton exportBtn = new JButton("Export");
        exportBtn.setFocusPainted(false);
        exportBtn.setFont(new Font("Arial", Font.PLAIN, 25));
        exportBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String[] res = getString();
                System.out.println("MAP:.....");
                for (String x : res) {
                    System.out.println(x);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        panel.add(itemButtons[c]);
        panel.add(clearBtn);
        panel.add(exportBtn);

        ButtonGroup itemButtonGroup = new ButtonGroup();
        for (i = 0; i < numberOfItemButtons; i++) {
            itemButtonGroup.add(itemButtons[i]);
        }
    }

    private class ItemButtonListener implements MouseListener {
        private void click(MouseEvent e) {
            JRadioButton btn = (JRadioButton) e.getSource();
            String type = btn.getText();
            switch (type) {
                case "Door":
                    text = '=';
                    break;
                case "Empty":
                    text = ' ';
                    break;
                case "Energizer":
                    text = 'o';
                    break;
                case "Pellet":
                    text = '.';
                    break;
                case "Wall":
                    text = '#';
                    break;
                case "RED-0":
                    text = '0';
                    break;
                case "PINK-1":
                    text = '1';
                    break;
                case "BLUE-2":
                    text = '2';
                    break;
                case "ORANGE-3":
                    text = '3';
                    break;
                case "Pacman":
                    text = 'P';
                    break;
            }
            System.out.println(text);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            click(e);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            click(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    private class MapListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            JButton item = (JButton) e.getSource();
            item.setText(text + "");
            if("#".equals(item.getText())) {
                item.setBackground(Color.BLUE);
            }
            else {
                item.setBackground(Color.BLACK);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            JButton item = (JButton) e.getSource();
            item.setText(text + "");
            if("#".equals(item.getText())) {
                item.setBackground(Color.BLUE);
            }
            else {
                item.setBackground(Color.BLACK);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    public String[] getString() {
        String[] res = new String[Constants.MAP_HEIGHT];

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < Constants.MAP_HEIGHT; i++) {
            builder.setLength(0);
            for (int j = 0; j < Constants.MAP_WIDTH; j++) {
                builder.append(jButtonMap[i*Constants.MAP_WIDTH + j].getText());
            }
            res[i] = builder.toString();
        }

        return res;
    }

    public static void main(String[] args) {
        new MapMakerApp();
    }
}
