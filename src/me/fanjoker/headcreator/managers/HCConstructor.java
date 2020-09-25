package me.fanjoker.headcreator.managers;

import me.fanjoker.headcreator.Main;
import me.fanjoker.headcreator.objects.HCBlock;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HCConstructor {

    private Main main;

    public HCConstructor(Main main) {
        this.main = main;
    }

    private Map<Integer, HCBlock> map = new LinkedHashMap<>();
    public Map<Integer, HCBlock> getMap() {
        return map;
    }


    public HCBlock getByLocation(Location loc) {
        return getMap().values().stream()
                .filter(block -> block.getLoc().equals(loc))
                .findFirst()
                .orElse(null);

    }

    public int getIdByLocation(Location location) {

        for (int id : getMap().keySet()) {
            if (location.equals(getMap().get(id).getLoc())) {
                return id;
            }
        }
        return 0;

    }
}
