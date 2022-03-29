package me.fanjoker.headcreator.api;

import me.fanjoker.headcreator.objects.HCBlock;
import me.fanjoker.headcreator.objects.HCConfig;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class BreakHeadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private HCBlock hcBlock;
    private Player player;

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
        return null;
    }
}
