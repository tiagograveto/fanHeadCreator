package me.fanjoker.headcreator.listener;

import me.fanjoker.headcreator.Main;
import me.fanjoker.headcreator.objects.HCBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvent implements Listener {

    private Main main;

    public InteractEvent(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {

        Block block = e.getClickedBlock();
        if (block.getType() != Material.SKULL) return;

        HCBlock hcBlock = main.getConstructor().getByLocation(block.getLocation());
        if (hcBlock == null) return;

        Player p = e.getPlayer();

        if (p.isSneaking() && p.hasPermission("hcreator.admin") && main.getSettings().isOpenWithShift()) {
            main.getInventories().getHeadGUI().open(p, main.getConstructor().getIdByLocation(hcBlock.getLoc()));
        }

        if (!hcBlock.isToggle()) return;

        if (!hcBlock.getConfig().hasPermission() || p.hasPermission(hcBlock.getConfig().getPermission())) {
            hcBlock.getConfig().executeCommands(p);
        }

    }
}
