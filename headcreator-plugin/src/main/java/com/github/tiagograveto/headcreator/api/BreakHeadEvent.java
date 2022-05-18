package com.github.tiagograveto.headcreator.api;

import com.github.tiagograveto.headcreator.entities.HCBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BreakHeadEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    private final HCBlock hcBlock;
    private final Player player;

    public BreakHeadEvent(Player player, HCBlock hcBlock) {
        this.player = player;
        this.hcBlock = hcBlock;
    }

    public HCBlock getHCBlock() {
        return hcBlock;
    }
    public Player getPlayer() {
        return player;
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
