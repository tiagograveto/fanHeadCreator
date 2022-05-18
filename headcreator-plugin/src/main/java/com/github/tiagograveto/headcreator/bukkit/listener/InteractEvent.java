package com.github.tiagograveto.headcreator.bukkit.listener;

import com.github.tiagograveto.headcreator.Main;
import com.github.tiagograveto.headcreator.api.InteractHeadEvent;
import com.github.tiagograveto.headcreator.config.Config;
import com.github.tiagograveto.headcreator.entities.HCBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvent implements Listener {

    private Main main;

    public InteractEvent(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {

        Block b = e.getClickedBlock();
        Player p = e.getPlayer();

        if (b.getType() != Material.SKULL) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        HCBlock hcBlock = main.getControllers().getCacheController().getHeadBlockByLocation(b.getLocation());
        if (hcBlock == null) return;

        InteractHeadEvent event = new InteractHeadEvent(p, hcBlock);
        main.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        if (p.isSneaking() && p.hasPermission(Config.PERMISSION) && Config.OPEN_WITH_SHIFT) {
            int id = main.getControllers().getCacheController().getHeadBlockIdByLocation(hcBlock.getLoc());
            main.getServices().getInventoryService().getHeadInfoInventory().openInventory(p, id);
            return;
        }

        if (!hcBlock.isToggle()) return;

        if (!hcBlock.getConfig().hasPermission() || p.hasPermission(hcBlock.getConfig().getPermission())) {
            hcBlock.getConfig().executeCommands(p);
        }

    }
}
