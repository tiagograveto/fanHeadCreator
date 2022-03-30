package me.fanjoker.headcreator.listener;

import me.fanjoker.headcreator.Main;
import me.fanjoker.headcreator.api.BreakHeadEvent;
import me.fanjoker.headcreator.api.PlaceHeadEvent;
import me.fanjoker.headcreator.objects.HCConfig;
import me.fanjoker.headcreator.config.Messages;
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

        HCConfig hcConfig = main.getSettings().getHeadByItem(e.getItemInHand());
        if (hcConfig == null) return;

        PlaceHeadEvent event = new PlaceHeadEvent(p, hcConfig);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            e.setCancelled(true);
            return;
        }

        main.getManager().createHead(loc, hcConfig);
        e.getPlayer().sendMessage(Messages.PLACED_HEAD.replace("%type%", hcConfig.getType()));
        main.getSettings().createHologram(main.getConstructor().getByLocation(loc));

    }
}
