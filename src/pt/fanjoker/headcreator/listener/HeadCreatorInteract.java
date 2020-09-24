package pt.fanjoker.headcreator.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import pt.fanjoker.headcreator.HeadCreator;
import pt.fanjoker.headcreator.menus.MenuGui;

import java.util.Locale;

public class HeadCreatorInteract implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteractHCreator(PlayerInteractEvent e) {
        Location loc = e.getClickedBlock().getLocation();
        if(e.getClickedBlock().getType().equals(Material.SKULL)) {
            if (HeadCreator.getManager().getAllLocs().contains(loc)) {
                int id = HeadCreator.getManager().getId(loc);
                if(HeadCreator.config.getConfig("config").getYaml().getBoolean("Config.OpenWithShift")
                        && e.getPlayer().isSneaking()
                        && e.getPlayer().hasPermission("hcreator.admin")) {
                    MenuGui.openInventory(e.getPlayer(), id);
                    return;
                }
                String type = HeadCreator.getManager().getType(id);
                if(HeadCreator.getManager().getToggle(id))
                    if (HeadCreator.getSettings().hasPermission(e.getPlayer(), type))
                        HeadCreator.getSettings().executeCommands(e.getPlayer(), type);

            }
        }
    }
}
