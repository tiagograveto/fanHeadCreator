package me.fanjoker.headcreator.menus;

import me.fanjoker.headcreator.Main;
import me.fanjoker.headcreator.config.Config;
import me.fanjoker.headcreator.objects.HCBlock;
import me.fanjoker.headcreator.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class HeadGUI implements Listener {

    private Main main;

    public HeadGUI(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    public void open(Player p, int id) {

        HCBlock hcBlock = main.getConstructor().getMap().get(id);

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
    @EventHandler
    public void aoClicar(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();

        if (e.getInventory().getTitle().startsWith("Informações da cabeça: ")) {
            e.setCancelled(true);
            int id = Integer.parseInt(e.getInventory().getTitle().replace("Informações da cabeça: ", ""));
            HCBlock hcBlock = main.getConstructor().getMap().get(id);

            if (e.getCurrentItem() == null) return;

            if (e.getSlot() == 11) {
                hcBlock.teleport(p);
                p.sendMessage("§aTeleportado com êxito.");
                p.closeInventory();
            }

            if(e.getSlot() == 12) {

                hcBlock.setToggle(!hcBlock.isToggle());
                open(p, id);

                if(Config.DISABLE_HOLOGRAM)
                    main.getSettings().reloadHolograms();

            }
            if(e.getSlot() == 15) {
                Location loc = hcBlock.getLoc();
                main.getManager().deleteHead(loc);
                main.getSettings().reloadHolograms();
                p.closeInventory();

            } else if(e.getRawSlot() == 31) {
                main.getInventories().getMenuGUI().openInv(p);
            }
        }

    }
}
