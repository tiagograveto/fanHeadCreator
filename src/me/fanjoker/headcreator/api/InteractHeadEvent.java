package me.fanjoker.headcreator.api;

import me.fanjoker.headcreator.objects.HCBlock;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InteractHeadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private HCBlock hcBlock;

    public InteractHeadEvent(HCBlock hcBlock) {
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
