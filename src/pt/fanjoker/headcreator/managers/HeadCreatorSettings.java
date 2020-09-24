package pt.fanjoker.headcreator.managers;

import com.gmail.filoghost.holograms.api.Hologram;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.gmail.filoghost.holographicdisplays.HolographicDisplays;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import pt.fanjoker.headcreator.HeadCreator;
import pt.fanjoker.headcreator.utils.ItemBuilder;
import pt.fanjoker.headcreator.utils.ScrollerInventory;
import sun.applet.Main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HeadCreatorSettings {

    private YamlConfiguration getConfig() {
        return HeadCreator.config.getConfig("config").getYaml();
    }
    public boolean existsType(String type) {
        for(String str : getConfig().getConfigurationSection("HeadCreator.").getKeys(false)) {
            if(str.toLowerCase().equals(type.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    public String getType(String name) {
        for(String str : getConfig().getConfigurationSection("HeadCreator.").getKeys(false)) {
            String name1 = getConfig().getString("HeadCreator." + str + ".name").replace("&", "§");
            if(name.equals(name1)) {
                return str;
            }
        }
        return null;
    }
    public List<String> getNames() {
        List<String> lista = new ArrayList<>();
        for(String str : getConfig().getConfigurationSection("HeadCreator.").getKeys(false)) {
            lista.add(getConfig().getString("HeadCreator." + str + ".name").replace("&", "§"));
        }
        return lista;
    }
    public String getTypeFormatted(String name) {
        for (String str : getConfig().getConfigurationSection("HeadCreator.").getKeys(false)) {
//            String name1 = getConfig().getString("HeadCreator." + str + ".name").replace("&", "§");
            if (str.toLowerCase().equals(name.toLowerCase())) {
                return str;
            }
        }
        return null;
    }
    public boolean hasPermission(Player p, String type) {
        boolean bool = getConfig().getBoolean("HeadCreator." + type + ".permission-required");
        if(!bool) {
            return true;
        }
        String permission = getConfig().getString("HeadCreator." + type + ".permission");
        if(p.hasPermission(permission)) {
            return true;
        }
        return false;
    }
    public boolean needsHologram(String type) {
        return getConfig().getBoolean("HeadCreator." + type + ".use-hologram");
    }
    public void executeCommands(Player p, String type) {
        List<String> lista = getConfig().getStringList("HeadCreator." + type + ".commands");
        for(String str : lista) {
            if(str.startsWith("op:")) {
                String string = str
                        .replace("op:", "")
                        .replace("%player%", p.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string);
            } else {
                p.chat("/" + str);
            }
        }
    }
    public boolean isHead(ItemStack itemStack) {
        if(itemStack.hasItemMeta()) {
            return getNames().contains(itemStack.getItemMeta().getDisplayName());
        }
        return false;
    }


//
//    INVENTORY ITEMSTACK
//

    public void openInv(Player p) {
        ArrayList<ItemStack> items = getTicketItemStack();

        new ScrollerInventory(items, "Painel de Cabeças", p);
    }
    public ArrayList<ItemStack> getTicketItemStack() {
        ArrayList<ItemStack> items = new ArrayList<>();
        List<Integer> ids = HeadCreator.getManager().getAllIds();

        if(ids.size() == 0) return items;

        int lastElement = Iterables.getLast(ids) + 1;
        for(int i = ids.get(0); i < lastElement;) {

            while (HeadCreator.getManager().getTarget(i) == null) {
                if (lastElement == i) {
                    return items;
                }
                i++;
            }

            String owner = HeadCreator.getManager().getTarget(i);
            String type = HeadCreator.getManager().getType(i);
            String url = getConfig().getString("HeadCreator." + type + ".texture");
            Location loc = HeadCreator.getManager().getLocation(i);

            ItemStack itemStack = new ItemBuilder(getSkull(url)).removeAttributes().build();
            SkullMeta sm = (SkullMeta) itemStack.getItemMeta();
            sm.setDisplayName("§7#" + i);
            ArrayList<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("§eCriador: §f" + owner);
            lore.add("§eTipo: §f" + type);
            lore.add("§eLocalização:");
            lore.add(" §eX: §a(§a" + loc.getX() + "§a)");
            lore.add(" §eY: §a(§a" + loc.getY() + "§a)");
            lore.add(" §eZ: §a(§a" + loc.getZ() + "§a)");
            lore.add("");
            lore.add("§7Clique para obter mais informações");
            lore.add("");
            sm.setLore(lore);
            itemStack.setItemMeta(sm);

            items.add(itemStack);

            i++;
        }

        return items;
    }

//
//    HEAD MANAGER
//
    public void sendHead(Player target, String type) {
        type = getTypeFormatted(type);
        String url = getConfig().getString("HeadCreator." + type + ".texture");
        String name = getConfig().getString("HeadCreator." + type + ".name").replace("&", "§");
        ItemStack itemStack = new ItemBuilder(getSkull(url)).name(name).build();
        addItem(target, type, itemStack);
    }
    public void addItem(Player target, String type, ItemStack itemStack) {

        ItemStack itemFinal = getItem(target, type);

        if(itemFinal == null) {

            target.getInventory().addItem(itemStack);
        } else if(itemFinal.getAmount() < 64) {

            itemFinal.setAmount(itemFinal.getAmount() + 1);
        } else {

            target.getInventory().addItem(itemStack);
        }
    }
    public ItemStack getItem(Player target, String type) {
        String name = getConfig().getString("HeadCreator." + type + ".name").replace("&", "§");
        for(ItemStack itemStack : target.getInventory().getContents()) {
            if(itemStack != null) {
                if(itemStack.hasItemMeta()) {
                    if (itemStack.getItemMeta().getDisplayName().equals(name) && itemStack.getType().equals(Material.SKULL_ITEM)) {
                        if(itemStack.getAmount() < 64) {
                            return itemStack;
                        }
                    }
                }
            }
        }
        return null;
    }
    public static ItemStack getSkull(String url) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (url == null || url.isEmpty())
            return skull;
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }

//    RELOAD HOLOGRAMS

//    public void createHologram(String type, Location loc) {
//        if(HeadCreator.getSettings().needsHologram(type)) {
//            loc.setY(loc.getY() + HeadCreator.config.getConfig("config").getYaml().getDouble("HeadCreator." + type + ".hologram-height"));
//            loc.setX(loc.getX() + 0.5);
//            loc.setZ(loc.getZ() + 0.5);
//            Hologram hologram = HolographicDisplaysAPI.createHologram(HeadCreator.getInstance(), loc);
//            List<String> lore = HeadCreator.config.getConfig("config").getYaml().getStringList("HeadCreator." + type + ".hologram");
//            for (String a : lore) {
//                hologram.addLine(a.replace("&", "§"));
//            }
//        }
//    }
//    public void reloadHolograms() {
//        for(com.gmail.filoghost.holographicdisplays.api.Hologram h : HologramsAPI.getHolograms(HeadCreator.getInstance())) {
//            h.delete();
//        }
//        Location loc = storage.getlocation;
//        loc.add(0, 2.8, 0);
//        Hologram hologram = HolographicDisplaysAPI.createHologram(HeadCreator.getInstance(), loc);
//        hologram.addLine("Linha1");
//        hologram.addLine("Linha2");
//    }
//    public List<Location> allLocs() {
//        List<Location> lista = new ArrayList<>();
//        for(String str : getConfig().getConfigurationSection("Hologramas").getKeys(false)) {
//            lista.add(Storage.getlocation("Hologramas." + str));
//        }
//        return lista;
//    }
    public void createHologram(String type, Location loc) {
        if(HeadCreator.getSettings().needsHologram(type)) {
            loc.setY(loc.getY() + HeadCreator.config.getConfig("config").getYaml().getDouble("HeadCreator." + type + ".hologram-height"));
            loc.setX(loc.getX() + 0.5);
            loc.setZ(loc.getZ() + 0.5);
            Hologram hologram = HolographicDisplaysAPI.createHologram(HeadCreator.getInstance(), loc);
            List<String> lore = HeadCreator.config.getConfig("config").getYaml().getStringList("HeadCreator." + type + ".hologram");
            for (String a : lore) {
                hologram.addLine(a.replace("&", "§"));
            }
        }
    }
    public void reloadHolograms() {
        for(com.gmail.filoghost.holographicdisplays.api.Hologram h : HologramsAPI.getHolograms(HeadCreator.getInstance())) {
            h.delete();
        }
        for(Location locs : HeadCreator.getManager().getAllLocs()) {
            int id = HeadCreator.getManager().getId(locs);
            String type = HeadCreator.getManager().getType(id);
            if(HeadCreator.config.getConfig("config").getYaml().getBoolean("Config.DisableHologram")
                    && !HeadCreator.getManager().getToggle(id)) {
                continue;
            }
            createHologram(type, locs.clone());
        }
    }

}
