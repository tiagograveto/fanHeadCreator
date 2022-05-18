package com.github.tiagograveto.headcreator.services;

import com.github.tiagograveto.headcreator.Main;

public class HeadCreatorServices {

    private HologramDisplaysService hologramService;
    private InventoryService inventoryService;

    public HeadCreatorServices(Main main) {
        this.hologramService = new HologramDisplaysService(main);
        this.inventoryService = new InventoryService(main);
        hologramService.reloadHeadHolograms();
    }
    public InventoryService getInventoryService() {
        return inventoryService;
    }
    public HologramDisplaysService getHologramService() {
        return hologramService;
    }
}
