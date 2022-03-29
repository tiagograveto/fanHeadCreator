package me.fanjoker.headcreator.api;

import me.fanjoker.headcreator.objects.HCBlock;
import me.fanjoker.headcreator.objects.HCConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlaceHeadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private HCConfig hcConfig;
    private Player player;

    public PlaceHeadEvent(Player player, HCConfig hcConfig) {
        this.player = player;
        this.hcConfig = hcConfig;
    }

    public Player getPlayer() {
        return player;
    }
    public HCConfig getHCConfig() {
        return hcConfig;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
