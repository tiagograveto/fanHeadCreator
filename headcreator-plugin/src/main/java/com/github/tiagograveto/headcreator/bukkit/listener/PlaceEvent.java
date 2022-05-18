package com.github.tiagograveto.headcreator.bukkit.listener;

import com.github.tiagograveto.headcreator.Main;
import com.github.tiagograveto.headcreator.api.PlaceHeadEvent;
import com.github.tiagograveto.headcreator.config.Messages;
import com.github.tiagograveto.headcreator.entities.HCBlock;
import com.github.tiagograveto.headcreator.entities.HCConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceEvent implements Listener {

    private Main main;

    public PlaceEvent(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent e) {

        if (e.getBlock().getType() != Material.SKULL) return;
        Location loc = e.getBlock().getLocation();
        Player p = e.getPlayer();

        HCConfig hcConfig = main.getControllers().getCacheController().getHeadConfigByItemStack(e.getItemInHand());
        if (hcConfig == null) return;

        PlaceHeadEvent event = new PlaceHeadEvent(p, hcConfig);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        HCBlock hcBlock = main.getControllers().getCacheController().getHeadBlockByLocation(loc);
        main.getControllers().getDatabaseController().createHead(loc, hcConfig);
        main.getServices().getHologramService().createHologram(hcBlock);
        e.getPlayer().sendMessage(Messages.PLACED_HEAD.replace("%type%", hcConfig.getType()));

    }
}
