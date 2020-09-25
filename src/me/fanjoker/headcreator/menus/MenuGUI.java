package me.fanjoker.headcreator.menus;

import me.fanjoker.headcreator.Main;
import me.fanjoker.headcreator.objects.HCBlock;
import me.fanjoker.headcreator.utils.ItemBuilder;
import me.fanjoker.headcreator.utils.ScrollerInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuGUI implements Listener {
    
    private Main main;

    public MenuGUI(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    public void openInv(Player p) {
        List<ItemStack> items = getItemStackList();

        new ScrollerInventory(items, "Painel HeadCreator", p);
    }
    public List<ItemStack> getItemStackList() {
        List<ItemStack> items = new ArrayList<>();

        for(int id : main.getConstructor().getMap().keySet()) {

            HCBlock hcBlock = main.getConstructor().getMap().get(id);

            ItemStack itemStack = new ItemBuilder(hcBlock.getConfig().getItemStack())
                    .name("§7#" + id)
                    .lore(
                            "",
                            "§eTipo: §f" + hcBlock.getConfig().getType(),
                            "§eLocalização:",
                            " §eX: §a(§a" + hcBlock.getLoc().getX() + "§a)",
                            " §eY: §a(§a" + hcBlock.getLoc().getY() + "§a)",
                            " §eZ: §a(§a" + hcBlock.getLoc().getZ() + "§a)",
                            "",
                            "§7Clique para obter mais informações"
                    )
                    .build();

            items.add(itemStack);
        }

        return items;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();

        ScrollerInventory inv = ScrollerInventory.users.get(p);
        if(inv == null) return;

        if(e.getInventory().getTitle().equals("Painel HeadCreator")) {

            e.setCancelled(true);
            if (e.getCurrentItem() == null)  return;
            if (e.getCurrentItem().getItemMeta() == null) return;
            if (e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cPróxima página")) {

                if (!(inv.currpage >= inv.pages.size() - 1)) {
                    inv.currpage += 1;
                    p.openInventory(inv.pages.get(inv.currpage));
                }

            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().equals("§cPágina anterior")) {

                if (inv.currpage > 0) {
                    inv.currpage -= 1;
                    p.openInventory(inv.pages.get(inv.currpage));
                }

            } else {
                String id = e.getCurrentItem().getItemMeta().getDisplayName().replace("§7#", "");
                main.getInventories().getHeadGUI().open(p, Integer.parseInt(id));
            }
        }
    }
}
