package com.github.tiagograveto.headcreator.controllers;

import com.github.tiagograveto.headcreator.config.Messages;
import com.github.tiagograveto.headcreator.entities.ColorText;
import com.github.tiagograveto.headcreator.entities.HCBlock;
import com.github.tiagograveto.headcreator.entities.HCConfig;
import com.github.tiagograveto.headcreator.services.inventories.entities.ItemBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HeadCreatorCacheController {

    private YamlConfiguration config;
    private Map<Integer, HCBlock> cacheBlocks;
    private Map<String, HCConfig> cacheConfig;

    public HeadCreatorCacheController(YamlConfiguration config) {
        this.config = config;
        this.cacheBlocks = new LinkedHashMap<>();
        this.cacheConfig = new ConcurrentHashMap<>();
        this.loadHeadConfigs();
    }

    public Map<Integer, HCBlock> getCacheBlocks() {
        return cacheBlocks;
    }

    private void loadHeadConfigs() {

        for (String type : config.getConfigurationSection("HeadCreator.").getKeys(false)) {

            String path = "HeadCreator." + type + ".";

            String url = config.getString(path + "texture");
            String name = new ColorText(config.getString(path + "name")).toString();
            ItemStack itemStack = new ItemBuilder(Material.SKULL_ITEM).head(url).name(name).build();
            String permission = config.getString(path + "permission");
            int height = config.getInt(path + "hologram-height");

            List<String> hologram = config.getStringList(path + "hologram").stream().map(str -> new ColorText(str).toString()).collect(Collectors.toList());
            List<String> commands = config.getStringList(path + "commands");

            cacheConfig.put(type, new HCConfig(type, itemStack, permission, hologram, commands, height));

        }

    }

    public void addHeadBlockInCache(int id, HCBlock block) {
        this.cacheBlocks.put(id, block);
    }

    public HCBlock getHeadBlockByLocation(Location loc) {

        for (int id : this.cacheBlocks.keySet()) {
            HCBlock hcBlock = this.cacheBlocks.get(id);
            if (loc.equals(hcBlock.getLoc())) {
                return hcBlock;
            }
        }
        return null;
    }

    public int getHeadBlockIdByLocation(Location location) {

        for (int id : this.cacheBlocks.keySet()) {
            if (location.equals(this.cacheBlocks.get(id).getLoc())) {
                return id;
            }
        }
        return 0;
    }

    public HCConfig getHeadConfigByItemStack(ItemStack itemInHand) {
        if (!itemInHand.hasItemMeta()) return null;

        for (HCConfig config : cacheConfig.values()) {
            if (config.getItemStack().getItemMeta().getDisplayName()
                    .equalsIgnoreCase(itemInHand.getItemMeta().getDisplayName())) {
                return config;
            }
        }
        return null;
    }

    public boolean existsHeadConfigType(String type) {
        return getHeadConfigType(type) != null;
    }

    public HCConfig getHeadConfigType(String name) {
        System.out.println(cacheConfig);
        return cacheConfig.get(name);
    }

    public void printHeadConfigTypes(Player p) {

        p.sendMessage(Messages.LIST_HEADS_HEADER);

        for (String str : cacheConfig.keySet()) {

            HCConfig hcConfig = cacheConfig.get(str);
            String body = Messages.LIST_HEADS_BODY.replace("%type%", str);
            String[] list = Messages.LIST_HEADS_HOVER;
            StringBuilder sb = new StringBuilder();

            for (String value : list) {
                sb.append(value).append("\n");
            }

            long amount = cacheBlocks.values().stream().filter(head -> head.getConfig().getType().equals(str)).count();

            String hover = sb.toString()
                    .replace("%cmds%" , "" + hcConfig.getTotalCmds())
                    .replace("%heads%", "" + amount)
                    .replace("%permission%", (hcConfig.hasPermission() ? hcConfig.getPermission() : "§cSem Permissão"));

            TextComponent textComponent = new TextComponent(body);
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(hover).create()));

            p.spigot().sendMessage(textComponent);

        }

        p.sendMessage(Messages.LIST_HEADS_FOOTER);
    }
}
