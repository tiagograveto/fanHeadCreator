package me.fanjoker.headcreator.listener;

import me.fanjoker.headcreator.Main;
import me.fanjoker.headcreator.objects.HCBlock;
import org.bukkit.Material;
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

        if (e.getBlock().getType() != Material.SKULL) return;
        if (!e.getPlayer().hasPermission("hcreator.admin")) return;

        HCBlock hcBlock = main.getConstructor().getByLocation(e.getBlock().getLocation());
        if (hcBlock == null) return;

        main.getManager().deleteHead(hcBlock.getLoc());
    }
}
