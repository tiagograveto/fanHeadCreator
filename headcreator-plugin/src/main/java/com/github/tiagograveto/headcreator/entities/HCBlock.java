package com.github.tiagograveto.headcreator.entities;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HCBlock {

    private Location loc;
    private HCConfig config;
    private boolean toggle;

    public HCBlock(Location loc, HCConfig config, boolean toggle) {
        this.loc = loc;
        this.config = config;
        this.toggle = toggle;
    }

    public Location getLoc() {
        return loc;
    }

    public HCConfig getConfig() {
        return config;
    }
    public void setConfig(HCConfig config) {
        this.config = config;
    }

    public boolean isToggle() {
        return toggle;
    }
    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public void teleport(Player p) {
        Location loc = this.loc.clone();
        loc.add(0.5, 0.5, 0.5);
        loc.setPitch(p.getLocation().getPitch());
        loc.setYaw(p.getLocation().getYaw());
        p.teleport(loc);
    }

}
