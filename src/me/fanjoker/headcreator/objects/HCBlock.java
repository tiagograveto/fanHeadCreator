package me.fanjoker.headcreator.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

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
    public void setLoc(Location loc) {
        this.loc = loc;
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

}
