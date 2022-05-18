package com.github.tiagograveto.headcreator.services.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class HeadInventory implements Listener {

    public HeadInventory() {

    }

    public abstract void openInventory(Player p, int id);
    @EventHandler
    public abstract void onClickInInventory(InventoryClickEvent e);
}
