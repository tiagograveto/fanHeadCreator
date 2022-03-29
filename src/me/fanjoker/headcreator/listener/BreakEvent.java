package me.fanjoker.headcreator.listener;

import me.fanjoker.headcreator.Main;
import me.fanjoker.headcreator.api.BreakHeadEvent;
import me.fanjoker.headcreator.config.Config;
import me.fanjoker.headcreator.objects.HCBlock;
import me.fanjoker.headcreator.config.Messages;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

        if (b.getType() != Material.SKULL) return;
        if (!e.getPlayer().hasPermission(Config.PERMISSION)) return;

        HCBlock hcBlock = main.getConstructor().getByLocation(b.getLocation());

        if (hcBlock == null) return;

        main.getManager().deleteHead(hcBlock.getLoc());
        main.getServer().getPluginManager().callEvent(new BreakHeadEvent(e.getPlayer(), hcBlock));
        e.getPlayer().sendMessage(Messages.BREAK_HEAD);
    }
}
