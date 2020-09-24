package pt.fanjoker.headcreator.listener;

import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import io.netty.handler.codec.spdy.SpdyHeaderBlockRawDecoder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import pt.fanjoker.headcreator.HeadCreator;
import sun.applet.Main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HeadCreatorPlace implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlaceHeadCreator(BlockPlaceEvent e) {
        Location loc = e.getBlock().getLocation();
        if(HeadCreator.getSettings().isHead(e.getItemInHand())) {
            String type = HeadCreator.getSettings().getType(e.getItemInHand().getItemMeta().getDisplayName());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  Date date = new Date();
            HeadCreator.getManager().create(e.getPlayer().getName(), loc, type, dateFormat.format(date));
            e.getPlayer().sendMessage("§aVocê colocou a cabeça §f'" + type + "' §acom êxito..");
            HeadCreator.getSettings().createHologram(type, loc.clone());
        }
    }
}
