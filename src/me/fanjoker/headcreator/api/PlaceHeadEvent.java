package me.fanjoker.headcreator.api;

import me.fanjoker.headcreator.objects.HCBlock;
import me.fanjoker.headcreator.objects.HCConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlaceHeadEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;
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
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }
}
