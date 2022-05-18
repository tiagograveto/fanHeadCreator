package com.github.tiagograveto.headcreator.services.inventories;

import com.github.tiagograveto.headcreator.Main;
import com.github.tiagograveto.headcreator.entities.HCBlock;
import com.github.tiagograveto.headcreator.services.inventories.entities.ItemBuilder;
import com.github.tiagograveto.headcreator.services.inventories.entities.ScrollerInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HeadPanelInventory extends HeadInventory implements Listener {
    
    private Main main;

    public HeadPanelInventory(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @Override
    public void openInventory(Player p, int id) {

        List<ItemStack> items = getItemStackList();

        new ScrollerInventory(items, "Painel HeadCreator", p);
    }

    public List<ItemStack> getItemStackList() {
        List<ItemStack> items = new ArrayList<>();

        for(int id : main.getControllers().getCacheController().getCacheBlocks().keySet()) {

            HCBlock hcBlock = main.getControllers().getCacheController().getCacheBlocks().get(id);

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

    @Override @EventHandler
    public void onClickInInventory(InventoryClickEvent e) {
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
                int id = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().replace("§7#", ""));
                main.getServices().getInventoryService().getHeadInfoInventory().openInventory(p, id);
            }
        }
    }
}
