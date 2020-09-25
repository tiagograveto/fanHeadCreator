package me.fanjoker.headcreator.objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HCConfig {

    private String type;
    private ItemStack itemStack;
    private String permission;
    private List<String> hologram;
    private List<String> commands;
    private int height;

    public HCConfig(String type, ItemStack itemStack, String permission, List<String> hologram, List<String> commands, int height) {
        this.type = type;
        this.itemStack = itemStack;
        this.permission = permission;
        this.hologram = hologram;
        this.commands = commands;
        this.height = height;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
    public boolean hasPermission() {
        return this.permission != null;
    }

    public List<String> getHologram() {
        return hologram;
    }
    public void setHologram(List<String> hologram) {
        this.hologram = hologram;
    }

    public List<String> getCommands() {
        return commands;
    }
    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public void executeCommands(Player p) {
        List<String> lista = this.commands;
        for(String str : lista) {
            if(str.startsWith("op:")) {
                String string = str
                        .replace("op:", "")
                        .replace("%player%", p.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string);
                continue;
            }
            p.chat("/" + str);
        }

    }

    public boolean usesHologram() {
        return hologram != null;
    }
}
