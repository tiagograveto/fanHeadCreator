package pt.fanjoker.headcreator.menus;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import pt.fanjoker.headcreator.HeadCreator;
import pt.fanjoker.headcreator.utils.ItemBuilder;

public class MenuGui implements Listener {

    public static void openInventory(Player p, int id) {
        Inventory inv = Bukkit.createInventory(null, 4 * 9, "Informações da cabeça: " + id);
        inv.setItem(11, new ItemBuilder(Material.ENDER_PEARL).name("§aTeleportar")
                .lore("§a・§7 Clique para teleportar à cabeça")
                .removeAttributes().build());
        if(HeadCreator.getManager().getToggle(id)) {
            inv.setItem(12, new ItemBuilder(Material.INK_SACK).durability(10).name("§cDesativar")
                    .lore("§a・§7 Clique para desativar os comandos da cabeça", "§7   Status: §aAtivado")
                    .removeAttributes().build());
        } else {
            inv.setItem(12, new ItemBuilder(Material.INK_SACK).durability(8).name("§aAtivar")
                    .lore("§a・§7 Clique para ativar os comandos da cabeça", "§7   Status: §cDesativado")
                    .removeAttributes().build());
        }
        inv.setItem(15, new ItemBuilder(Material.BARRIER).name("§cDeletar")
                .lore("§a・§7 Clique para deletar a cabeça")
                .removeAttributes().build());

        inv.setItem(31, new ItemBuilder(Material.ARROW).name("§aVoltar")
                .lore("§a・§7 Clique para voltar para o menu principal")
                .removeAttributes().build());

        p.openInventory(inv);
    }
    @EventHandler
    public void aoClicar(InventoryClickEvent e) {
        if (e.getInventory().getTitle().startsWith("Informações da cabeça: ")) {
            int id = Integer.valueOf(e.getInventory().getTitle().replace("Informações da cabeça: ", ""));
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            if (e.getCurrentItem() == null)
                return;

            if (e.getRawSlot() == 11) {
                Location mn = HeadCreator.getManager().getLocation(id);
                Location loc = new Location(mn.getWorld(), mn.getX(), mn.getY(), mn.getZ());
                loc.add(0.5, 0.5, 0.5);
                loc.setPitch(90);
                p.teleport(loc);
                p.sendMessage("§aTeleportado com êxito.");
                p.closeInventory();
            } else if(e.getRawSlot() == 12) {
                if(HeadCreator.getManager().getToggle(id)) {
                    HeadCreator.getManager().setToggle(id, false);
                    if(HeadCreator.config.getConfig("config").getYaml().getBoolean("Config.DisableHologram")) {
                        HeadCreator.getSettings().reloadHolograms();
                    }
                    MenuGui.openInventory(p, id);
                    return;
                }
                HeadCreator.getManager().setToggle(id, true);
                if(HeadCreator.config.getConfig("config").getYaml().getBoolean("Config.DisableHologram")) {
                    HeadCreator.getSettings().reloadHolograms();
                }
                MenuGui.openInventory(p, id);
            } else if(e.getRawSlot() == 15) {
                Location loc = HeadCreator.getManager().getLocation(id).clone();
                loc.getBlock().setType(Material.AIR);
                HeadCreator.getManager().deleteHead(loc);
                HeadCreator.getSettings().reloadHolograms();
                p.closeInventory();

            } else if(e.getRawSlot() == 31) {
                HeadCreator.getSettings().openInv(p);
            }
        }

    }
}
