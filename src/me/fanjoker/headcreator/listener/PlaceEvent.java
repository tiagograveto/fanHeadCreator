package me.fanjoker.headcreator.listener;

import me.fanjoker.headcreator.Main;
import me.fanjoker.headcreator.api.PlaceHeadEvent;
import me.fanjoker.headcreator.objects.HCConfig;
import me.fanjoker.headcreator.config.Messages;
import org.bukkit.Location;
import org.bukkit.Material;
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

        HCConfig hcConfig = main.getSettings().getHeadByItem(e.getItemInHand());
        if (hcConfig == null) return;

        String type = hcConfig.getType();
        main.getManager().create(loc, type);
        e.getPlayer().sendMessage(Messages.PLACED_HEAD);
        main.getServer().getPluginManager().callEvent(new PlaceHeadEvent(hcConfig));

        main.getSettings().createHologram(main.getConstructor().getByLocation(loc));

    }
}
