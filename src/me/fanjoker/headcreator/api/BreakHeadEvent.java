package me.fanjoker.headcreator.api;

import me.fanjoker.headcreator.objects.HCBlock;
import me.fanjoker.headcreator.objects.HCConfig;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class BreakHeadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private HCBlock hcBlock;

    public BreakHeadEvent(HCBlock hcBlock) {
        this.hcBlock = hcBlock;
    }

    public HCBlock getHCBlock() {
        return hcBlock;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }
}
