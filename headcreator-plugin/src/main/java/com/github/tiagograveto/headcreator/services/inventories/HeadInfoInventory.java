package com.github.tiagograveto.headcreator.services.inventories;

import com.github.tiagograveto.headcreator.Main;
import com.github.tiagograveto.headcreator.config.Config;
import com.github.tiagograveto.headcreator.entities.HCBlock;
import com.github.tiagograveto.headcreator.services.inventories.entities.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class HeadInfoInventory extends HeadInventory implements Listener {

    private Main main;

    public HeadInfoInventory(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @Override
    public void openInventory(Player p, int id) {
        HCBlock hcBlock = main.getControllers().getCacheController().getCacheBlocks().get(id);

        Inventory inv = Bukkit.createInventory(null, 4 * 9, "Informações da cabeça: " + id);

        inv.setItem(11, new ItemBuilder(Material.ENDER_PEARL).name("§aTeleportar")
                .lore("§a・§7 Clique para teleportar à cabeça").build());

        if (hcBlock.isToggle())

            inv.setItem(12, new ItemBuilder(Material.INK_SACK).durability(10).name("§cDesativar")
                    .lore("§a・§7 Clique para desativar os comandos da cabeça", "§7   Status: §aAtivado").build());
        else

            inv.setItem(12, new ItemBuilder(Material.INK_SACK).durability(8).name("§aAtivar")
                    .lore("§a・§7 Clique para ativar os comandos da cabeça", "§7   Status: §cDesativado").build());

        inv.setItem(15, new ItemBuilder(Material.BARRIER).name("§cDeletar")
                .lore("§a・§7 Clique para deletar a cabeça").build());

        inv.setItem(31, new ItemBuilder(Material.ARROW).name("§aVoltar")
                .lore("§a・§7 Clique para voltar para o menu principal").build());

        p.openInventory(inv);
    }

    @Override @EventHandler
    public void onClickInInventory(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();

        if (e.getInventory().getTitle().startsWith("Informações da cabeça: ")) {
            e.setCancelled(true);
            int id = Integer.parseInt(e.getInventory().getTitle().replace("Informações da cabeça: ", ""));
            HCBlock hcBlock = main.getControllers().getCacheController().getCacheBlocks().get(id);

            if (e.getCurrentItem() == null) return;

            if (e.getSlot() == 11) {
                hcBlock.teleport(p);
                p.sendMessage("§aTeleportado com êxito.");
                p.closeInventory();
            }

            if(e.getSlot() == 12) {

                hcBlock.setToggle(!hcBlock.isToggle());
                this.openInventory(p, id);

                if(!Config.DISABLE_HOLOGRAM)
                    main.getServices().getHologramService().reloadHeadHolograms();

            }
            if(e.getSlot() == 15) {
                Location loc = hcBlock.getLoc();
                main.getControllers().getDatabaseController().deleteHead(loc);
                main.getServices().getHologramService().reloadHeadHolograms();
                p.closeInventory();

            } else if(e.getRawSlot() == 31) {
                main.getServices().getInventoryService().getHeadPanelInventory().openInventory(p, id);
            }
        }
    }
}
