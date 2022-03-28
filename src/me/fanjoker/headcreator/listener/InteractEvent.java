package me.fanjoker.headcreator.listener;

import me.fanjoker.headcreator.Main;
import me.fanjoker.headcreator.objects.HCBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractEvent implements Listener {

    private Main main;

    public InteractEvent(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {

        Block b = e.getClickedBlock();
        Player p = e.getPlayer();

        if (b.getType() != Material.SKULL) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        HCBlock hcBlock = main.getConstructor().getByLocation(b.getLocation());
        if (hcBlock == null) return;

        if (p.isSneaking() && p.hasPermission("hcreator.admin") && main.getSettings().isOpenWithShift()) {
            main.getInventories().getHeadGUI().open(p, main.getConstructor().getIdByLocation(hcBlock.getLoc()));
            return;
        }

        if (!hcBlock.isToggle()) return;

        if (!hcBlock.getConfig().hasPermission() || p.hasPermission(hcBlock.getConfig().getPermission())) {
            hcBlock.getConfig().executeCommands(p);
        }

    }
}
