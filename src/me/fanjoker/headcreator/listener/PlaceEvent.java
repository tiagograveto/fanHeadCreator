package me.fanjoker.headcreator.listener;

import me.fanjoker.headcreator.Main;
import me.fanjoker.headcreator.objects.HCBlock;
import me.fanjoker.headcreator.objects.HCConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.omg.CORBA.MARSHAL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        e.getPlayer().sendMessage("§aVocê colocou a cabeça §f'" + type + "' §acom êxito..");

        main.getSettings().createHologram(main.getConstructor().getByLocation(loc));

    }
}
