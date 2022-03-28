package me.fanjoker.headcreator.utils;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ScrollerInventory implements Listener {

    public ArrayList<Inventory> pages = new ArrayList<>();
    public UUID id;
    public int currpage = 0;

    public static Map<Player, ScrollerInventory> users = new HashMap<>();

    public ScrollerInventory(List<ItemStack> items, String name, Player p) {
        this.id = UUID.randomUUID();
        Inventory page = getBlankPage(name);
        int j = 0;
        for (ItemStack item : items) {
            if (page.firstEmpty() == -1) {
                pages.add(page);
                page = getBlankPage(name);
            }
            page.addItem(item);
        }
        pages.add(page);

        p.openInventory(pages.get(currpage));
        users.put(p, this);
    }


    private Inventory getBlankPage(String name) {
        Inventory page = Bukkit.createInventory(null, 54, name);

        ItemStack nextpage = new ItemStack(Material.ARROW, 1);
        ItemMeta meta = nextpage.getItemMeta();
        meta.setDisplayName("§cPróxima página");
        nextpage.setItemMeta(meta);

        ItemStack prevpage = new ItemStack(Material.ARROW, 1);
        meta = prevpage.getItemMeta();
        meta.setDisplayName("§cPágina anterior");
        prevpage.setItemMeta(meta);


        ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE);

        for(int i = 0; i < 10; i++) {
            page.setItem(i, vidro);
        }
        page.setItem(18, vidro);
        page.setItem(27, vidro);
        page.setItem(36, vidro);
        page.setItem(45, vidro);
        page.setItem(17, vidro);
        page.setItem(26, vidro);
        page.setItem(35, vidro);

        for(int i = 44; i < 54; i++) {
            page.setItem(i, vidro);
        }

        page.setItem(50, nextpage);
        page.setItem(48, prevpage);
        return page;
    }
}
