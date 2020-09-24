package pt.fanjoker.headcreator.listener;

import io.netty.channel.local.LocalAddress;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.MetadataValue;
import pt.fanjoker.headcreator.HeadCreator;

import java.util.List;

public class HeadCreatorBreak implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onBreakHeadCreator(BlockBreakEvent e) {
        if(e.getBlock().getType().equals(Material.SKULL)) {
            if (HeadCreator.getManager().getAllLocs().contains(e.getBlock().getLocation())) {
                int id = HeadCreator.getManager().getId(e.getBlock().getLocation());
                String type = HeadCreator.getManager().getType(id);
                HeadCreator.getManager().deleteHead(e.getBlock().getLocation());
                e.getPlayer().sendMessage("§cVocê deletou a cabeça §f'" + type + "' §ccom êxito.");
                HeadCreator.getSettings().reloadHolograms();
            }
        }
    }
}
