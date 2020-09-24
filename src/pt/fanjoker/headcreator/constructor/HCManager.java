package pt.fanjoker.headcreator.constructor;

import org.bukkit.Location;

public class HCManager {

    private Location loc;
    private boolean toggle;
    private String type;
    private String owner;

    public HCManager(Location loc, boolean toggle, String type, String owner) {
        this.loc = loc;
        this.toggle = toggle;
        this.type = type;
        this.owner = owner;
    }
    public Location getLoc() {
        return loc;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public boolean isToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
