package pt.fanjoker.headcreator.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import pt.fanjoker.headcreator.HeadCreator;
import pt.fanjoker.headcreator.menus.MenuGui;

public class ScrollerInventoryListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        if(!ScrollerInventory.users.containsKey(p.getUniqueId())) return;
        ScrollerInventory inv = ScrollerInventory.users.get(p.getUniqueId());
        if(e.getInventory().getTitle().equals("Painel de Cabeças")) {

            if(e.getCurrentItem() == null) { e.setCancelled(true); return; }
            if(e.getCurrentItem().getItemMeta() == null) { e.setCancelled(true); return; }
            if(e.getCurrentItem().getItemMeta().getDisplayName() == null) { e.setCancelled(true); return; }

            if(e.getCurrentItem().getType().equals(Material.AIR)) {
                e.setCancelled(true);

            } else if (e.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE)) {
                e.setCancelled(true);
            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cPróxima página")) {
                e.setCancelled(true);
                if (!(inv.currpage >= inv.pages.size() - 1)) {
                    inv.currpage += 1;
                    p.openInventory(inv.pages.get(inv.currpage));
                }

            } else if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cPágina anterior")) {
                e.setCancelled(true);
                if (inv.currpage > 0) {
                    inv.currpage -= 1;
                    p.openInventory(inv.pages.get(inv.currpage));
                }
            } else {
                String id = e.getCurrentItem().getItemMeta().getDisplayName().replace("§7#", "");
                e.setCancelled(true);
                MenuGui.openInventory(p, Integer.valueOf(id));
            }
        }
    }
}
