package me.fanjoker.headcreator.menus;

import me.fanjoker.headcreator.Main;

public class Menus {

    private Main main;
    private PanelGUI menuGUI;
    private HeadGUI headGUI;

    public Menus(Main main) {
        this.main = main;
        menuGUI = new PanelGUI(main);
        headGUI = new HeadGUI(main);
    }

    public HeadGUI getHeadGUI() {
        return headGUI;
    }
    public PanelGUI getMenuGUI() {
        return menuGUI;
    }
}