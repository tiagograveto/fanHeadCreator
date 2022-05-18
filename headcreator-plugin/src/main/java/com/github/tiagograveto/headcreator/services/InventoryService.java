package com.github.tiagograveto.headcreator.services;

import com.github.tiagograveto.headcreator.Main;
import com.github.tiagograveto.headcreator.services.inventories.HeadInfoInventory;
import com.github.tiagograveto.headcreator.services.inventories.HeadPanelInventory;

public class InventoryService {

    private HeadInfoInventory headInfoInventory;
    private HeadPanelInventory headPanelInventory;

    public InventoryService(Main main) {
        this.headInfoInventory = new HeadInfoInventory(main);
        this.headPanelInventory = new HeadPanelInventory(main);
    }

    public HeadInfoInventory getHeadInfoInventory() {
        return headInfoInventory;
    }

    public HeadPanelInventory getHeadPanelInventory() {
        return headPanelInventory;
    }
}
