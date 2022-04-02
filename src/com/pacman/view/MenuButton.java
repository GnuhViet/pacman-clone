package com.pacman.view;

import javax.swing.*;

public class MenuButton extends JLabel{
    private static final String iconFolPath = "src\\com\\pacman\\res\\MenuButton\\";
    String buttonName;
    String iconName;
    String activeIconName;

    ImageIcon norIcon;
    ImageIcon actIcon;

    public MenuButton(String iconName) {
        super();
        this.buttonName = iconName;
        this.iconName = iconFolPath + iconName + ".png";
        this.activeIconName = iconFolPath + "Active" + iconName + ".png";
        norIcon = new ImageIcon(this.iconName);
        actIcon = new ImageIcon(activeIconName);
        this.setIcon(norIcon);
    }

    public String getButtonName() {
        return buttonName;
    }

    public void setActIcon() {
        this.setIcon(actIcon);
    }

    public void setNorIcon() {
        this.setIcon(norIcon);
    }
}