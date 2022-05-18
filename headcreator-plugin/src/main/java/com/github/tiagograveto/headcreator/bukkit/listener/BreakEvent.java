package com.github.tiagograveto.headcreator.bukkit.listener;

import com.github.tiagograveto.headcreator.Main;
import com.github.tiagograveto.headcreator.api.BreakHeadEvent;
import com.github.tiagograveto.headcreator.config.Config;
import com.github.tiagograveto.headcreator.config.Messages;
import com.github.tiagograveto.headcreator.entities.HCBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakEvent implements Listener {

    private Main main;

    public BreakEvent(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent e) {

        Block b = e.getBlock();
        Player p = e.getPlayer();

        if (b.getType() != Material.SKULL) return;
        if (!e.getPlayer().hasPermission(Config.PERMISSION)) return;

        HCBlock hcBlock = main.getControllers().getCacheController().getHeadBlockByLocation(b.getLocation());
        if (hcBlock == null) return;

        BreakHeadEvent event = new BreakHeadEvent(p, hcBlock);
        main.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        main.getControllers().getDatabaseController().deleteHead(hcBlock.getLoc());
        e.getPlayer().sendMessage(Messages.BREAK_HEAD);
    }
}
