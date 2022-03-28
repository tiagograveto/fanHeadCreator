package me.fanjoker.headcreator.managers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.fanjoker.headcreator.Main;
import me.fanjoker.headcreator.objects.HCBlock;
import me.fanjoker.headcreator.objects.HCConfig;
import me.fanjoker.headcreator.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HCSettings {

    private Main main;

    public HCSettings(Main main) {
        this.main = main;
    }

    private Map<String, HCConfig> heads = new ConcurrentHashMap<>();
    public Map<String, HCConfig> getHeads() {
        return heads;
    }

    private YamlConfiguration getConfig() {
        return Main.config.getConfig("config").getYaml();
    }

    public String colorText(String str) { return ChatColor.translateAlternateColorCodes('&', str); }

    public void loadHeadConfigs() {

        for (String type : getConfig().getConfigurationSection("HeadCreator.").getKeys(false)) {

            String path = "HeadCreator." + type + ".";

            String url = getConfig().getString(path + "texture");
            String name = colorText(getConfig().getString(path + "name"));
            ItemStack itemStack = new ItemBuilder(Material.SKULL_ITEM).head(url).name(name).build();

            String permission = getConfig().getString(path + "permission");
            List<String> hologram = getConfig().getStringList(path + "hologram").stream().map(this::colorText).collect(Collectors.toList());
            List<String> commands = getConfig().getStringList(path + "commands");
            int height = getConfig().getInt(path + "hologram-height");

            getHeads().put(type, new HCConfig(type, itemStack, permission, hologram, commands, height));

        }

    }

//    SETTINGS

    public boolean isOpenWithShift() {
        return getConfig().getBoolean("Config.OpenWithShift");
    }

    public boolean isDisableHologram() {
        return getConfig().getBoolean("Config.DisableHologram");
    }

    public boolean useHolograms() {
        return getConfig().getBoolean("Config.UseHolograms");
    }

    public HCConfig getHeadByItem(ItemStack itemInHand) {
        if (!itemInHand.hasItemMeta()) return null;

        for (HCConfig config : getHeads().values()) {
            if (config.getItemStack().getItemMeta().getDisplayName()
                    .equalsIgnoreCase(itemInHand.getItemMeta().getDisplayName())) {
                return config;
            }
        }
        return null;
    }

//    public boolean needsHologram(String type) {
//        return getConfig().getBoolean("HeadCreator." + type + ".use-hologram");
//    }

//    HEAD CONFIG

    public boolean existsType(String type) {
        return getType(type) != null;
    }

    public HCConfig getType(String name) {
        return getHeads().get(name);
    }

//
//    HEAD MANAGER
//
    public void giveHead(Player target, HCConfig type) {

        ItemStack itemFinal = getItem(target, type);

        if (itemFinal == null) {
            target.getInventory().addItem(type.getItemStack());
            return;
        }

        if (itemFinal.getAmount() < 64) {
            itemFinal.setAmount(itemFinal.getAmount() + 1);
            return;

        }

        target.getInventory().addItem(type.getItemStack());
    }
    public ItemStack getItem(Player target, HCConfig type) {
        for(ItemStack itemStack : target.getInventory().getContents()) {
            if(itemStack == null || itemStack.getType() != Material.SKULL
                    || !itemStack.hasItemMeta() || !type.getItemStack().isSimilar(itemStack)) continue;

            return itemStack;
        }
        return null;
    }

    public void createHologram(HCBlock hcBlock) {

        HCConfig type = hcBlock.getConfig();
        Location loc = hcBlock.getLoc().clone();

        if(type.usesHologram()) {

            loc.add(0.5, type.getHeight(), 0.5);

            Hologram hologram = HologramsAPI.createHologram(Main.getInstance(), loc);
            type.getHologram().stream().map(this::colorText).forEach(hologram::appendTextLine);

        }
    }
    public boolean reloadHolograms() {
        try {
            HologramsAPI.getHolograms(main).forEach(Hologram::delete);
            for (HCBlock hcBlock : main.getConstructor().getMap().values()) {
                if (hcBlock.getConfig().usesHologram())
                    createHologram(hcBlock);

            }
            return true;

        } catch (Exception e) {
            main.error("&cAconteceu um erro ao recarregar os hologramas, tente reiniciar o servidor.");
            return false;
        }

    }

}
