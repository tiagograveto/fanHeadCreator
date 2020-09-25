package me.fanjoker.headcreator.menus;

import me.fanjoker.headcreator.Main;

public class Inventories {

    private Main main;
    private MenuGUI menuGUI;
    private HeadGUI headGUI;

    public Inventories(Main main) {
        this.main = main;
        menuGUI = new MenuGUI(main);
        headGUI = new HeadGUI(main);
    }

    public HeadGUI getHeadGUI() {
        return headGUI;
    }
    public MenuGUI getMenuGUI() {
        return menuGUI;
    }
}
