package com.github.tiagograveto.headcreator.entities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getPermission() {
        return permission;
    }
    public boolean hasPermission() {
        return this.permission != null;
    }

    public List<String> getHologram() {
        return hologram;
    }
    public boolean usesHologram() {
        return hologram != null;
    }

    public int getHeight() {
        return height;
    }
    public int getTotalCmds() {
        return this.commands.size();
    }

    public void executeCommands(Player p) {

        for(String str : this.commands) {
            if(str.startsWith("op:")) {
                String string = str
                        .replace("op:", "")
                        .replace("op: ", "")
                        .replace("%player%", p.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string);
                continue;
            }
            p.chat("/" + str);
        }

    }

    public void giveHeadConfigItem(Player target) {

        ItemStack itemFinal = getHeadConfigItemInInventory(target);

        if (itemFinal == null) {
            target.getInventory().addItem(itemStack);
            return;
        }

        if (itemFinal.getAmount() < 64) {
            itemFinal.setAmount(itemFinal.getAmount() + 1);
            return;

        }

        target.getInventory().addItem(this.itemStack);
    }

    private ItemStack getHeadConfigItemInInventory(Player target) {
        for (ItemStack itemStack : target.getInventory().getContents()) {
            if(itemStack == null || itemStack.getType() != Material.SKULL
                    || !itemStack.hasItemMeta() || !this.itemStack.isSimilar(itemStack)) continue;

            return itemStack;
        }
        return null;
    }
}
