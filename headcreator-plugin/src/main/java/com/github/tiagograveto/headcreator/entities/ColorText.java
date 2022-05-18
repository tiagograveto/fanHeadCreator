package com.github.tiagograveto.headcreator.entities;

import org.bukkit.ChatColor;

public class ColorText {

    private String string;

    public ColorText(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
